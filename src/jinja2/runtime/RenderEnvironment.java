package jinja2.runtime;

import java.util.List;
import java.util.Objects;

public record RenderEnvironment(
        List<FlashMessage> flashedMessages,
        List<RouteDefinition> routes
) {
    public RenderEnvironment {
        Objects.requireNonNull(flashedMessages);
        Objects.requireNonNull(routes);

        flashedMessages = List.copyOf(flashedMessages);
        routes = List.copyOf(routes);
    }

    public static RenderEnvironment empty() {
        return new RenderEnvironment(
                List.of(),
                List.of()
        );
    }
}