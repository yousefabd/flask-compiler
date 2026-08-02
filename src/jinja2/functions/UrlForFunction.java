package jinja2.functions;

import jinja2.runtime.RenderEnvironment;
import jinja2.runtime.RouteDefinition;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class UrlForFunction
        implements JinjaFunction {

    private static final String ENDPOINT =
            "endpoint";

    private static final String ANCHOR =
            "_anchor";

    private static final String EXTERNAL =
            "_external";

    private static final String SCHEME =
            "_scheme";

    private static final String METHOD =
            "_method";

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

        Object anchor =
                values.remove(ANCHOR);

        Object external =
                values.remove(EXTERNAL);

        Object scheme =
                values.remove(SCHEME);

        Object method =
                values.remove(METHOD);

        validateSpecialArguments(
                external,
                scheme,
                method
        );

        RouteDefinition route =
                selectRoute(
                        endpoint,
                        values,
                        environment
                );

        String url =
                buildRoute(
                        route,
                        values
                );

        url = appendQueryString(
                url,
                values
        );

        if (anchor != null) {
            url += "#"
                    + encodeComponent(
                    anchor.toString()
            );
        }

        return url;
    }

    private String readEndpoint(
            JinjaCallArguments arguments
    ) {
        if (arguments.positional().size() > 1) {
            throw new IllegalArgumentException(
                    "url_for() accepts only one positional argument"
            );
        }

        boolean positionalEndpoint =
                !arguments.positional().isEmpty();

        boolean keywordEndpoint =
                arguments.keyword()
                        .containsKey(ENDPOINT);

        if (positionalEndpoint
                && keywordEndpoint) {

            throw new IllegalArgumentException(
                    "url_for() endpoint was provided more than once"
            );
        }

        Object endpointValue;

        if (positionalEndpoint) {
            endpointValue =
                    arguments.positional()
                            .getFirst();

        } else if (keywordEndpoint) {
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

    private void validateSpecialArguments(
            Object external,
            Object scheme,
            Object method
    ) {
        if (external != null) {
            if (!(external instanceof Boolean booleanValue)) {
                throw new IllegalArgumentException(
                        "'_external' must be boolean"
                );
            }

            if (booleanValue) {
                throw new IllegalArgumentException(
                        "External URLs are not supported by the static generator"
                );
            }
        }

        if (scheme != null) {
            throw new IllegalArgumentException(
                    "'_scheme' is not supported by the static generator"
            );
        }

        if (method != null) {
            throw new IllegalArgumentException(
                    "'_method' route selection is not supported yet"
            );
        }
    }

    private RouteDefinition selectRoute(
            String endpoint,
            Map<String, Object> values,
            RenderEnvironment environment
    ) {
        List<RouteDefinition> matchingEndpoint =
                environment.routes()
                        .stream()
                        .filter(route ->
                                route.endpoint()
                                        .equals(endpoint)
                        )
                        .toList();

        if (matchingEndpoint.isEmpty()) {
            throw new IllegalArgumentException(
                    "Flask endpoint '"
                            + endpoint
                            + "' does not exist"
            );
        }

        /*
         * Prefer the most specific rule whose required path
         * arguments are all present.
         */
        return matchingEndpoint
                .stream()
                .filter(route ->
                        values.keySet()
                                .containsAll(
                                        route.arguments()
                                )
                )
                .max(
                        Comparator.comparingInt(
                                route ->
                                        route.arguments()
                                                .size()
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No route for endpoint '"
                                        + endpoint
                                        + "' matches the supplied arguments"
                        )
                );
    }

    private String buildRoute(
            RouteDefinition route,
            Map<String, Object> remainingValues
    ) {
        String result = route.rule();

        for (String argumentName :
                route.arguments()) {

            Object value =
                    remainingValues.remove(
                            argumentName
                    );

            if (value == null) {
                throw new IllegalArgumentException(
                        "Route argument '"
                                + argumentName
                                + "' cannot be none"
                );
            }

            result = replaceRouteArgument(
                    result,
                    argumentName,
                    value
            );
        }

        return result;
    }

    private String replaceRouteArgument(
            String route,
            String argumentName,
            Object value
    ) {
        Pattern pattern = Pattern.compile(
                "<(?:([^:<>]+):)?"
                        + Pattern.quote(argumentName)
                        + ">"
        );

        Matcher matcher =
                pattern.matcher(route);

        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "Route rule does not contain argument '"
                            + argumentName
                            + "'"
            );
        }

        String converter =
                matcher.group(1);

        String encodedValue =
                encodeRouteValue(
                        value.toString(),
                        converter
                );

        return matcher.replaceFirst(
                Matcher.quoteReplacement(
                        encodedValue
                )
        );
    }

    private String encodeRouteValue(
            String value,
            String converter
    ) {
        if ("path".equals(converter)) {
            return Arrays.stream(
                            value.split("/", -1)
                    )
                    .map(this::encodeComponent)
                    .collect(
                            Collectors.joining("/")
                    );
        }

        return encodeComponent(value);
    }

    private String appendQueryString(
            String url,
            Map<String, Object> remainingValues
    ) {
        List<String> queryParts =
                new ArrayList<>();

        for (Map.Entry<String, Object> entry :
                remainingValues.entrySet()) {

            addQueryValues(
                    queryParts,
                    entry.getKey(),
                    entry.getValue()
            );
        }

        if (queryParts.isEmpty()) {
            return url;
        }

        return url
                + (url.contains("?") ? "&" : "?")
                + String.join("&", queryParts);
    }

    private void addQueryValues(
            List<String> queryParts,
            String name,
            Object value
    ) {
        if (value == null) {
            return;
        }

        if (value instanceof Iterable<?> iterable) {
            for (Object item : iterable) {
                addSingleQueryValue(
                        queryParts,
                        name,
                        item
                );
            }

            return;
        }

        if (value.getClass().isArray()) {
            int length =
                    Array.getLength(value);

            for (int index = 0;
                 index < length;
                 index++) {

                addSingleQueryValue(
                        queryParts,
                        name,
                        Array.get(value, index)
                );
            }

            return;
        }

        addSingleQueryValue(
                queryParts,
                name,
                value
        );
    }

    private void addSingleQueryValue(
            List<String> queryParts,
            String name,
            Object value
    ) {
        if (value == null) {
            return;
        }

        queryParts.add(
                encodeComponent(name)
                        + "="
                        + encodeComponent(
                        value.toString()
                )
        );
    }

    private String encodeComponent(
            String value
    ) {
        return URLEncoder
                .encode(
                        value,
                        StandardCharsets.UTF_8
                )
                .replace(
                        "+",
                        "%20"
                );
    }
}