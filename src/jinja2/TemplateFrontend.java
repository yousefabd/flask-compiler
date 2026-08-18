package jinja2;

import antlr.html.HTMLLexer;
import antlr.html.HTMLParser;
import compiler.logging.AnalysisLog;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import jinja2.dependency.TemplateDependencyFinder;
import jinja2.models.file.TemplateFile;
import jinja2.semantic.JinjaBuiltinCatalog;
import jinja2.semantic.JinjaFreeVariableCollector;
import jinja2.semantic.JinjaFreeVariableResult;
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

    private final Path templatesDirectory;
    private final ErrorReporter reporter;
    private final JinjaTestRegistry testRegistry;
    private final AnalysisLog analysisLog;

    public TemplateFrontend(
            Path templatesDirectory,
            ErrorReporter reporter,
            JinjaTestRegistry testRegistry
    ) {
        this(
                templatesDirectory,
                reporter,
                testRegistry,
                new AnalysisLog()
        );
    }

    public TemplateFrontend(
            Path templatesDirectory,
            ErrorReporter reporter,
            JinjaTestRegistry testRegistry,
            AnalysisLog analysisLog
    ) {
        this.templatesDirectory =
                Objects.requireNonNull(
                        templatesDirectory
                );

        this.reporter =
                Objects.requireNonNull(reporter);

        this.testRegistry =
                Objects.requireNonNull(testRegistry);

        this.analysisLog =
                Objects.requireNonNull(analysisLog);
    }

    public Map<String, TemplateFile> parseTemplates(
            Collection<String> templateNames
    ) {
        Map<String, TemplateFile> templates =
                new LinkedHashMap<>();

        Deque<String> pendingTemplates =
                new ArrayDeque<>(
                        new LinkedHashSet<>(templateNames)
                );

        while (!pendingTemplates.isEmpty()) {
            String templateName =
                    pendingTemplates.removeFirst();

            if (templates.containsKey(templateName)) {
                continue;
            }

            Path templatePath =
                    templatesDirectory.resolve(templateName).normalize();

            analysisLog.record(
                    CompilerStage.PARSING,
                    "Parsing Jinja template: "
                            + templatePath
            );
            TemplateFile template = parseTemplate(templatePath);

            if (template != null) {
                templates.put(templateName, template);

                for (String includedTemplate :
                        TemplateDependencyFinder
                                .findStaticIncludes(template)) {

                    if (!templates.containsKey(includedTemplate)) {
                        pendingTemplates.addLast(includedTemplate);
                    }
                }
            } else {
                break;
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
    public JinjaFreeVariableResult collectFreeVariables(
            TemplateFile template
    ) {
        return new JinjaFreeVariableCollector().collect(template);
    }

    public SymbolTable analyzeTemplate(
            String templateName,
            TemplateFile template,
            Collection<String> externalVariables
    ) {
        SymbolTable symbolTable = new SymbolTable();

        // Names automatically available to Jinja templates.
        for (String builtin : JinjaBuiltinCatalog.names()) {
            symbolTable.define(
                    new Symbol(
                            builtin,
                            SymbolKind.VARIABLE,
                            0,
                            null
                    )
            );
        }

        /*
         * External template requirements are defined here so ordinary Jinja
         * type/structure analysis can proceed without misclassifying a Flask
         * context omission as a Jinja UNDEFINED_VARIABLE. The separate
         * TemplateContextValidator checks which of these names are actually
         * supplied by render_template calls.
         */
        for (String variable : externalVariables) {
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
