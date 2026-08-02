package jinja2.runtime;

import java.util.List;
import java.util.Objects;

public record RouteDefinition(
        String endpoint,
        String rule,
        List<String> arguments
) {
    public RouteDefinition {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(rule);
        Objects.requireNonNull(arguments);

        arguments = List.copyOf(arguments);
    }
}