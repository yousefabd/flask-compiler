package jinja2.filters;

import java.util.Objects;

public record JinjaFilterDefinition(
        String name,
        int minimumArguments,
        int maximumArguments,
        JinjaFilter implementation
) {
    public JinjaFilterDefinition {
        Objects.requireNonNull(name);
        Objects.requireNonNull(implementation);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Filter name cannot be blank"
            );
        }

        if (minimumArguments < 0
                || maximumArguments < minimumArguments) {

            throw new IllegalArgumentException(
                    "Invalid argument range for filter '"
                            + name
                            + "'"
            );
        }
    }

    public boolean acceptsArgumentCount(int count) {
        return count >= minimumArguments
                && count <= maximumArguments;
    }
}