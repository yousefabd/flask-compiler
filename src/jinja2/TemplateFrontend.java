package jinja2;

import antlr.html.HTMLLexer;
import antlr.html.HTMLParser;
import errors.CompilerProblem;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.*;
import jinja2.symbol_table.semantic_rules.ISemanticRule;
import jinja2.symbol_table.semantic_rules.TypeCheckerRule;
import jinja2.symbol_table.semantic_rules.UlLiRule;
import jinja2.tests.JinjaTestRegistry;
import jinja2.visitor.AntlrToTemplateAstVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.CompilerUtils;

import java.nio.file.Path;
import java.util.*;

public final class TemplateFrontend {

    /**
     * Names Jinja makes available without the route supplying them. Public so
     * the missing-Flask-variable check excludes exactly the same set instead
     * of keeping a second copy that could drift.
     */
    public static final List<String> TEMPLATE_BUILTINS =
            List.of(
                    "url_for",
                    "get_flashed_messages",
                    "request",
                    "session",
                    "config",
                    "g",
                    "range",
                    "dict",
                    "namespace"
            );

    private final Path templatesDirectory;
    private final ErrorReporter reporter;
    private JinjaTestRegistry testRegistry;

    public TemplateFrontend(
            Path templatesDirectory,
            ErrorReporter reporter,
            JinjaTestRegistry testRegistry
    ) {
        this.templatesDirectory =
                Objects.requireNonNull(templatesDirectory);

        this.reporter =
                Objects.requireNonNull(reporter);

        this.testRegistry =
                Objects.requireNonNull(testRegistry);
    }

    public Map<String, TemplateFile> parseTemplates(
            Collection<String> templateNames
    ) {
        Map<String, TemplateFile> templates =
                new LinkedHashMap<>();

        for (String templateName : templateNames) {
            Path templatePath =
                    templatesDirectory.resolve(templateName).normalize();

            System.out.println(
                    "Parsing template: " + templatePath
            );

            TemplateFile template = parseTemplate(templatePath);

            if (template != null) {
                templates.put(templateName, template);
            }
        }

        return templates;
    }

    private TemplateFile parseTemplate(Path templatePath) {
        CharStream input =
                CompilerUtils.readSource(templatePath);

        SyntaxErrorListener listener =
                new SyntaxErrorListener(templatePath.toString());

        HTMLLexer lexer = new HTMLLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        HTMLParser parser =
                new HTMLParser(new CommonTokenStream(lexer));

        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        ParseTree tree = parser.template();

        if (listener.hasErrors()) {
            for (CompilerProblem problem : listener.getErrors()) {
                reporter.report(problem);
            }

            return null;
        }

        AntlrToTemplateAstVisitor visitor =
                new AntlrToTemplateAstVisitor();

        return (TemplateFile) visitor.visit(tree);
    }
    public SymbolTable analyzeTemplate(
            String templateName,
            TemplateFile template,
            Collection<String> contextVariables
    ) {
        SymbolTable symbolTable = new SymbolTable();

        // Names automatically available to Jinja templates.
        for (String builtin : TEMPLATE_BUILTINS) {
            symbolTable.define(
                    new Symbol(
                            builtin,
                            SymbolKind.VARIABLE,
                            0,
                            null
                    )
            );
        }

        // Names supplied through render_template(...).
        for (String variable : contextVariables) {
            symbolTable.define(
                    new Symbol(
                            variable,
                            SymbolKind.VARIABLE,
                            0,
                            null
                    )
            );
        }

        List<CompilerError> errors = new ArrayList<>();

        List<ISemanticRule> rules = List.of(
                new UlLiRule(),
                new TypeCheckerRule(testRegistry)
        );

        SymbolTableBuilder builder =
                new SymbolTableBuilder(
                        symbolTable,
                        errors,
                        rules
                );

        builder.build(template);

        Path templatePath =
                templatesDirectory.resolve(templateName).normalize();

        for (CompilerError error : errors) {
            reporter.report(templatePath.toString(), error);
        }

        return symbolTable;
    }
}