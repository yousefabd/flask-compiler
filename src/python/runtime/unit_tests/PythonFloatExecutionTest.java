package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;
import java.util.Map;

public class PythonFloatExecutionTest {
    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "float_values.py"
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
        requireNumber(
                module.resolve("price"),
                19.95,
                "String-to-float conversion failed"
        );

        requireNumber(
                module.resolve("quantity"),
                3.0,
                "Integer-to-float conversion failed"
        );

        requireNumber(
                module.resolve("enabled_number"),
                1.0,
                "Boolean-to-float conversion failed"
        );

        requireNumber(
                module.resolve("default_number"),
                0.0,
                "Empty float() call failed"
        );

        requireNumber(
                module.resolve("parsed_price"),
                99.99,
                "float() inside a function failed"
        );

        System.out.println(
                "Python float builtin passed."
        );

        System.out.println(
                "Price: "
                        + module.resolve("price")
        );

        System.out.println(
                "Quantity: "
                        + module.resolve("quantity")
        );

        System.out.println(
                "Enabled: "
                        + module.resolve(
                        "enabled_number"
                )
        );

        System.out.println(
                "Default: "
                        + module.resolve(
                        "default_number"
                )
        );

        System.out.println(
                "Parsed: "
                        + module.resolve(
                        "parsed_price"
                )
        );
    }
    private static void requireNumber(
            Object actual,
            double expected,
            String message
    ) {
        if (!(actual instanceof Number number)
                || Double.compare(
                number.doubleValue(),
                expected
        ) != 0) {

            throw new AssertionError(
                    message
                            + ": expected "
                            + expected
                            + ", got "
                            + actual
            );
        }
    }
}
