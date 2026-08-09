package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.atom_statement.*;
import python.models.expr_statement.IDTrailer;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;
import java.util.Map;

public class PythonComparisonExecutionTest {
    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "comparison_values.py"
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
        frontend.analyzePython(program);
        require(
                Boolean.TRUE.equals(
                        module.resolve("adult")
                ),
                "Greater-than-or-equal comparison failed"
        );

        require(
                Boolean.FALSE.equals(
                        module.resolve("too_young")
                ),
                "Less-than comparison failed"
        );

        require(
                Boolean.TRUE.equals(
                        module.resolve("same_age")
                ),
                "Equality comparison failed"
        );

        require(
                Boolean.TRUE.equals(
                        module.resolve("different_age")
                ),
                "Not-equal comparison failed"
        );

        require(
                Boolean.TRUE.equals(
                        module.resolve("matching_product")
                ),
                "Indexed equality comparison failed"
        );

        require(
                Boolean.TRUE.equals(
                        module.resolve("higher_id")
                ),
                "Indexed greater-than comparison failed"
        );

        require(
                Boolean.TRUE.equals(
                        module.resolve("empty_is_false")
                ),
                "Empty-list truthiness failed"
        );

        require(
                Boolean.FALSE.equals(
                        module.resolve("non_empty_is_false")
                ),
                "Non-empty-list truthiness failed"
        );

        require(
                "default".equals(
                        module.resolve("fallback")
                ),
                "Python or behavior failed"
        );

        require(
                "second".equals(
                        module.resolve("selected")
                ),
                "Python and behavior failed"
        );

        System.out.println(
                "Python comparison execution passed."
        );

        System.out.println(
                "Adult: "
                        + module.resolve("adult")
        );

        System.out.println(
                "Empty is false: "
                        + module.resolve("empty_is_false")
        );

        System.out.println(
                "Fallback: "
                        + module.resolve("fallback")
        );

        System.out.println(
                "Selected: "
                        + module.resolve("selected")
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
