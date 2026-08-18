package compiler;

import compiler.generation.TemplateRenderRequest;
import compiler.logging.AnalysisLog;
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
    private final AnalysisLog analysisLog;

    public CompilationPipeline() {
        this(CompilerSettings.appSource, CompilerSettings.templatesDir);
    }

    public CompilationPipeline(Path appSource, Path templatesDirectory) {
        this.appSource = Objects.requireNonNull(appSource);
        this.templatesDirectory = Objects.requireNonNull(templatesDirectory);
        this.reporter =
                new ErrorReporter();
        this.analysisLog =
                new AnalysisLog();

        JinjaTestRegistry testRegistry =
                new JinjaTestRegistry();

        this.backendPreparer =
                new PythonBackendPreparer(
                        this.appSource,
                        this.reporter,
                        this.analysisLog
                );

        this.templatePreparer =
                new TemplateProjectPreparer(
                        this.templatesDirectory,
                        this.reporter,
                        testRegistry,
                        this.analysisLog
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
    public String formatReport() {
        return reporter.formatReport();
    }
    public String formatAnalysisLog() {
        return analysisLog.format();
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
