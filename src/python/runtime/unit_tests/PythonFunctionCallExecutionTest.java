package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class PythonFunctionCallExecutionTest {
    public static void main(String[] args) {
        Path source = Path.of(
                "tests",
                "python",
                "function_call_values.py"
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
                "Hello Yousef".equals(
                        module.resolve(
                                "positional_message"
                        )
                ),
                "Parsed positional call failed"
        );

        require(
                "Hello Mona".equals(
                        module.resolve(
                                "keyword_message"
                        )
                ),
                "Parsed keyword call failed"
        );

        require(
                "Hello Omar".equals(
                        module.resolve(
                                "nested_message"
                        )
                ),
                "Nested function call failed"
        );

        require(
                Integer.valueOf(42).equals(
                        module.resolve("total")
                ),
                "Function arithmetic result failed"
        );

        require(
                module.resolve("nothing") == null,
                "Passing or returning None failed"
        );

        System.out.println(
                "Python parsed function calls passed."
        );

        System.out.println(
                "Positional: "
                        + module.resolve(
                        "positional_message"
                )
        );

        System.out.println(
                "Keyword: "
                        + module.resolve(
                        "keyword_message"
                )
        );

        System.out.println(
                "Nested: "
                        + module.resolve(
                        "nested_message"
                )
        );

        System.out.println(
                "Total: "
                        + module.resolve("total")
        );

        System.out.println(
                "None result: "
                        + module.resolve("nothing")
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
