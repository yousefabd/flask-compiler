package jinja2.tests;

import java.util.Objects;

public record JinjaTestDefinition(
        String name,
        int minimumArguments,
        int maximumArguments,
        boolean acceptsUndefined,
        JinjaTest implementation
) {
    public JinjaTestDefinition {
        Objects.requireNonNull(name);
        Objects.requireNonNull(implementation);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Test name cannot be blank"
            );
        }

        if (minimumArguments < 0
                || maximumArguments < minimumArguments) {
            throw new IllegalArgumentException(
                    "Invalid argument range for test '" + name + "'"
            );
        }
    }

    public boolean acceptsArgumentCount(int count) {
        return count >= minimumArguments
                && count <= maximumArguments;
    }
}