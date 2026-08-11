package python.runtime.flask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FlaskRouteMatcher {

    private static final Pattern ROUTE_ARGUMENT =
            Pattern.compile(
                    "<(?:([^:<>]+):)?([^<>]+)>"
            );

    public Optional<FlaskRouteMatch> match(
            List<FlaskRoute> routes,
            String requestMethod,
            String requestPath
    ) {
        Objects.requireNonNull(routes);
        Objects.requireNonNull(requestMethod);
        Objects.requireNonNull(requestPath);

        String normalizedMethod =
                requestMethod.toUpperCase(
                        Locale.ROOT
                );

        routeLoop:
        for (FlaskRoute route : routes) {
            if (!route.methods()
                    .contains(normalizedMethod)) {

                continue;
            }

            CompiledRule compiledRule =
                    compileRule(route.rule());

            Matcher pathMatcher =
                    compiledRule.pattern()
                            .matcher(requestPath);

            if (!pathMatcher.matches()) {
                continue;
            }

            Map<String, Object> arguments =
                    new LinkedHashMap<>();

            for (int index = 0;
                 index < compiledRule.parameters().size();
                 index++) {

                RouteParameter parameter =
                        compiledRule.parameters()
                                .get(index);

                String rawValue =
                        pathMatcher.group(index + 1);

                try {
                    arguments.put(
                            parameter.name(),
                            convertValue(
                                    parameter.converter(),
                                    rawValue
                            )
                    );

                } catch (NumberFormatException exception) {
                    continue routeLoop;
                }
            }

            return Optional.of(
                    new FlaskRouteMatch(
                            route,
                            arguments
                    )
            );
        }

        return Optional.empty();
    }

    private CompiledRule compileRule(
            String rule
    ) {
        Matcher matcher =
                ROUTE_ARGUMENT.matcher(rule);

        StringBuilder regularExpression =
                new StringBuilder("^");

        List<RouteParameter> parameters =
                new ArrayList<>();

        int previousEnd = 0;

        while (matcher.find()) {
            regularExpression.append(
                    Pattern.quote(
                            rule.substring(
                                    previousEnd,
                                    matcher.start()
                            )
                    )
            );

            String converter =
                    matcher.group(1) == null
                            ? "string"
                            : matcher.group(1);

            String name =
                    matcher.group(2);

            regularExpression
                    .append('(')
                    .append(converterPattern(converter))
                    .append(')');

            parameters.add(
                    new RouteParameter(
                            name,
                            converter
                    )
            );

            previousEnd =
                    matcher.end();
        }

        regularExpression.append(
                Pattern.quote(
                        rule.substring(previousEnd)
                )
        );

        regularExpression.append('$');

        return new CompiledRule(
                Pattern.compile(
                        regularExpression.toString()
                ),
                List.copyOf(parameters)
        );
    }

    private String converterPattern(
            String converter
    ) {
        return switch (converter) {
            case "int" -> "\\d+";
            case "string" -> "[^/]+";
            case "path" -> ".+";

            default ->
                    throw new UnsupportedOperationException(
                            "Flask route converter is not supported: "
                                    + converter
                    );
        };
    }

    private Object convertValue(
            String converter,
            String rawValue
    ) {
        return switch (converter) {
            case "int" ->
                    Integer.parseInt(rawValue);

            case "string", "path" ->
                    rawValue;

            default ->
                    throw new UnsupportedOperationException(
                            "Flask route converter is not supported: "
                                    + converter
                    );
        };
    }

    private record RouteParameter(
            String name,
            String converter
    ) {
    }

    private record CompiledRule(
            Pattern pattern,
            List<RouteParameter> parameters
    ) {
    }
}