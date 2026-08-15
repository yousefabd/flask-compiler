package jinja2.functions;

import jinja2.runtime.RenderEnvironment;
import jinja2.runtime.RouteUrlBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class UrlForFunction
        implements JinjaFunction {

    private static final String ENDPOINT =
            "endpoint";

    private final RouteUrlBuilder routeUrlBuilder;

    public UrlForFunction() {
        this(new RouteUrlBuilder());
    }

    public UrlForFunction(
            RouteUrlBuilder routeUrlBuilder
    ) {
        this.routeUrlBuilder =
                Objects.requireNonNull(routeUrlBuilder);
    }

    @Override
    public Object invoke(
            JinjaCallArguments arguments,
            RenderEnvironment environment
    ) {
        String endpoint =
                readEndpoint(arguments);

        Map<String, Object> values =
                new LinkedHashMap<>(
                        arguments.keyword()
                );

        values.remove(ENDPOINT);

        return routeUrlBuilder.build(
                endpoint,
                values,
                environment.routes()
        );
    }

    private String readEndpoint(
            JinjaCallArguments arguments
    ) {
        if (arguments.positional().size() > 1) {
            throw new IllegalArgumentException(
                    "url_for() accepts only one positional argument"
            );
        }

        boolean hasPositionalEndpoint =
                !arguments.positional().isEmpty();

        boolean hasKeywordEndpoint =
                arguments.keyword()
                        .containsKey(ENDPOINT);

        if (hasPositionalEndpoint
                && hasKeywordEndpoint) {

            throw new IllegalArgumentException(
                    "url_for() endpoint was provided more than once"
            );
        }

        Object endpointValue;

        if (hasPositionalEndpoint) {
            endpointValue =
                    arguments.positional()
                            .getFirst();

        } else if (hasKeywordEndpoint) {
            endpointValue =
                    arguments.keyword()
                            .get(ENDPOINT);

        } else {
            throw new IllegalArgumentException(
                    "url_for() requires an endpoint"
            );
        }

        if (!(endpointValue instanceof String endpoint)
                || endpoint.isBlank()) {

            throw new IllegalArgumentException(
                    "url_for() endpoint must be a non-empty string"
            );
        }

        return endpoint;
    }
}