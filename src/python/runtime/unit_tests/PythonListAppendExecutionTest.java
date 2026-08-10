package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;
import java.util.Map;

public class PythonListAppendExecutionTest {
    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "list_append_values.py"
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
        Object productsValue =
                module.resolve("products");

        require(
                productsValue instanceof java.util.List<?>,
                "products is not a runtime list"
        );

        java.util.List<?> products =
                (java.util.List<?>) productsValue;

        require(
                products.size() == 2,
                "Product add/remove produced the wrong size"
        );

        require(
                products.get(0) instanceof Map<?, ?>,
                "First product is not a dictionary"
        );

        require(
                products.get(1) instanceof Map<?, ?>,
                "Second product is not a dictionary"
        );

        Map<?, ?> firstProduct =
                (Map<?, ?>) products.get(0);

        Map<?, ?> secondProduct =
                (Map<?, ?>) products.get(1);

        require(
                Integer.valueOf(2).equals(
                        firstProduct.get("id")
                ),
                "Wrong first product remained"
        );

        require(
                "Phone".equals(
                        firstProduct.get("name")
                ),
                "Wrong first product name"
        );

        require(
                Integer.valueOf(3).equals(
                        secondProduct.get("id")
                ),
                "Appended product is missing"
        );

        require(
                "Keyboard".equals(
                        secondProduct.get("name")
                ),
                "Appended product has the wrong name"
        );

        require(
                module.resolve("append_result") == null,
                "list.append() should return None"
        );

        System.out.println(
                "Python list append execution passed."
        );

        System.out.println(
                "Products remaining: "
                        + products.size()
        );

        System.out.println(
                "First: "
                        + firstProduct.get("name")
        );

        System.out.println(
                "Second: "
                        + secondProduct.get("name")
        );

        System.out.println(
                "Append result: "
                        + module.resolve("append_result")
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
