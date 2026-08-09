package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.preparation.*;
import compiler.template.TemplateCall;
import errors.CodeGenError;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.filters.JinjaFilterRegistry;
import jinja2.functions.JinjaFunctionRegistry;
import jinja2.models.file.TemplateFile;
import jinja2.renderer.ExpressionEvaluator;
import jinja2.renderer.RenderContext;
import jinja2.renderer.TemplateRenderer;
import jinja2.tests.JinjaTestRegistry;
import utils.CompilerSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CompilationPipeline {

    private final ErrorReporter reporter;
    private final PythonBackendPreparer backendPreparer;
    private final TemplateProjectPreparer templatePreparer;
    private final TemplateRenderRequestProvider renderRequestProvider;
    private final TemplateRenderer templateRenderer;

    public CompilationPipeline(
            TemplateRenderRequestProvider renderRequestProvider
    ) {
        this.reporter =
                new ErrorReporter();

        this.renderRequestProvider =
                Objects.requireNonNull(renderRequestProvider);
        
        JinjaTestRegistry testRegistry =
                new JinjaTestRegistry();

        this.backendPreparer =
                new PythonBackendPreparer(
                        CompilerSettings.appSource,
                        this.reporter
                );

        this.templatePreparer =
                new TemplateProjectPreparer(
                        CompilerSettings.templatesDir,
                        this.reporter,
                        testRegistry
                );

        this.templateRenderer =
                new TemplateRenderer(
                        new ExpressionEvaluator(
                                testRegistry,
                                new JinjaFilterRegistry(),
                                new JinjaFunctionRegistry()
                        )
                );
    }

    /**
     * Performs parsing, AST construction, symbol-table construction,
     * and semantic analysis for both sides of the application.
     * No Python execution or HTML generation happens here.
    /**
     * Prepares both sides of the application without executing
     * Python or generating HTML.
     */
    public PreparedApplication prepare() {
        PythonCompilationResult backend =
                backendPreparer.prepare();

        if (backend == null) {
            return null;
        }

        TemplateCompilationResult frontend =
                templatePreparer.prepare(backend);

        if (frontend == null) {
            return null;
        }

        return new PreparedApplication(
                backend,
                frontend
        );
    }

    /**
     * Temporary snapshot generation entry point.
     * This still uses CPython for comparison while the Java Python
     * interpreter is being implemented.
     */
    public void compileSnapshot(
            String ownerFunctionName
    ) {
        PreparedApplication application =
                prepare();

        if (application == null) {
            finishCompilation();
            return;
        }

        try {
            TemplateCall callToRender =
                    requireSingleCallFromFunction(
                            application.backend()
                                    .templateCalls(),
                            ownerFunctionName
                    );

            String renderedHtml =
                    renderTemplateCall(
                            callToRender,
                            application.frontend()
                                    .templates()
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
                    CompilerStage.CODE_GENERATION,
                    CompilerSettings.appSource.toString(),
                    exception
            );
        }

        finishCompilation();
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
                call.templateName(),
                templates,
                renderContext
        );
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