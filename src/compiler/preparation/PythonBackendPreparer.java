package compiler.preparation;

import compiler.logging.AnalysisLog;
import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.semantic.PythonSemanticResult;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handles every compile-time preparation step for the Python backend.
 * It does not execute Python code or create runtime values.
 */
public final class PythonBackendPreparer {

    private final Path appSource;
    private final ErrorReporter reporter;
    private final AnalysisLog analysisLog;

    public PythonBackendPreparer(
            Path appSource,
            ErrorReporter reporter,
            AnalysisLog analysisLog
    ) {
        this.appSource =
                Objects.requireNonNull(appSource);

        this.reporter =
                Objects.requireNonNull(reporter);

        this.analysisLog =
                Objects.requireNonNull(analysisLog);
    }

    public PythonCompilationResult prepare() {
        CompilerStage currentStage =
                CompilerStage.PARSING;

        try {
            PythonFrontend pythonFrontend =
                    new PythonFrontend(
                            appSource,
                            reporter
                    );
            analysisLog.record(
                    CompilerStage.PARSING,
                    "Parsing Python source: "
                            + appSource.normalize()
            );
            Program program =
                    pythonFrontend.parsePython();

            if (program == null) {
                return null;
            }

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            analysisLog.record(
                    CompilerStage.SEMANTIC_ANALYSIS,
                    "Analyzing Python source: "
                            + appSource.normalize()
            );
            PythonSemanticResult semanticResult =
                    pythonFrontend.analyzePython(program);

            if (semanticResult.hasErrors() || reporter.hasErrors()) {
                return null;
            }

            /*
             * Extract Flask/Jinja connections from the validated
             * Python AST.
             */
            List<TemplateCall> templateCalls =
                    TemplateCallFinder.findTemplateCalls(
                            program,
                            appSource.toString(),
                            reporter
                    );

            if (reporter.hasErrors()) {
                return null;
            }

            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(
                            templateCalls
                    );

            return new PythonCompilationResult(
                    program,
                    semanticResult,
                    templateCalls,
                    callsByTemplate
            );

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    currentStage,
                    appSource.toString(),
                    exception
            );
        }

        return null;
    }
}
