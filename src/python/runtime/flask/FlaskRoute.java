package python.runtime.flask;

import jinja2.runtime.RouteDefinition;
import python.runtime.PythonFunction;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record FlaskRoute(
        String endpoint,
        String rule,
        List<String> arguments,
        Set<String> methods,
        PythonFunction handler
) {
    public FlaskRoute {
        Objects.requireNonNull(endpoint);
        Objects.requireNonNull(rule);
        Objects.requireNonNull(arguments);
        Objects.requireNonNull(methods);
        Objects.requireNonNull(handler);

        arguments = List.copyOf(arguments);

        methods = Collections.unmodifiableSet(
                new LinkedHashSet<>(methods)
        );
    }

    public RouteDefinition toRenderRoute() {
        return new RouteDefinition(
                endpoint,
                rule,
                arguments
        );
    }
}