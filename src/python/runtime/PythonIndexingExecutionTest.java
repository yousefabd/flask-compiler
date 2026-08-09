package python.runtime;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PythonIndexingExecutionTest {

    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "indexing_values.py"
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

        Map<String, Object> form =
                new LinkedHashMap<>();

        form.put("name", "Keyboard");

        Map<String, Object> request =
                new LinkedHashMap<>();

        request.put("method", "POST");
        request.put("form", form);

        /*
         * The Java server will eventually create this object
         * for every incoming request.
         */
        module.assign("request", request);

        PythonInterpreter interpreter =
                new PythonInterpreter(
                        new PythonExpressionEvaluator()
                );

        interpreter.executeModule(
                program,
                module
        );

        require(
                "Laptop".equals(
                        module.resolve("first_name")
                ),
                "First product indexing failed"
        );

        require(
                "Phone".equals(
                        module.resolve("last_name")
                ),
                "Negative indexing failed"
        );

        require(
                "POST".equals(
                        module.resolve("method")
                ),
                "Attribute access failed"
        );

        require(
                "Keyboard".equals(
                        module.resolve("form_name")
                ),
                "Nested attribute/index access failed"
        );

        System.out.println(
                "Python indexing execution passed."
        );

        System.out.println(
                "First: "
                        + module.resolve("first_name")
        );

        System.out.println(
                "Last: "
                        + module.resolve("last_name")
        );

        System.out.println(
                "Request: "
                        + module.resolve("method")
                        + " / "
                        + module.resolve("form_name")
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