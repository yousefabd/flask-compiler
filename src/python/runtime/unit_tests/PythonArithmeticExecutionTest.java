package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;

public final class PythonArithmeticExecutionTest {

    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "arithmetic_values.py"
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

        if (reporter.hasErrors()) {
            reporter.printReport();
            throw new AssertionError(
                    "Python semantic analysis failed"
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

        require(
                Integer.valueOf(60).equals(
                        module.resolve("calculated_total")
                ),
                "Multiplication failed"
        );

        require(
                Integer.valueOf(4).equals(
                        module.resolve("next_id")
                ),
                "Addition failed"
        );

        require(
                "Hello Yousef".equals(
                        module.resolve("message")
                ),
                "String concatenation failed"
        );

        require(
                module.resolve("decimal_total")
                        instanceof Number,
                "Decimal multiplication did not produce a number"
        );

        double decimalTotal =
                ((Number) module.resolve(
                        "decimal_total"
                )).doubleValue();

        require(
                Math.abs(decimalTotal - 10.0)
                        < 0.000001,
                "Decimal multiplication was incorrect"
        );

        System.out.println(
                "Python arithmetic execution passed."
        );

        System.out.println(
                "Calculated total: "
                        + module.resolve("calculated_total")
        );

        System.out.println(
                "Next ID: "
                        + module.resolve("next_id")
        );

        System.out.println(
                "Message: "
                        + module.resolve("message")
        );

        System.out.println(
                "Decimal total: "
                        + module.resolve("decimal_total")
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