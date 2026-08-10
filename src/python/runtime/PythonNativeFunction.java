package python.runtime;

import java.util.Objects;
import java.util.function.Function;

public record PythonNativeFunction(
        String name,
        Function<PythonCallArguments, Object> implementation
) implements PythonCallable {

    public PythonNativeFunction {
        Objects.requireNonNull(name);
        Objects.requireNonNull(implementation);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Native Python function name cannot be blank"
            );
        }
    }

    @Override
    public Object call(
            PythonCallArguments arguments
    ) {
        Objects.requireNonNull(arguments);
        return implementation.apply(arguments);
    }
}