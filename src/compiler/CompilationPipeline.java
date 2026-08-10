package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.preparation.*;
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
import python.runtime.PythonApplicationRuntime;
import utils.CompilerSettings;
import java.util.Map;

public final class CompilationPipeline {

    private final ErrorReporter reporter;
    private final PythonBackendPreparer backendPreparer;
    private final TemplateProjectPreparer templatePreparer;
    private final TemplateRenderer templateRenderer;

    public CompilationPipeline() {
        this.reporter =
                new ErrorReporter();

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
            PythonApplicationRuntime runtime =
                    new PythonApplicationRuntime(
                            CompilerSettings.appSource,
                            application.backend().program()
                    );

            TemplateRenderRequest renderRequest =
                    runtime.invokeRenderFunction(
                            ownerFunctionName
                    );

            String renderedHtml =
                    renderTemplateRequest(
                            renderRequest,
                            application.frontend().templates()
                    );

            System.out.println();

            System.out.printf(
                    "Rendered %s from function %s:%n",
                    renderRequest.templateName(),
                    ownerFunctionName
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

    private String renderTemplateRequest(
            TemplateRenderRequest renderRequest,
            Map<String, TemplateFile> templates
    ) {
        TemplateFile template =
                templates.get(
                        renderRequest.templateName()
                );

        if (template == null) {
            throw new CodeGenError(
                    CompilerSettings.templatesDir
                            .resolve(
                                    renderRequest.templateName()
                            )
                            .toString(),
                    -1,
                    "The parsed template AST is unavailable"
            );
        }

        RenderContext renderContext =
                RenderContext.root(
                        renderRequest.context(),
                        renderRequest.environment()
                );

        return templateRenderer.render(
                renderRequest.templateName(),
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