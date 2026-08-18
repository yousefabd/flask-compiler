package compiler.preparation;

import compiler.logging.AnalysisLog;
import compiler.template.TemplateContextValidator;
import errors.CompilerException;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import jinja2.semantic.JinjaFreeVariableResult;
import jinja2.symbol_table.SymbolTable;
import jinja2.tests.JinjaTestRegistry;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Handles project-wide parsing and semantic analysis
 * for all referenced Jinja templates.
 */
public final class TemplateProjectPreparer {

    private final Path templatesDirectory;
    private final ErrorReporter reporter;
    private final JinjaTestRegistry testRegistry;
    private final AnalysisLog analysisLog;

    public TemplateProjectPreparer(
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

    public TemplateCompilationResult prepare(
            PythonCompilationResult backend
    ) {
        Objects.requireNonNull(backend);

        CompilerStage currentStage =
                CompilerStage.PARSING;

        try {
            TemplateFrontend templateFrontend =
                    new TemplateFrontend(
                            templatesDirectory,
                            reporter,
                            testRegistry,
                            analysisLog
                    );

            Map<String, TemplateFile> templates =
                    templateFrontend.parseTemplates(
                            backend.callsByTemplate()
                                    .keySet()
                    );

            if (reporter.hasErrors()) {
                return null;
            }

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            Map<String, JinjaFreeVariableResult> freeVariables =
                    collectFreeVariables(
                            templateFrontend,
                            templates
                    );

            Map<String, SymbolTable> symbolTables =
                    analyzeTemplates(
                            templateFrontend,
                            templates,
                            freeVariables
                    );

            if (reporter.hasErrors()) {
                return null;
            }

            TemplateContextValidator contextValidator =
                    new TemplateContextValidator(
                            templatesDirectory
                    );

            for (CompilerProblem problem
                    : contextValidator.validate(
                            templates,
                            freeVariables,
                            backend.callsByTemplate()
                    )) {
                reporter.report(problem);
            }

            if (reporter.hasErrors()) {
                return null;
            }

            TemplateCompilationResult result =
                    new TemplateCompilationResult(
                            templates,
                            symbolTables
                    );

            return result;

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    currentStage,
                    templatesDirectory.toString(),
                    exception
            );
        }

        return null;
    }

    private Map<String, JinjaFreeVariableResult> collectFreeVariables(
            TemplateFrontend templateFrontend,
            Map<String, TemplateFile> templates
    ) {
        Map<String, JinjaFreeVariableResult> freeVariables =
                new LinkedHashMap<>();

        for (Map.Entry<String, TemplateFile> entry
                : templates.entrySet()) {
            freeVariables.put(
                    entry.getKey(),
                    templateFrontend.collectFreeVariables(
                            entry.getValue()
                    )
            );
        }

        return freeVariables;
    }

    private Map<String, SymbolTable> analyzeTemplates(
            TemplateFrontend templateFrontend,
            Map<String, TemplateFile> templates,
            Map<String, JinjaFreeVariableResult> freeVariables
    ) {
        Map<String, SymbolTable> symbolTables =
                new LinkedHashMap<>();

        for (Map.Entry<String, TemplateFile> entry
                : templates.entrySet()) {

            String templateName =
                    entry.getKey();

            TemplateFile template =
                    entry.getValue();

            JinjaFreeVariableResult templateFreeVariables =
                    freeVariables.get(templateName);

            analysisLog.record(
                    CompilerStage.SEMANTIC_ANALYSIS,
                    "Analyzing Jinja template: "
                            + templateName
                            + " with external variables="
                            + templateFreeVariables
                            .externalVariables()
                            .keySet()
            );

            SymbolTable symbolTable =
                    templateFrontend.analyzeTemplate(
                            templateName,
                            template,
                            templateFreeVariables
                                    .externalVariables()
                                    .keySet()
                    );

            symbolTables.put(
                    templateName,
                    symbolTable
            );

            if (reporter.hasErrors()) {
                return symbolTables;
            }
        }

        analysisLog.record(
                CompilerStage.SEMANTIC_ANALYSIS,
                "Analyzed "
                        + symbolTables.size()
                        + " unique Jinja template(s)."
        );

        return symbolTables;
    }


}
