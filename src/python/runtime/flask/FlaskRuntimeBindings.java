package python.runtime.flask;

import compiler.generation.TemplateRenderRequest;
import jinja2.runtime.FlashMessage;
import jinja2.runtime.RenderEnvironment;
import python.runtime.PythonCallArguments;
import python.runtime.PythonCallable;
import python.runtime.PythonEnvironment;
import python.runtime.PythonNativeFunction;
import jinja2.runtime.RouteDefinition;
import jinja2.runtime.RouteUrlBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class FlaskRuntimeBindings {

    private final Supplier<RenderEnvironment>
            renderEnvironmentSupplier;
    private final FlaskRequestProxy requestProxy =
            new FlaskRequestProxy();

    private final PythonCallable flaskConstructor;

    private FlaskApplication currentApplication;
    private static final String ENDPOINT =
            "endpoint";

    private final RouteUrlBuilder routeUrlBuilder;
    private static final String MESSAGE =
            "message";

    private static final String CATEGORY =
            "category";
    private final FlaskFlashStore flashStore;


    private final PythonCallable renderTemplateFunction;
    private final PythonCallable urlForFunction;
    private final PythonCallable redirectFunction;
    private final PythonCallable flashFunction;

    public FlaskRuntimeBindings() {
        this(RenderEnvironment::empty);
    }

    public FlaskRuntimeBindings(
            Supplier<RenderEnvironment>
                    renderEnvironmentSupplier
    ) {
        this.renderEnvironmentSupplier =
                Objects.requireNonNull(
                        renderEnvironmentSupplier
                );

        this.flaskConstructor =
                new PythonNativeFunction(
                        "Flask",
                        this::createFlaskApplication
                );

        this.routeUrlBuilder =
                new RouteUrlBuilder();

        this.renderTemplateFunction =
                new PythonNativeFunction(
                        "render_template",
                        this::renderTemplate
                );
        this.urlForFunction =
                new PythonNativeFunction(
                        "url_for",
                        this::urlFor
                );
        this.redirectFunction =
                new PythonNativeFunction(
                        "redirect",
                        this::redirect
                );
        this.flashStore =
                new FlaskFlashStore();

        this.flashFunction =
                new PythonNativeFunction(
                        "flash",
                        this::flash
                );
    }

    public void installInto(
            PythonEnvironment module
    ) {
        Objects.requireNonNull(module);

        module.defineLocal(
                "Flask",
                flaskConstructor
        );

        module.defineLocal(
                "request",
                requestProxy
        );

        module.defineLocal(
                "render_template",
                renderTemplateFunction
        );
        module.defineLocal(
                "url_for",
                urlForFunction
        );
        module.defineLocal(
                "redirect",
                redirectFunction
        );
        module.defineLocal(
                "flash",
                flashFunction
        );
    }

    private Object createFlaskApplication(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()
                || arguments.positional().size() != 1) {

            throw new IllegalArgumentException(
                    "Flask() expects exactly one"
                            + " positional argument"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object importName =
                arguments.positional().getFirst();

        if (!(importName instanceof String name)) {
            throw new IllegalArgumentException(
                    "Flask() import name must be a string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        FlaskApplication application =
                new FlaskApplication(name);

        currentApplication =
                application;

        return application;
    }
    private Object renderTemplate(
            PythonCallArguments arguments
    ) {
        if (arguments.positional().size() != 1) {
            throw new IllegalArgumentException(
                    "render_template() expects exactly"
                            + " one positional template name"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object templateValue =
                arguments.positional().getFirst();

        if (!(templateValue instanceof String templateName)
                || templateName.isBlank()) {

            throw new IllegalArgumentException(
                    "render_template() template name"
                            + " must be a non-empty string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }


        RenderEnvironment renderEnvironment =
                createRenderEnvironment();

        return new TemplateRenderRequest(
                templateName,
                arguments.keywords(),
                renderEnvironment
        );
    }
    private RenderEnvironment createRenderEnvironment() {
        RenderEnvironment suppliedEnvironment =
                suppliedRenderEnvironment();

        List<FlashMessage> flashedMessages =
                new ArrayList<>(
                        suppliedEnvironment.flashedMessages()
                );

        flashedMessages.addAll(
                flashStore.consume()
        );

        return new RenderEnvironment(
                flashedMessages,
                combineRoutes(
                        suppliedEnvironment.routes()
                )
        );
    }

    private List<RouteDefinition> currentRoutes() {
        RenderEnvironment suppliedEnvironment =
                suppliedRenderEnvironment();

        return combineRoutes(
                suppliedEnvironment.routes()
        );
    }

    private RenderEnvironment suppliedRenderEnvironment() {
        return Objects.requireNonNull(
                renderEnvironmentSupplier.get(),
                "Render environment supplier returned null"
        );
    }

    private List<RouteDefinition> combineRoutes(
            List<RouteDefinition> suppliedRoutes
    ) {
        List<RouteDefinition> combinedRoutes =
                new ArrayList<>(suppliedRoutes);

        if (currentApplication != null) {
            combinedRoutes.addAll(
                    currentApplication.renderRoutes()
            );
        }

        return List.copyOf(combinedRoutes);
    }
    public FlaskApplication currentApplication() {
        if (currentApplication == null) {
            throw new IllegalStateException(
                    "Python code did not create a Flask application"
            );
        }

        return currentApplication;
    }
    private Object urlFor(
            PythonCallArguments arguments
    ) {
        String endpoint =
                readUrlEndpoint(arguments);

        Map<String, Object> values =
                new LinkedHashMap<>(
                        arguments.keywords()
                );

        values.remove(ENDPOINT);

        return routeUrlBuilder.build(
                endpoint,
                values,
                currentRoutes()
        );
    }

    private String readUrlEndpoint(
            PythonCallArguments arguments
    ) {
        if (arguments.positional().size() > 1) {
            throw new IllegalArgumentException(
                    "url_for() accepts only one positional argument"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        boolean hasPositionalEndpoint =
                !arguments.positional().isEmpty();

        boolean hasKeywordEndpoint =
                arguments.keywords()
                        .containsKey(ENDPOINT);

        if (hasPositionalEndpoint
                && hasKeywordEndpoint) {

            throw new IllegalArgumentException(
                    "url_for() endpoint was provided more than once"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object endpointValue;

        if (hasPositionalEndpoint) {
            endpointValue =
                    arguments.positional()
                            .getFirst();

        } else if (hasKeywordEndpoint) {
            endpointValue =
                    arguments.keywords()
                            .get(ENDPOINT);

        } else {
            throw new IllegalArgumentException(
                    "url_for() requires an endpoint"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        if (!(endpointValue instanceof String endpoint)
                || endpoint.isBlank()) {

            throw new IllegalArgumentException(
                    "url_for() endpoint must be a non-empty string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        return endpoint;
    }
    public void beginRequest(
            FlaskRequestData request
    ) {
        requestProxy.bind(request);
    }

    public void endRequest() {
        requestProxy.clear();
    }

    private Object redirect(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()
                || arguments.positional().size() != 1) {

            throw new IllegalArgumentException(
                    "redirect() expects exactly one"
                            + " positional location"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object locationValue =
                arguments.positional()
                        .getFirst();

        if (!(locationValue instanceof String location)
                || location.isBlank()) {

            throw new IllegalArgumentException(
                    "redirect() location must be"
                            + " a non-empty string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        return FlaskRedirectResponse.temporary(
                location
        );
    }
    private Object flash(
            PythonCallArguments arguments
    ) {
        validateFlashKeywordNames(arguments);

        if (arguments.positional().size() > 2) {
            throw new IllegalArgumentException(
                    "flash() accepts at most two"
                            + " positional arguments"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        boolean hasPositionalMessage =
                !arguments.positional().isEmpty();

        boolean hasKeywordMessage =
                arguments.keywords()
                        .containsKey(MESSAGE);

        if (hasPositionalMessage
                && hasKeywordMessage) {

            throw new IllegalArgumentException(
                    "flash() message was provided more than once"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        if (!hasPositionalMessage
                && !hasKeywordMessage) {

            throw new IllegalArgumentException(
                    "flash() requires a message"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object message =
                hasPositionalMessage
                        ? arguments.positional()
                        .getFirst()
                        : arguments.keywords()
                        .get(MESSAGE);

        boolean hasPositionalCategory =
                arguments.positional().size() == 2;

        boolean hasKeywordCategory =
                arguments.keywords()
                        .containsKey(CATEGORY);

        if (hasPositionalCategory
                && hasKeywordCategory) {

            throw new IllegalArgumentException(
                    "flash() category was provided more than once"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object categoryValue;

        if (hasPositionalCategory) {
            categoryValue =
                    arguments.positional()
                            .get(1);

        } else if (hasKeywordCategory) {
            categoryValue =
                    arguments.keywords()
                            .get(CATEGORY);

        } else {
            categoryValue = "message";
        }

        if (!(categoryValue instanceof String category)
                || category.isBlank()) {

            throw new IllegalArgumentException(
                    "flash() category must be"
                            + " a non-empty string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        flashStore.add(
                message,
                category
        );

        // Python None
        return null;
    }

    private void validateFlashKeywordNames(
            PythonCallArguments arguments
    ) {
        for (String keyword :
                arguments.keywords().keySet()) {

            if (!keyword.equals(MESSAGE)
                    && !keyword.equals(CATEGORY)) {

                throw new IllegalArgumentException(
                        "flash() received unknown keyword '"
                                + keyword
                                + "' at line "
                                + arguments.sourceLine()
                );
            }
        }
    }
}