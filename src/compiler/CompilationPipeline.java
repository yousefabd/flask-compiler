package compiler;

import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import jinja2.renderer.ExpressionEvaluator;
import jinja2.renderer.RenderContext;
import jinja2.renderer.TemplateRenderer;
import python.PythonFrontend;
import python.execution.CPythonExecutor;
import python.models.root.Program;
import python.symbol_table.SymbolTable;
import utils.CompilerSettings;

import java.util.*;

public class CompilationPipeline {
    private final ErrorReporter reporter;
    public CompilationPipeline(){
        reporter = new ErrorReporter();
    }
    public void analyze() {

        CompilerStage currentStage = CompilerStage.PARSING;

        try {
            // Parse exactly once.
            PythonFrontend pythonFrontend = new PythonFrontend(
                    CompilerSettings.appSource,
                    reporter
            );
            Program program = pythonFrontend.parsePython();

            if (program == null) {
                System.out.println("Compilation failed:");
                reporter.printReport();
                return;
            }

            currentStage = CompilerStage.SEMANTIC_ANALYSIS;

            // Analyze the AST that was already created.
            SymbolTable pythonSymbolTable = pythonFrontend.analyzePython(program);
            List<TemplateCall> templateCalls =
                    TemplateCallFinder.findTemplateCalls(program);
            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(templateCalls);

            currentStage = CompilerStage.PARSING;

            TemplateFrontend templateFrontend =
                    new TemplateFrontend(
                            CompilerSettings.templatesDir,
                            reporter
                    );

            Map<String, TemplateFile> templates =
                    templateFrontend.parseTemplates(
                            callsByTemplate.keySet()
                    );

            currentStage = CompilerStage.SEMANTIC_ANALYSIS;

            Map<String, jinja2.symbol_table.SymbolTable>
                    templateSymbolTables = new LinkedHashMap<>();

            for (Map.Entry<String, TemplateFile> entry
                    : templates.entrySet()) {

                String templateName = entry.getKey();
                TemplateFile template = entry.getValue();

                Set<String> contextVariables =
                        new LinkedHashSet<>();

                for (TemplateCall call :
                        callsByTemplate.getOrDefault(
                                templateName,
                                List.of()
                        )) {
                    contextVariables.addAll(
                            call.contextArguments().keySet()
                    );
                }

                System.out.printf(
                        "Analyzing template: %s with context=%s%n",
                        templateName,
                        contextVariables
                );

                jinja2.symbol_table.SymbolTable templateSymbolTable =
                        templateFrontend.analyzeTemplate(
                                templateName,
                                template,
                                contextVariables
                        );

                templateSymbolTables.put(
                        templateName,
                        templateSymbolTable
                );
            }

            System.out.printf(
                    "Analyzed %d unique template(s).%n",
                    templateSymbolTables.size()
            );
            for (Map.Entry<String, jinja2.symbol_table.SymbolTable> entry
                    : templateSymbolTables.entrySet()) {

                System.out.println();
                System.out.println(
                        "Jinja Symbol Table: " + entry.getKey()
                );

                System.out.println(entry.getValue());
            }
            if (!reporter.hasErrors()) {
                currentStage = CompilerStage.CODE_GENERATION;

                TemplateFile testTemplate =
                        templates.get("render_test.html");

                RenderContext testContext =
                        RenderContext.root(
                                Map.of("name", "Yousef")
                        );

                TemplateRenderer renderer =
                        new TemplateRenderer(
                                new ExpressionEvaluator()
                        );

                String renderedHtml = renderer.render(
                        testTemplate,
                        testContext
                );
                CPythonExecutor cPythonExecutor = new CPythonExecutor(
                        CompilerSettings.pythonExecutable,
                        CompilerSettings.renderCaptureScript,
                        CompilerSettings.appSource
                );

                String capturedJson =
                        cPythonExecutor.executeCaptureScript("render_test");

                System.out.println();
                System.out.println("Context captured from CPython:");
                System.out.println(capturedJson);

                System.out.println();
                System.out.println("Rendered template:");
                System.out.println(renderedHtml);
            }
        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    currentStage,
                    CompilerSettings.appSource.toString(),
                    exception
            );
        }

        if (reporter.hasErrors()) {
            System.out.println("Compilation failed:");
            reporter.printReport();
            return;
        }

        System.out.println(
                "Python parsing and semantic analysis completed successfully."
        );
    }
}
