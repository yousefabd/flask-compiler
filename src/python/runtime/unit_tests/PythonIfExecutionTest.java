package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;

public class PythonIfExecutionTest {
    public static void main(String[] args) {
        Path source = Path.of(
                "tests",
                "python",
                "if_values.py"
        );

        ErrorReporter reporter =
                new ErrorReporter();

        PythonFrontend frontend =
                new PythonFrontend(
                        source,
                        reporter
                );

        Program program =
                frontend.parsePython();

        if (program == null) {
            reporter.printReport();
            throw new AssertionError(
                    "Python parsing failed"
            );
        }

        frontend.analyzePython(program);

        PythonEnvironment module =
                PythonEnvironment.module();

        PythonInterpreter interpreter =
                new PythonInterpreter(
                        new PythonExpressionEvaluator()
                );

        interpreter.executeModule(
                program,
                module
        );
        require(
                "elif".equals(
                        module.resolve("selection")
                ),
                "Elif branch selection failed"
        );

        require(
                "else".equals(
                        module.resolve("fallback")
                ),
                "Else branch selection failed"
        );

        require(
                "positive".equals(
                        module.resolve(
                                "positive_result"
                        )
                ),
                "Positive function branch failed"
        );

        require(
                "negative".equals(
                        module.resolve(
                                "negative_result"
                        )
                ),
                "Negative function branch failed"
        );

        require(
                "zero".equals(
                        module.resolve("zero_result")
                ),
                "Zero function branch failed"
        );

        require(
                "allowed".equals(
                        module.resolve("adult_access")
                ),
                "Nested adult branch failed"
        );

        require(
                "minor".equals(
                        module.resolve("minor_access")
                ),
                "Nested minor branch failed"
        );

        require(
                "disabled".equals(
                        module.resolve(
                                "disabled_access"
                        )
                ),
                "Disabled branch failed"
        );

        System.out.println(
                "Python if execution passed."
        );

        System.out.println(
                "Selection: "
                        + module.resolve("selection")
        );

        System.out.println(
                "Fallback: "
                        + module.resolve("fallback")
        );

        System.out.println(
                "Classes: "
                        + module.resolve("positive_result")
                        + ", "
                        + module.resolve("zero_result")
                        + ", "
                        + module.resolve("negative_result")
        );

        System.out.println(
                "Access: "
                        + module.resolve("adult_access")
                        + ", "
                        + module.resolve("minor_access")
                        + ", "
                        + module.resolve("disabled_access")
        );
    }

    private static void require(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
