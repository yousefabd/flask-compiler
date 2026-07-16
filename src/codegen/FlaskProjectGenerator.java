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
import jinja2.symbol_table.semantic_rules.ISemanticRule;
import jinja2.symbol_table.semantic_rules.UlLiRule;
import jinja2.visitor.AntlrToTemplateAstVisitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import python.models.ASTNode;
import python.models.atom_statement.StringAtom;
import python.models.expr_statement.IDTrailer;
import python.models.root.Program;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.visitor.PythonVisitor;

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

            // ── 3. discover templates + the context each one receives ─
            Map<String, Set<String>> contexts = collectRenderContexts(program);

            // ── 4. parse + analyze every referenced template ──────────
            Map<String, TemplateFile> templates = new LinkedHashMap<>();
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
                    analyzeTemplate(file.toString(), ast, entry.getValue());
                    templates.put(name, ast);
                } catch (CompilerException e) {
                    reporter.report(e);  // keep checking the other templates
                }
            }

            // ── 5. stop before generation if anything failed ──────────
            if (reporter.hasErrors())
                return false;

            // ── 6. back end: generate Python + HTML output files ──────
            writeOutput(program, templates);
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
    private void analyzeTemplate(String fileName, TemplateFile ast, Set<String> contextVars) {
        jinja2.symbol_table.SymbolTable symtab = new jinja2.symbol_table.SymbolTable();
        for (String builtin : TEMPLATE_BUILTINS)
            symtab.define(new jinja2.symbol_table.Symbol(
                    builtin, jinja2.symbol_table.SymbolKind.VARIABLE, 0));
        for (String var : contextVars)
            symtab.define(new jinja2.symbol_table.Symbol(
                    var, jinja2.symbol_table.SymbolKind.VARIABLE, 0));

        List<jinja2.symbol_table.CompilerError> errors = new ArrayList<>();
        List<ISemanticRule> rules = new ArrayList<>();
        rules.add(new UlLiRule());
        new jinja2.symbol_table.SymbolTableBuilder(symtab, errors, rules).build(ast);

        for (jinja2.symbol_table.CompilerError e : errors)
            reporter.report(fileName, e);
    }

    // ─────────────────────────────────────────────────────────────
    // RENDER-CALL DISCOVERY
    // ─────────────────────────────────────────────────────────────

    /**
     * Walks the Python AST looking for {@code render_template('x.html', kw=...)}
     * calls and returns template name → set of context variable names.
     */
    private Map<String, Set<String>> collectRenderContexts(Program program) {
        Map<String, Set<String>> contexts = new LinkedHashMap<>();
        collectRenderCalls(program, contexts);
        return contexts;
    }

    private void collectRenderCalls(ASTNode node, Map<String, Set<String>> contexts) {
        if (node instanceof IDTrailer idt
                && "render_template".equals(idt.id.name)
                && idt.trailers != null && !idt.trailers.isEmpty()
                && idt.trailers.get(0).arguments instanceof CallArguments call) {
            registerRenderCall(call, contexts);
        }
        for (ASTNode child : node.getChildren())
            if (child != null)
                collectRenderCalls(child, contexts);
    }

    private void registerRenderCall(CallArguments call, Map<String, Set<String>> contexts) {
        if (call.args == null || call.args.isEmpty()) return;

        Argument first = call.args.get(0);
        if (first.isAssigned() || !(first.arg instanceof StringAtom template)) return;

        String name = stripQuotes(template.value);
        Set<String> vars = contexts.computeIfAbsent(name, k -> new HashSet<>());

        for (int i = 1; i < call.args.size(); i++) {
            Argument arg = call.args.get(i);
            if (arg.isAssigned() && arg.arg instanceof IDTrailer keyword
                    && (keyword.trailers == null || keyword.trailers.isEmpty()))
                vars.add(keyword.id.name);
        }
    }

    private static String stripQuotes(String raw) {
        String s = raw.strip();
        if (s.length() >= 2 && (s.startsWith("'") || s.startsWith("\""))
                && s.endsWith(s.substring(0, 1)))
            return s.substring(1, s.length() - 1);
        return s;
    }

    // ─────────────────────────────────────────────────────────────
    // OUTPUT
    // ─────────────────────────────────────────────────────────────

    private void writeOutput(Program program, Map<String, TemplateFile> templates) {
        // Python backend
        PythonCodeGenerator pyGen = new PythonCodeGenerator(appSource.toString());
        String pythonCode = pyGen.generate(program);
        writeFile(outputDir.resolve("app.py"), pythonCode);

        // HTML templates
        for (Map.Entry<String, TemplateFile> entry : templates.entrySet()) {
            TemplateCodeGenerator htmlGen = new TemplateCodeGenerator(entry.getKey());
            String html = htmlGen.generate(entry.getValue());
            writeFile(outputDir.resolve("templates").resolve(entry.getKey()), html);
        }

        // static assets are copied through unchanged (CSS is not compiled here)
        copyStaticAssets();
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
