package python.runtime.flask;

import python.runtime.PythonAttributeContainer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import jinja2.runtime.RouteDefinition;
import python.runtime.PythonCallArguments;
import python.runtime.PythonCallable;
import python.runtime.PythonFunction;
import python.runtime.PythonNativeFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FlaskApplication implements PythonAttributeContainer {

    private final String importName;
    private final Map<String, Object> attributes =
            new LinkedHashMap<>();
    private static final Pattern ROUTE_ARGUMENT =
            Pattern.compile(
                    "<(?:[^:<>]+:)?([^<>]+)>"
            );

    private final List<FlaskRoute> routes =
            new ArrayList<>();

    private final PythonCallable routeFunction =
            new PythonNativeFunction(
                    "Flask.route",
                    this::createRouteDecorator
            );

    public FlaskApplication(String importName) {
        this.importName =
                Objects.requireNonNull(importName);
    }

    public String importName() {
        return importName;
    }

    @Override
    public String toString() {
        return "FlaskApplication{importName='"
                + importName
                + "'}";
    }

    @Override
    public Object getAttribute(
            String name,
            int sourceLine
    ) {
        Objects.requireNonNull(name);

        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }

        if ("route".equals(name)) {
            return routeFunction;
        }

        throw new IllegalStateException(
                "Flask application has no attribute '"
                        + name
                        + "' at line "
                        + sourceLine
        );
    }

    @Override
    public void setAttribute(
            String name,
            Object value,
            int sourceLine
    ) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Python attribute name cannot be blank"
                            + " at line "
                            + sourceLine
            );
        }

        attributes.put(
                name,
                value
        );
    }
    public List<FlaskRoute> routes() {
        return List.copyOf(routes);
    }

    public List<RouteDefinition> renderRoutes() {
        return routes.stream()
                .map(FlaskRoute::toRenderRoute)
                .toList();
    }

    private Object createRouteDecorator(
            PythonCallArguments arguments
    ) {
        if (arguments.positional().size() != 1) {
            throw new IllegalArgumentException(
                    "Flask.route() expects exactly one"
                            + " positional route rule"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object ruleValue =
                arguments.positional().getFirst();

        if (!(ruleValue instanceof String rule)
                || rule.isBlank()) {

            throw new IllegalArgumentException(
                    "Flask route rule must be"
                            + " a non-empty string"
            );
        }

        validateRouteKeywords(arguments);

        Set<String> methods =
                readRouteMethods(arguments);

        String explicitEndpoint =
                readExplicitEndpoint(arguments);

        return new PythonNativeFunction(
                "Flask.route.decorator",
                decoratorArguments ->
                        registerDecoratedRoute(
                                rule,
                                methods,
                                explicitEndpoint,
                                decoratorArguments
                        )
        );
    }

    private Object registerDecoratedRoute(
            String rule,
            Set<String> methods,
            String explicitEndpoint,
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()
                || arguments.positional().size() != 1
                || !(arguments.positional().getFirst()
                instanceof PythonFunction function)) {

            throw new IllegalArgumentException(
                    "Flask route decorator expects"
                            + " exactly one Python function"
            );
        }

        String endpoint =
                explicitEndpoint == null
                        ? function.name()
                        : explicitEndpoint;

        routes.add(
                new FlaskRoute(
                        endpoint,
                        rule,
                        extractRouteArguments(rule),
                        methods,
                        function
                )
        );

        // Flask decorators return the original function.
        return function;
    }

    private void validateRouteKeywords(
            PythonCallArguments arguments
    ) {
        for (String name
                : arguments.keywords().keySet()) {

            if (!name.equals("methods")
                    && !name.equals("endpoint")) {

                throw new UnsupportedOperationException(
                        "Flask.route() keyword '"
                                + name
                                + "' is not supported yet"
                                + " at line "
                                + arguments.sourceLine()
                );
            }
        }
    }

    private Set<String> readRouteMethods(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords()
                .containsKey("methods")) {

            return Set.of("GET");
        }

        Object value =
                arguments.keywords().get("methods");

        if (!(value instanceof Iterable<?> iterable)) {
            throw new IllegalArgumentException(
                    "Flask route methods must be iterable"
            );
        }

        Set<String> methods =
                new LinkedHashSet<>();

        for (Object item : iterable) {
            if (!(item instanceof String method)
                    || method.isBlank()) {

                throw new IllegalArgumentException(
                        "Flask route method must be"
                                + " a non-empty string"
                );
            }

            methods.add(
                    method.toUpperCase(Locale.ROOT)
            );
        }

        if (methods.isEmpty()) {
            throw new IllegalArgumentException(
                    "Flask route methods cannot be empty"
            );
        }

        return methods;
    }

    private String readExplicitEndpoint(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords()
                .containsKey("endpoint")) {

            return null;
        }

        Object value =
                arguments.keywords().get("endpoint");

        if (!(value instanceof String endpoint)
                || endpoint.isBlank()) {

            throw new IllegalArgumentException(
                    "Flask route endpoint must be"
                            + " a non-empty string"
            );
        }

        return endpoint;
    }

    private List<String> extractRouteArguments(
            String rule
    ) {
        List<String> arguments =
                new ArrayList<>();

        Matcher matcher =
                ROUTE_ARGUMENT.matcher(rule);

        while (matcher.find()) {
            arguments.add(
                    matcher.group(1)
            );
        }

        return arguments;
    }
    public Object secretKey() {
        return attributes.get("secret_key");
    }
}