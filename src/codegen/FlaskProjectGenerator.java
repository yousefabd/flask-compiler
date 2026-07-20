package codegen;

import antlr.html.HTMLLexer;
import antlr.html.HTMLParser;
import antlr.python.PythonLexer;
import antlr.python.PythonParser;

import errors.CodeGenError;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;

import jinja2.models.file.TemplateFile;
import jinja2.resolver.TemplateResolver;
import jinja2.symbol_table.semantic_rules.ISemanticRule;
import jinja2.symbol_table.semantic_rules.UlLiRule;
import jinja2.visitor.AntlrToTemplateAstVisitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import python.models.ASTNode;
import python.models.atom_statement.BoolAtom;
import python.models.atom_statement.FloatAtom;
import python.models.atom_statement.IntegerAtom;
import python.models.atom_statement.None;
import python.models.atom_statement.StringAtom;
import python.models.compound_statement.DecoratorStatement;
import python.models.expr_statement.Condition;
import python.models.expr_statement.IDTrailer;
import python.models.root.Program;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.resolver.PythonResolver;
import python.visitor.PythonVisitor;

import resolver.ConstantValue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * End-to-end driver of the miniFlask compiler:
 *
 * <pre>
 *   app.py ──► PythonLexer/Parser ──► Python AST ──► symbol table + checks ─┐
 *                                                                           ├─► code generation
 *   *.html ──► HTMLLexer/Parser  ──► Template AST ──► symbol table + checks ┘
 *                                                                           │
 *                                          generated/app.py, generated/templates/*.html
 * </pre>
 *
 * <p>Every stage runs behind this class's error boundary: syntax errors are
 * collected by {@link SyntaxErrorListener}, semantic errors by the two
 * symbol-table builders, and generation/I-O failures raise
 * {@link CompilerException}s. Nothing here ever crashes the caller — the
 * outcome is always "generated files + empty report" or "no files + report".</p>
 */
public class FlaskProjectGenerator {

    /** Names Flask/Jinja2 inject into every template context. */
    private static final List<String> TEMPLATE_BUILTINS = List.of(
            "url_for", "get_flashed_messages", "request", "session",
            "config", "g", "range", "dict", "namespace");

    private final Path appSource;      // e.g. tests/app.py
    private final Path templatesDir;   // e.g. tests/templates
    private final Path staticDir;      // e.g. tests/static (optional)
    private final Path outputDir;      // e.g. generated
    private final ErrorReporter reporter;

    public FlaskProjectGenerator(Path appSource, Path templatesDir, Path staticDir,
                                 Path outputDir, ErrorReporter reporter) {
        this.appSource = appSource;
        this.templatesDir = templatesDir;
        this.staticDir = staticDir;
        this.outputDir = outputDir;
        this.reporter = reporter;
    }

    /**
     * Runs the whole pipeline. Returns {@code true} when the project was
     * generated; {@code false} when errors were reported instead.
     */
    public boolean generate() {
        try {
            // ── 1. front end: parse the Flask backend ────────────────
            Program program = parsePython(appSource);
            if (program == null)
                return false;   // syntax errors already reported

            // ── 2. semantic analysis of the backend ──────────────────
            List<python.symbol_table.CompilerError> pyErrors = new ArrayList<>();
            python.symbol_table.SymbolTable pySymtab = new python.symbol_table.SymbolTable();
            new python.symbol_table.SymbolTableBuilder(pySymtab, pyErrors).build(program);
            for (python.symbol_table.CompilerError e : pyErrors)
                reporter.report(appSource.toString(), e);

            // ── 2b. resolution: bind every identifier read to its declaration,
            //       detect genuinely undefined names, track compile-time-known
            //       values — the SymbolTableBuilder above only declares symbols,
            //       it never visits a condition/iterable/RHS to resolve a *read*
            PythonResolver pyResolver = new PythonResolver();
            pyResolver.resolve(program);
            for (python.symbol_table.CompilerError e : pyResolver.getErrors())
                reporter.report(appSource.toString(), e);

            // ── 3. discover templates + the render_template call sites ────
            List<RenderCall> renderCalls = collectRenderCalls(program);
            Map<String, Set<String>> contexts = new LinkedHashMap<>();
            for (RenderCall call : renderCalls)
                contexts.computeIfAbsent(call.templateName(), k -> new HashSet<>()).addAll(call.argNames());

            // ── 4. parse + analyze every referenced template ──────────
            Map<String, TemplateFile> templates = new LinkedHashMap<>();
            Map<String, TemplateResolver> templateResolvers = new LinkedHashMap<>();
            for (Map.Entry<String, Set<String>> entry : contexts.entrySet()) {
                String name = entry.getKey();
                Path file = templatesDir.resolve(name);
                if (!Files.exists(file)) {
                    reporter.report(new errors.SemanticError(appSource.toString(), -1,
                            "render_template refers to missing template '" + name + "'"));
                    continue;
                }
                try {
                    TemplateFile ast = parseTemplate(file);
                    if (ast == null)
                        continue;   // syntax errors already reported
                    jinja2.symbol_table.SymbolTable symtab = analyzeTemplate(file.toString(), ast, entry.getValue());
                    templates.put(name, ast);
                    templateResolvers.put(name, new TemplateResolver(symtab));
                } catch (CompilerException e) {
                    reporter.report(e);  // keep checking the other templates
                }
            }

            // ── 5. stop before generation if anything failed ──────────
            if (reporter.hasErrors())
                return false;

            // ── 6. back end: generate Python + HTML output files ──────
            writeOutput(program, templates, renderCalls);
            writeReport(program, pyResolver, templates, templateResolvers);
            return true;

        } catch (CompilerException e) {
            reporter.report(e);
            return false;
        } catch (RuntimeException e) {
            // last-resort guard: an unexpected bug must be reported, not thrown
            reporter.reportUnexpected(CompilerStage.CODE_GENERATION,
                    appSource.toString(), e);
            return false;
        }
    }

    /**
     * One {@code render_template('name.html', kw=value, ...)} call site,
     * discovered by walking the Python AST. {@code literalArgs} holds only the
     * keyword arguments whose value is a provable compile-time literal (e.g.
     * {@code page='home'}); {@code argNames} holds every keyword argument name
     * regardless of whether its value is literal, which is what the existing
     * Flask-aware template seeding (undefined-variable checking) has always used.
     */
    private record RenderCall(String functionName, String templateName,
                              Set<String> argNames, Map<String, ConstantValue> literalArgs) {}

    // ─────────────────────────────────────────────────────────────
    // PARSING (with collected syntax errors)
    // ─────────────────────────────────────────────────────────────

    private Program parsePython(Path file) {
        CharStream input = readSource(file);
        SyntaxErrorListener listener = new SyntaxErrorListener(file.toString());

        PythonLexer lexer = new PythonLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        ParseTree tree = parser.prog();
        if (listener.hasErrors()) {
            for (errors.CompilerProblem p : listener.getErrors())
                reporter.report(p);
            return null;    // do not build an AST from a broken parse tree
        }

        return (Program) new PythonVisitor().visit(tree);
    }

    private TemplateFile parseTemplate(Path file) {
        CharStream input = readSource(file);
        SyntaxErrorListener listener = new SyntaxErrorListener(file.toString());

        HTMLLexer lexer = new HTMLLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        HTMLParser parser = new HTMLParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        ParseTree tree = parser.template();
        if (listener.hasErrors()) {
            for (errors.CompilerProblem p : listener.getErrors())
                reporter.report(p);
            return null;    // do not build an AST from a broken parse tree
        }

        return (TemplateFile) new AntlrToTemplateAstVisitor().visit(tree);
    }

    private CharStream readSource(Path file) {
        try {
            return CharStreams.fromPath(file);
        } catch (IOException e) {
            throw new errors.ParseError(file.toString(),
                    "Cannot read source file: " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TEMPLATE SEMANTIC ANALYSIS (Flask-aware)
    // ─────────────────────────────────────────────────────────────

    /**
     * Runs the existing Jinja2 semantic analysis, but first seeds the
     * template scope with (a) the names Flask injects into every render and
     * (b) the keyword arguments the backend passes via render_template —
     * so cross-file "undefined variable" checking actually understands Flask.
     */
    private jinja2.symbol_table.SymbolTable analyzeTemplate(
            String fileName, TemplateFile ast, Set<String> contextVars) {
        jinja2.symbol_table.SymbolTable symtab = new jinja2.symbol_table.SymbolTable();
        for (String builtin : TEMPLATE_BUILTINS)
            symtab.define(new jinja2.symbol_table.Symbol(
                    builtin, jinja2.symbol_table.SymbolKind.VARIABLE, 0, null));
        for (String var : contextVars)
            symtab.define(new jinja2.symbol_table.Symbol(
                    var, jinja2.symbol_table.SymbolKind.VARIABLE, 0, null));

        List<jinja2.symbol_table.CompilerError> errors = new ArrayList<>();
        List<ISemanticRule> rules = new ArrayList<>();
        rules.add(new UlLiRule());
        rules.add(new jinja2.symbol_table.semantic_rules.TypeCheckerRule());
        new jinja2.symbol_table.SymbolTableBuilder(symtab, errors, rules).build(ast);

        for (jinja2.symbol_table.CompilerError e : errors)
            reporter.report(fileName, e);

        return symtab;
    }

    // ─────────────────────────────────────────────────────────────
    // RENDER-CALL DISCOVERY
    // ─────────────────────────────────────────────────────────────

    /**
     * Walks the Python AST looking for {@code render_template('x.html', kw=...)}
     * calls. Each call site is recorded with (a) every keyword argument name,
     * used for the existing Flask-aware undefined-variable seeding, and
     * (b) only the keyword arguments whose value is a provable literal, used
     * by the optional per-call-site template evaluation in {@link #writeOutput}.
     */
    private List<RenderCall> collectRenderCalls(Program program) {
        List<RenderCall> calls = new ArrayList<>();
        collectRenderCalls(program, "module", calls);
        return calls;
    }

    private void collectRenderCalls(ASTNode node, String currentFunction, List<RenderCall> calls) {
        String nextFunction = currentFunction;
        if (node instanceof DecoratorStatement ds && ds.function != null && ds.function.id != null)
            nextFunction = ds.function.id.name;

        if (node instanceof IDTrailer idt
                && "render_template".equals(idt.id.name)
                && idt.trailers != null && !idt.trailers.isEmpty()
                && idt.trailers.get(0).arguments instanceof CallArguments call) {
            RenderCall rc = buildRenderCall(currentFunction, call);
            if (rc != null) calls.add(rc);
        }
        for (ASTNode child : node.getChildren())
            if (child != null)
                collectRenderCalls(child, nextFunction, calls);
    }

    private RenderCall buildRenderCall(String functionName, CallArguments call) {
        if (call.args == null || call.args.isEmpty()) return null;

        Argument first = call.args.get(0);
        if (first.isAssigned() || !(first.arg instanceof StringAtom template)) return null;

        String templateName = stripQuotes(template.value);
        Set<String> argNames = new HashSet<>();
        Map<String, ConstantValue> literalArgs = new LinkedHashMap<>();

        for (int i = 1; i < call.args.size(); i++) {
            Argument arg = call.args.get(i);
            if (!arg.isAssigned() || !(arg.arg instanceof IDTrailer keyword)
                    || keyword.trailers == null || !keyword.trailers.isEmpty())
                continue;
            argNames.add(keyword.id.name);
            ConstantValue literal = literalOf(arg.assign);
            if (literal.isKnown())
                literalArgs.put(keyword.id.name, literal);
        }
        return new RenderCall(functionName, templateName, argNames, literalArgs);
    }

    /** Best-effort literal evaluation of a render_template keyword argument's value. */
    private static ConstantValue literalOf(Condition value) {
        if (value instanceof StringAtom sa)  return ConstantValue.ofString(stripQuotes(sa.value));
        if (value instanceof IntegerAtom ia) return ConstantValue.ofInt(ia.value);
        if (value instanceof FloatAtom fa)   return ConstantValue.ofFloat(fa.value);
        if (value instanceof BoolAtom ba)    return ConstantValue.ofBool(ba.value);
        if (value instanceof None)           return ConstantValue.none();
        return ConstantValue.unknown(); // a variable, call, expression, ... — not provable here
    }

    private static String stripQuotes(String raw) {
        String s = raw.strip();
        for (String q : new String[]{"'''", "\"\"\"", "'", "\""})
            if (s.length() >= 2 * q.length() && s.startsWith(q) && s.endsWith(q))
                return s.substring(q.length(), s.length() - q.length());
        return s;
    }

    // ─────────────────────────────────────────────────────────────
    // OUTPUT
    // ─────────────────────────────────────────────────────────────

    private void writeOutput(Program program, Map<String, TemplateFile> templates,
                             List<RenderCall> renderCalls) {
        // Python backend
        PythonCodeGenerator pyGen = new PythonCodeGenerator(appSource.toString());
        String pythonCode = pyGen.generate(program);
        writeFile(outputDir.resolve("app.py"), pythonCode);

        // HTML templates — the LIVE templates Flask renders per-request at runtime.
        // Always generated with the default (no folding) constructor: the data a
        // route passes in (products, a looked-up product, ...) is only known once
        // a real request comes in, so these must stay dynamic Jinja2, never frozen.
        for (Map.Entry<String, TemplateFile> entry : templates.entrySet()) {
            TemplateCodeGenerator htmlGen = new TemplateCodeGenerator(entry.getKey());
            String html = htmlGen.generate(entry.getValue());
            writeFile(outputDir.resolve("templates").resolve(entry.getKey()), html);
        }

        // Compile-time "template evaluation" preview — one extra, clearly-separate
        // file per render_template call site whose keyword arguments included at
        // least one provable literal (e.g. page='home'). {% if page == 'home' %}
        // chains that depend only on such literals are folded to the taken branch;
        // everything else (loops over runtime data, filters, ...) stays live Jinja.
        // These previews are NEVER read by generated/app.py — they exist purely to
        // demonstrate what the resolver's constant values make possible.
        for (RenderCall call : renderCalls) {
            if (call.literalArgs().isEmpty()) continue;
            TemplateFile ast = templates.get(call.templateName());
            if (ast == null) continue;
            TemplateCodeGenerator evalGen = new TemplateCodeGenerator(call.templateName(), call.literalArgs());
            String html = evalGen.generate(ast);
            writeFile(outputDir.resolve("rendered").resolve(call.functionName() + ".html"), html);
        }

        // static assets are copied through unchanged (CSS is not compiled here)
        copyStaticAssets();
    }

    /**
     * Writes a single, readable text report combining the Python AST, both
     * symbol tables, and both resolvers' findings — for every variable it
     * shows the declaration, scope, inferred type/kind, resolved value (when
     * known) and every line it was used on.
     */
    private void writeReport(Program program, PythonResolver pyResolver,
                             Map<String, TemplateFile> templates,
                             Map<String, TemplateResolver> templateResolvers) {
        StringBuilder sb = new StringBuilder();

        sb.append("miniFlask Compiler Report\n");
        sb.append("=========================\n\n");

        sb.append("--- Python AST (").append(appSource).append(") ---\n");
        sb.append(new python.printer.ASTPrinter().treeToString(program)).append('\n');

        sb.append("--- Python Resolver (declarations, scopes, values, usages) ---\n");
        sb.append(pyResolver.report()).append('\n');

        for (Map.Entry<String, TemplateFile> entry : templates.entrySet()) {
            String name = entry.getKey();
            sb.append("--- Template AST (").append(name).append(") ---\n");
            sb.append(new jinja2.printer.ASTPrinter().treeToString(entry.getValue())).append('\n');

            TemplateResolver resolver = templateResolvers.get(name);
            if (resolver != null) {
                sb.append("--- Template Resolver (").append(name).append(") ---\n");
                sb.append(resolver.report()).append('\n');
            }
        }

        writeFile(outputDir.resolve("compiler_report.txt"), sb.toString());
    }

    private void writeFile(Path target, String content) {
        try {
            Files.createDirectories(target.getParent());
            Files.writeString(target, content, StandardCharsets.UTF_8);
            System.out.println("  generated " + target);
        } catch (IOException e) {
            throw new CodeGenError(target.toString(),
                    "Cannot write generated file: " + e.getMessage(), e);
        }
    }

    private void copyStaticAssets() {
        if (staticDir == null || !Files.isDirectory(staticDir)) return;
        try (var stream = Files.walk(staticDir)) {
            for (Path source : (Iterable<Path>) stream::iterator) {
                if (!Files.isRegularFile(source)) continue;
                Path target = outputDir.resolve("static")
                        .resolve(staticDir.relativize(source).toString());
                Files.createDirectories(target.getParent());
                Files.copy(source, target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("  copied    " + target);
            }
        } catch (IOException e) {
            throw new CodeGenError(staticDir.toString(),
                    "Cannot copy static assets: " + e.getMessage(), e);
        }
    }
}
