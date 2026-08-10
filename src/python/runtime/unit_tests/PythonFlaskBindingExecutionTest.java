package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonInterpreter;
import python.runtime.flask.FlaskApplication;
import python.runtime.flask.FlaskRuntimeBindings;

import java.nio.file.Path;
import java.util.Map;

public class PythonFlaskBindingExecutionTest {
    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "python",
                        "flask_constructor_values.py"
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
        module.defineLocal(
                "__name__",
                "compiler_input_app"
        );

        FlaskRuntimeBindings flaskBindings =
                new FlaskRuntimeBindings();

        flaskBindings.installInto(module);

        interpreter.executeModule(
                program,
                module
        );
        Object appValue =
                module.resolve("app");

        require(
                appValue instanceof FlaskApplication,
                "Flask() did not create a FlaskApplication"
        );

        FlaskApplication application =
                (FlaskApplication) appValue;

        require(
                "compiler_input_app".equals(
                        application.importName()
                ),
                "Flask application received the wrong module name"
        );

        require(
                "compiler_input_app".equals(
                        module.resolve(
                                "captured_module_name"
                        )
                ),
                "__name__ binding failed"
        );

        System.out.println(
                "Flask constructor execution passed."
        );

        System.out.println(
                "Application: "
                        + application
        );

        System.out.println(
                "Module name: "
                        + module.resolve(
                        "captured_module_name"
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
