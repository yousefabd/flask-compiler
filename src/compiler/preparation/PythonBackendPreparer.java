package compiler.preparation;

import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.symbol_table.SymbolTable;

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

    public PythonBackendPreparer(
            Path appSource,
            ErrorReporter reporter
    ) {
        this.appSource =
                Objects.requireNonNull(appSource);

        this.reporter =
                Objects.requireNonNull(reporter);
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

            Program program =
                    pythonFrontend.parsePython();

            if (program == null) {
                return null;
            }

            currentStage =
                    CompilerStage.SEMANTIC_ANALYSIS;

            SymbolTable symbolTable =
                    pythonFrontend.analyzePython(program);

            /*
             * Extract Flask/Jinja connections from the validated
             * Python AST.
             */
            List<TemplateCall> templateCalls =
                    TemplateCallFinder.findTemplateCalls(
                            program
                    );

            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(
                            templateCalls
                    );

            return new PythonCompilationResult(
                    program,
                    symbolTable,
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