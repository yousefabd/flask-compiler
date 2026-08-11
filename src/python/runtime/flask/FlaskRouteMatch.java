package python.runtime.flask;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record FlaskRouteMatch(
        FlaskRoute route,
        Map<String, Object> arguments
) {
    public FlaskRouteMatch {
        Objects.requireNonNull(route);
        Objects.requireNonNull(arguments);

        arguments =
                Collections.unmodifiableMap(
                        new LinkedHashMap<>(arguments)
                );
    }
}