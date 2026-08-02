package jinja2.functions;

import java.util.Objects;

public record JinjaFunctionDefinition(
        String name,
        int minimumArguments,
        int maximumArguments,
        boolean acceptsKeywordArguments,
        JinjaFunction implementation
) {
    public JinjaFunctionDefinition {
        Objects.requireNonNull(name);
        Objects.requireNonNull(implementation);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Function name cannot be blank"
            );
        }

        if (minimumArguments < 0
                || maximumArguments < minimumArguments) {

            throw new IllegalArgumentException(
                    "Invalid argument range for function '"
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