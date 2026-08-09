package compiler.preparation;

import compiler.template.TemplateCall;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.dependency.TemplateDependencyFinder;
import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.SymbolTable;
import jinja2.tests.JinjaTestRegistry;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Handles project-wide parsing and semantic analysis
 * for all referenced Jinja templates.
 */
public final class TemplateProjectPreparer {

    private final Path templatesDirectory;
    private final ErrorReporter reporter;
    private final JinjaTestRegistry testRegistry;

    public TemplateProjectPreparer(
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
                            testRegistry
                    );

            Map<String, TemplateFile> templates =
                    templateFrontend.parseTemplates(
                            backend.callsByTemplate()
                                    .keySet()
                    );

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            Map<String, SymbolTable> symbolTables =
                    analyzeTemplates(
                            templateFrontend,
                            templates,
                            backend.callsByTemplate()
                    );

            TemplateCompilationResult result =
                    new TemplateCompilationResult(
                            templates,
                            symbolTables
                    );

            printSymbolTables(
                    result.symbolTables()
            );

            if (reporter.hasErrors()) {
                return null;
            }

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

    private Map<String, SymbolTable> analyzeTemplates(
            TemplateFrontend templateFrontend,
            Map<String, TemplateFile> templates,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Map<String, SymbolTable> symbolTables =
                new LinkedHashMap<>();

        Map<String, Set<String>> contextByTemplate =
                buildTemplateContexts(
                        templates,
                        callsByTemplate
                );

        for (Map.Entry<String, TemplateFile> entry
                : templates.entrySet()) {

            String templateName =
                    entry.getKey();

            TemplateFile template =
                    entry.getValue();

            Set<String> contextVariables =
                    contextByTemplate.get(templateName);

            System.out.printf(
                    "Analyzing template: %s with context=%s%n",
                    templateName,
                    contextVariables
            );

            SymbolTable symbolTable =
                    templateFrontend.analyzeTemplate(
                            templateName,
                            template,
                            contextVariables
                    );

            symbolTables.put(
                    templateName,
                    symbolTable
            );
        }

        System.out.printf(
                "Analyzed %d unique template(s).%n",
                symbolTables.size()
        );

        return symbolTables;
    }

    private Map<String, Set<String>> buildTemplateContexts(
            Map<String, TemplateFile> templates,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Map<String, Set<String>> contextByTemplate =
                new LinkedHashMap<>();

        for (String templateName : templates.keySet()) {
            contextByTemplate.put(
                    templateName,
                    collectContextVariables(
                            templateName,
                            callsByTemplate
                    )
            );
        }

        boolean contextChanged;

        do {
            contextChanged = false;

            for (Map.Entry<String, TemplateFile> entry
                    : templates.entrySet()) {

                Set<String> parentContext =
                        contextByTemplate.get(
                                entry.getKey()
                        );

                for (String includedTemplate :
                        TemplateDependencyFinder
                                .findStaticIncludes(
                                        entry.getValue()
                                )) {

                    Set<String> includedContext =
                            contextByTemplate.get(
                                    includedTemplate
                            );

                    if (includedContext != null
                            && includedContext.addAll(
                            parentContext
                    )) {

                        contextChanged = true;
                    }
                }
            }
        } while (contextChanged);

        return contextByTemplate;
    }

    private Set<String> collectContextVariables(
            String templateName,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Set<String> contextVariables =
                new LinkedHashSet<>();

        for (TemplateCall call :
                callsByTemplate.getOrDefault(
                        templateName,
                        List.of()
                )) {

            contextVariables.addAll(
                    call.contextArguments()
                            .keySet()
            );
        }

        return contextVariables;
    }

    private void printSymbolTables(
            Map<String, SymbolTable> symbolTables
    ) {
        for (Map.Entry<String, SymbolTable> entry
                : symbolTables.entrySet()) {

            System.out.println();

            System.out.println(
                    "Jinja Symbol Table: "
                            + entry.getKey()
            );

            System.out.println(entry.getValue());
        }
    }
}