package python.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PythonBuiltinRegistry {

    private final Map<String, PythonCallable> builtins =
            new LinkedHashMap<>();

    public PythonBuiltinRegistry() {
        register(
                "float",
                new PythonNativeFunction(
                        "float",
                        this::evaluateFloat
                )
        );
    }

    public void installInto(
            PythonEnvironment environment
    ) {
        Objects.requireNonNull(environment);

        for (Map.Entry<String, PythonCallable> entry
                : builtins.entrySet()) {

            environment.defineLocal(
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    private void register(
            String name,
            PythonCallable callable
    ) {
        if (builtins.containsKey(name)) {
            throw new IllegalStateException(
                    "Python builtin '"
                            + name
                            + "' is already registered"
            );
        }

        builtins.put(
                name,
                Objects.requireNonNull(callable)
        );
    }

    private Object evaluateFloat(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()) {
            throw new IllegalArgumentException(
                    "float() does not accept keyword arguments"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        if (arguments.positional().isEmpty()) {
            return 0.0;
        }

        if (arguments.positional().size() != 1) {
            throw new IllegalArgumentException(
                    "float() expects at most one argument"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object value =
                arguments.positional().getFirst();

        if (value instanceof Boolean bool) {
            return bool ? 1.0 : 0.0;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        if (value instanceof String text) {
            try {
                return Double.parseDouble(
                        text.trim()
                );
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(
                        "Could not convert string to float: '"
                                + text
                                + "' at line "
                                + arguments.sourceLine(),
                        exception
                );
            }
        }

        throw new IllegalArgumentException(
                "float() cannot convert Python value of type "
                        + runtimeTypeName(value)
                        + " at line "
                        + arguments.sourceLine()
        );
    }

    private String runtimeTypeName(Object value) {
        return value == null
                ? "None"
                : value.getClass().getSimpleName();
    }
}