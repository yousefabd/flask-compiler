package python.runtime.flask;

import python.runtime.PythonCallArguments;
import python.runtime.PythonCallable;
import python.runtime.PythonEnvironment;
import python.runtime.PythonNativeFunction;

import java.util.Objects;

public final class FlaskRuntimeBindings {

    private final PythonCallable flaskConstructor =
            new PythonNativeFunction(
                    "Flask",
                    this::createFlaskApplication
            );

    public void installInto(
            PythonEnvironment module
    ) {
        Objects.requireNonNull(module);

        /*
         * Python imports are currently ignored by the interpreter,
         * so this supplies the Java implementation of:
         *
         * from flask import Flask
         */
        module.defineLocal(
                "Flask",
                flaskConstructor
        );
    }

    private Object createFlaskApplication(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()
                || arguments.positional().size() != 1) {

            throw new IllegalArgumentException(
                    "Flask() expects exactly one"
                            + " positional argument"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object importName =
                arguments.positional().getFirst();

        if (!(importName instanceof String name)) {
            throw new IllegalArgumentException(
                    "Flask() import name must be a string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        return new FlaskApplication(name);
    }
}