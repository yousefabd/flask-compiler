package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;

public class GlobalValuesExecutionTest {
    public static void main(String[] args) {
        Path source = Path.of(
                "tests",
                "python",
                "global_values.py"
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
                Integer.valueOf(99).equals(
                        module.resolve(
                                "first_product_id"
                        )
                ),
                "Global products assignment failed"
        );

        require(
                "Replacement".equals(
                        module.resolve(
                                "first_product_name"
                        )
                ),
                "Global product value failed"
        );

        require(
                "replaced".equals(
                        module.resolve("status")
                ),
                "Second global declaration failed"
        );

        require(
                Integer.valueOf(2).equals(
                        module.resolve("count")
                ),
                "Repeated global mutation failed"
        );

        require(
                "module".equals(
                        module.resolve("label")
                ),
                "Local assignment incorrectly changed module value"
        );

        require(
                "local".equals(
                        module.resolve("shadow_result")
                ),
                "Local shadowing failed"
        );

        require(
                !module.contains("local_only"),
                "Function-local value escaped its frame"
        );

        System.out.println(
                "Python global execution passed."
        );

        System.out.println(
                "Product: "
                        + module.resolve(
                        "first_product_name"
                )
        );

        System.out.println(
                "Status: "
                        + module.resolve("status")
        );

        System.out.println(
                "Count: "
                        + module.resolve("count")
        );

        System.out.println(
                "Labels: "
                        + module.resolve("label")
                        + " / "
                        + module.resolve("shadow_result")
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
