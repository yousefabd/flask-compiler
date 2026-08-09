package python.runtime;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class PythonModuleExecutionTest {

    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "module_values.py"
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

        /*
         * Semantic analysis happens before interpretation.
         */
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
                "Yousef".equals(
                        module.resolve("name")
                ),
                "Name assignment failed"
        );

        require(
                Integer.valueOf(24).equals(
                        module.resolve("age")
                ),
                "Age assignment failed"
        );

        require(
                module.resolve("nothing") == null,
                "None assignment failed"
        );

        require(
                module.resolve("products")
                        instanceof List<?>,
                "Products did not become a Java List"
        );

        require(
                module.resolve("profile")
                        instanceof Map<?, ?>,
                "Profile did not become a Java Map"
        );

        System.out.println(
                "Python module execution passed."
        );

        System.out.println(
                "Name: "
                        + module.resolve("name")
        );

        System.out.println(
                "Products: "
                        + module.resolve("products")
        );

        System.out.println(
                "Profile: "
                        + module.resolve("profile")
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