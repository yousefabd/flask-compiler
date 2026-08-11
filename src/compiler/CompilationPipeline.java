package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.preparation.*;
import compiler.runtime.CompiledApplication;
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
import java.util.Objects;

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
            CompiledApplication application,
            String ownerFunctionName
    ) {
        Objects.requireNonNull(application);

        CompilerStage currentStage =
                CompilerStage.PYTHON_EXECUTION;

        String currentSource =
                CompilerSettings.appSource.toString();

        try {
            TemplateRenderRequest renderRequest =
                    application.invokeRenderFunction(
                            ownerFunctionName
                    );

            currentStage =
                    CompilerStage.CODE_GENERATION;

            currentSource =
                    CompilerSettings.templatesDir
                            .resolve(renderRequest.templateName())
                            .toString();

            String renderedHtml =
                    application.render(renderRequest);

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
                    currentStage,
                    currentSource,
                    exception
            );
        }

        finishCompilation();
    }
    public CompiledApplication compileApplication() {
        PreparedApplication preparation =
                prepare();

        if (preparation == null) {
            finishCompilation();
            return null;
        }

        try {
            return new CompiledApplication(
                    CompilerSettings.appSource,
                    CompilerSettings.templatesDir,
                    preparation,
                    templateRenderer
            );

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    CompilerStage.PYTHON_EXECUTION,
                    CompilerSettings.appSource.toString(),
                    exception
            );
        }

        finishCompilation();
        return null;
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