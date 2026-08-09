package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class PythonFunctionInvocationTest {

    public static void main(String[] args) {
        Path source = Path.of(
                "tests",
                "python",
                "function_registration.py"
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

        PythonFunction greet =
                (PythonFunction) module.resolve("greet");

        Object positionalResult =
                greet.call(
                        new PythonCallArguments(
                                List.of("Yousef"),
                                Map.of(),
                                0
                        )
                );

        require(
                "Hello Yousef".equals(positionalResult),
                "Positional function call failed"
        );

        Object keywordResult =
                greet.call(
                        new PythonCallArguments(
                                List.of(),
                                Map.of("name", "Mona"),
                                0
                        )
                );

        require(
                "Hello Mona".equals(keywordResult),
                "Keyword function call failed"
        );

        PythonFunction noResult =
                (PythonFunction) module.resolve(
                        "no_result"
                );

        Object implicitResult =
                noResult.call(
                        new PythonCallArguments(
                                List.of("temporary"),
                                Map.of(),
                                0
                        )
                );

        require(
                implicitResult == null,
                "Function without return should produce None"
        );

        require(
                !module.contains("name"),
                "Function parameter leaked into module scope"
        );

        require(
                !module.contains("copied"),
                "Function-local assignment leaked into module scope"
        );

        System.out.println(
                "Python function invocation passed."
        );

        System.out.println(
                "Positional result: "
                        + positionalResult
        );

        System.out.println(
                "Keyword result: "
                        + keywordResult
        );

        System.out.println(
                "Implicit result: "
                        + implicitResult
        );

        System.out.println(
                "Local value leaked: "
                        + module.contains("copied")
        );

        System.out.println(
                "Python function registration passed."
        );

        System.out.println(
                "Registered function: "
                        + greet.name()
        );

        System.out.println(
                "Parameters: "
                        + greet.definition()
                        .parameters
                        .size()
        );

        System.out.println(
                "Body executed: "
                        + module.contains(
                        "body_was_executed"
                )
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
