package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonFunction;
import python.runtime.PythonInterpreter;

import java.nio.file.Path;

public class PythonFunctionRegistrationTest {

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

        Object greetValue =
                module.resolve("greet");

        require(
                greetValue instanceof PythonFunction,
                "greet was not registered as a Python function"
        );

        PythonFunction greet =
                (PythonFunction) greetValue;

        require(
                "greet".equals(greet.name()),
                "Registered function has the wrong name"
        );

        require(
                greet.definition().parameters.size() == 1,
                "greet should have one parameter"
        );

        require(
                module.resolve("add")
                        instanceof PythonFunction,
                "add was not registered"
        );

        require(
                !module.contains("body_was_executed"),
                "Function body executed during registration"
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