package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;

public class PythonForExecutionTest {
    public static void main(String[] args) {
        Path source = Path.of(
                "tests",
                "python",
                "for_values.py"
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
                "Phone".equals(
                        module.resolve("found_name")
                ),
                "Product lookup loop failed"
        );

        require(
                module.resolve("missing_product") == null,
                "Missing product should return None"
        );

        require(
                Integer.valueOf(3).equals(
                        module.resolve("maximum_id")
                ),
                "Maximum product ID loop failed"
        );

        require(
                "firstsecond".equals(
                        module.resolve("pair_names")
                ),
                "Loop-variable unpacking failed"
        );

        require(
                Integer.valueOf(30).equals(
                        module.resolve("pair_total")
                ),
                "Unpacked loop calculation failed"
        );

        require(
                "ab".equals(
                        module.resolve("dictionary_keys")
                ),
                "Dictionary key iteration failed"
        );

        require(
                "empty".equals(
                        module.resolve("empty_status")
                ),
                "For-else behavior failed"
        );

        System.out.println(
                "Python for execution passed."
        );

        System.out.println(
                "Found: "
                        + module.resolve("found_name")
        );

        System.out.println(
                "Maximum ID: "
                        + module.resolve("maximum_id")
        );

        System.out.println(
                "Pairs: "
                        + module.resolve("pair_names")
                        + " / "
                        + module.resolve("pair_total")
        );

        System.out.println(
                "Dictionary keys: "
                        + module.resolve(
                        "dictionary_keys"
                )
        );

        System.out.println(
                "Empty-loop status: "
                        + module.resolve("empty_status")
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
