package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.preparation.*;
import compiler.runtime.CompiledApplication;
import errors.CompilerException;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.filters.JinjaFilterRegistry;
import jinja2.functions.JinjaFunctionRegistry;
import jinja2.renderer.ExpressionEvaluator;
import jinja2.renderer.TemplateRenderer;
import jinja2.tests.JinjaTestRegistry;
import utils.CompilerSettings;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class CompilationPipeline {

    private final ErrorReporter reporter;
    private final PythonBackendPreparer backendPreparer;
    private final TemplateProjectPreparer templatePreparer;
    private final TemplateRenderer templateRenderer;
    private final Path appSource;
    private final Path templatesDirectory;

    public CompilationPipeline() {
        this(CompilerSettings.appSource, CompilerSettings.templatesDir);
    }

    public CompilationPipeline(Path appSource, Path templatesDirectory) {
        this.appSource = Objects.requireNonNull(appSource);
        this.templatesDirectory = Objects.requireNonNull(templatesDirectory);
        this.reporter =
                new ErrorReporter();

        JinjaTestRegistry testRegistry =
                new JinjaTestRegistry();

        this.backendPreparer =
                new PythonBackendPreparer(
                        this.appSource,
                        this.reporter
                );

        this.templatePreparer =
                new TemplateProjectPreparer(
                        this.templatesDirectory,
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
     * Renders one function result through the Java interpreter and renderer.
     */
    public void compileSnapshot(
            CompiledApplication application,
            String ownerFunctionName
    ) {
        Objects.requireNonNull(application);

        CompilerStage currentStage =
                CompilerStage.PYTHON_EXECUTION;

        String currentSource =
                appSource.toString();

        try {
            TemplateRenderRequest renderRequest =
                    application.invokeRenderFunction(
                            ownerFunctionName
                    );

            currentStage =
                    CompilerStage.CODE_GENERATION;

            currentSource =
                    templatesDirectory
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

        if (preparation == null || reporter.hasErrors()) {
            finishCompilation();
            return null;
        }

        try {
            return new CompiledApplication(
                    appSource,
                    templatesDirectory,
                    preparation,
                    templateRenderer
            );

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    CompilerStage.PYTHON_EXECUTION,
                    appSource.toString(),
                    exception
            );
        }

        finishCompilation();
        return null;
    }
    public List<CompilerProblem> getProblems() {
        return reporter.getProblems();
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
