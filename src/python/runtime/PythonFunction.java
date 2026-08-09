package python.runtime;

import python.models.funcdef.FunctionDef;

import java.util.Objects;

public record PythonFunction(
        FunctionDef definition,
        PythonEnvironment definingEnvironment,
        PythonInterpreter interpreter
) implements PythonCallable {

    public PythonFunction {
        Objects.requireNonNull(
                definition,
                "Python function definition cannot be null"
        );

        Objects.requireNonNull(
                definition.id,
                "Python function name cannot be null"
        );

        Objects.requireNonNull(definingEnvironment);
        Objects.requireNonNull(interpreter);
    }

    public String name() {
        return definition.id.name;
    }

    @Override
    public Object call(
            PythonCallArguments arguments
    ) {
        return interpreter.invokeFunction(
                this,
                arguments
        );
    }
}