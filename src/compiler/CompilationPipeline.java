package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.semantic.MissingFlaskVariableAnalyzer;
import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.CodeGenError;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.filters.JinjaFilterRegistry;
import jinja2.functions.JinjaFunctionRegistry;
import jinja2.models.file.TemplateFile;
import jinja2.renderer.ExpressionEvaluator;
import jinja2.renderer.RenderContext;
import jinja2.renderer.TemplateRenderer;
import jinja2.tests.JinjaTestRegistry;
import python.PythonFrontend;
import python.models.root.Program;
import utils.CompilerSettings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class CompilationPipeline {

    private final ErrorReporter reporter;
    private final TemplateRenderRequestProvider renderRequestProvider;
    private final TemplateRenderer templateRenderer;
    private final JinjaTestRegistry testRegistry;

    public CompilationPipeline(
            TemplateRenderRequestProvider renderRequestProvider
    ) {
        this.reporter =
                new ErrorReporter();

        this.renderRequestProvider =
                Objects.requireNonNull(renderRequestProvider);

        this.testRegistry =
                new JinjaTestRegistry();

        this.templateRenderer =
                new TemplateRenderer(
                        new ExpressionEvaluator(
                                new JinjaTestRegistry(),
                                new JinjaFilterRegistry(),
                                new JinjaFunctionRegistry()
                        )
                );
    }

    /**
     * Performs all analysis and generates one static snapshot.
     * The function name is temporary input for the current development
     * stage. Later, it can be replaced by a generation plan containing
     * all snapshots that should be produced.
     */
    public void compileSnapshot(
            String ownerFunctionName
    ) {
        CompilerStage currentStage =
                CompilerStage.PARSING;

        try {
            /*
             * Python front end
             */
            PythonFrontend pythonFrontend =
                    new PythonFrontend(
                            CompilerSettings.appSource,
                            reporter
                    );

            Program program =
                    pythonFrontend.parsePython();

            if (program == null) {
                finishCompilation();
                return;
            }

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            pythonFrontend.analyzePython(program);

            /*
             * Name resolution and type checking. Reported through the same
             * ErrorReporter as every other stage.
             */
            pythonFrontend.analyzeSemantics(program);

            /*
             * Python semantics are the foundation everything after this
             * depends on. Continuing past a broken program only produces
             * follow-on noise, so stop here.
             */
            if (reporter.hasErrors()) {
                finishCompilation();
                return;
            }

            /*
             * Discover render_template calls exactly once.
             */
            List<TemplateCall> templateCalls =
                    TemplateCallFinder.findTemplateCalls(
                            program
                    );

            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(
                            templateCalls
                    );

            /*
             * Jinja front end
             */
            currentStage =
                    CompilerStage.PARSING;

            TemplateFrontend templateFrontend =
                    new TemplateFrontend(
                            CompilerSettings.templatesDir,
                            reporter,
                            testRegistry
                    );

            Map<String, TemplateFile> templates =
                    templateFrontend.parseTemplates(
                            callsByTemplate.keySet()
                    );

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            Map<String, jinja2.symbol_table.SymbolTable>
                    templateSymbolTables =
                    analyzeTemplates(
                            templateFrontend,
                            templates,
                            callsByTemplate
                    );

            printTemplateSymbolTables(
                    templateSymbolTables
            );

            /*
             * Jinja's own analysis already reports UNDEFINED_VARIABLE for any
             * name a template needs that isn't in the (unioned) context set —
             * exactly the condition MissingFlaskVariableAnalyzer also checks,
             * since both are fed the same union of every call's arguments.
             * Running the Flask-side check anyway would double-report the
             * same root cause under two different error kinds. Stopping here
             * when Jinja already found something skips that overlap, the same
             * way a Python semantic error already skips template analysis
             * entirely above.
             */
            if (reporter.hasErrors()) {
                finishCompilation();
                return;
            }

            /*
             * Cross-stage check: what the templates need versus what the
             * routes actually pass. Only reachable when Jinja found nothing,
             * so anything reported here is guaranteed not to already be
             * covered by a Jinja UNDEFINED_VARIABLE for the same template.
             */
            for (python.symbol_table.CompilerError error :
                    MissingFlaskVariableAnalyzer.analyze(
                            templates,
                            callsByTemplate
                    )) {

                reporter.report(
                        CompilerSettings.appSource.toString(),
                        error
                );
            }

            if (reporter.hasErrors()) {
                finishCompilation();
                return;
            }

            /*
             * Code generation
             */
            currentStage =
                    CompilerStage.CODE_GENERATION;

            TemplateCall callToRender =
                    requireSingleCallFromFunction(
                            templateCalls,
                            ownerFunctionName
                    );

            String renderedHtml =
                    renderTemplateCall(
                            callToRender,
                            templates
                    );

            System.out.println();
            System.out.printf(
                    "Rendered %s from function %s:%n",
                    callToRender.templateName(),
                    callToRender.ownerFunctionName()
            );

            System.out.println(renderedHtml);

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    currentStage,
                    CompilerSettings.appSource.toString(),
                    exception
            );
        }

        finishCompilation();
    }

    private Map<String, jinja2.symbol_table.SymbolTable>
    analyzeTemplates(
            TemplateFrontend templateFrontend,
            Map<String, TemplateFile> templates,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Map<String, jinja2.symbol_table.SymbolTable>
                symbolTables =
                new LinkedHashMap<>();

        for (Map.Entry<String, TemplateFile> entry
                : templates.entrySet()) {

            String templateName =
                    entry.getKey();

            TemplateFile template =
                    entry.getValue();

            Set<String> contextVariables =
                    collectContextVariables(
                            templateName,
                            callsByTemplate
                    );

            System.out.printf(
                    "Analyzing template: %s with context=%s%n",
                    templateName,
                    contextVariables
            );

            jinja2.symbol_table.SymbolTable symbolTable =
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
                    call.contextArguments().keySet()
            );
        }

        return contextVariables;
    }

    private TemplateCall requireSingleCallFromFunction(
            List<TemplateCall> templateCalls,
            String ownerFunctionName
    ) {
        List<TemplateCall> matches = getTemplateCalls(templateCalls, ownerFunctionName);

        /*
         * If one function contains multiple render_template calls,
         * runtime inputs are needed to determine which call is reached.
         * Do not silently select the first one.
         */
        if (matches.size() > 1) {
            throw new CodeGenError(
                    CompilerSettings.appSource.toString(),
                    matches.getFirst().line(),
                    "Function '"
                            + ownerFunctionName
                            + "' contains "
                            + matches.size()
                            + " render_template calls. "
                            + "A runtime scenario is required to choose one"
            );
        }

        return matches.getFirst();
    }

    private static List<TemplateCall> getTemplateCalls(List<TemplateCall> templateCalls, String ownerFunctionName) {
        List<TemplateCall> matches =
                new ArrayList<>();

        for (TemplateCall call : templateCalls) {
            if (call.ownerFunctionName()
                    .equals(ownerFunctionName)) {

                matches.add(call);
            }
        }

        if (matches.isEmpty()) {
            throw new CodeGenError(
                    CompilerSettings.appSource.toString(),
                    -1,
                    "Function '"
                            + ownerFunctionName
                            + "' does not contain a render_template call"
            );
        }
        return matches;
    }

    private String renderTemplateCall(
            TemplateCall call,
            Map<String, TemplateFile> templates
    ) {

        /*
         * This is the important replacement:
         *
         * Before:
         * Map.of("name", "Yousef")
         *
         * Now:
         * actual values produced by executing Python.
         */
        TemplateRenderRequest renderRequest =
                renderRequestProvider.provide(call);
        TemplateFile template =
                templates.get(call.templateName());

        if (template == null) {
            throw new CodeGenError(
                    CompilerSettings.templatesDir
                            .resolve(renderRequest.templateName())
                            .toString(),
                    call.line(),
                    "The parsed template AST is unavailable"
            );
        }

        RenderContext renderContext =
                RenderContext.root(renderRequest.context(),renderRequest.environment());

        return templateRenderer.render(
                template,
                renderContext
        );
    }

    private void printTemplateSymbolTables(
            Map<String, jinja2.symbol_table.SymbolTable>
                    symbolTables
    ) {
        for (Map.Entry<String, jinja2.symbol_table.SymbolTable>
                entry : symbolTables.entrySet()) {

            System.out.println();
            System.out.println(
                    "Jinja Symbol Table: "
                            + entry.getKey()
            );

            System.out.println(entry.getValue());
        }
    }

    private void finishCompilation() {
        if (reporter.hasErrors()) {
            System.out.println("Compilation failed:");
            reporter.printReport();
            return;
        }

        System.out.println(
                "Compilation completed successfully."
        );
    }
}