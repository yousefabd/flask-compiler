package server;

import compiler.generation.TemplateRenderRequest;
import compiler.runtime.CompiledApplication;
import python.runtime.flask.FlaskRouteMatch;
import python.runtime.flask.FlaskRouteMatcher;
import server.http.ServerResponse;

import java.util.Objects;
import java.util.Optional;

public final class ApplicationRequestDispatcher {

    private final CompiledApplication application;
    private final FlaskRouteMatcher routeMatcher;

    public ApplicationRequestDispatcher(
            CompiledApplication application
    ) {
        this(
                application,
                new FlaskRouteMatcher()
        );
    }

    public ApplicationRequestDispatcher(
            CompiledApplication application,
            FlaskRouteMatcher routeMatcher
    ) {
        this.application =
                Objects.requireNonNull(application);

        this.routeMatcher =
                Objects.requireNonNull(routeMatcher);
    }

    public ServerResponse dispatch(
            String method,
            String path
    ) {
        Optional<FlaskRouteMatch> possibleMatch =
                routeMatcher.match(
                        application.routes(),
                        method,
                        path
                );

        if (possibleMatch.isEmpty()) {
            return ServerResponse.text(
                    404,
                    "Not Found"
            );
        }

        FlaskRouteMatch match =
                possibleMatch.orElseThrow();

        Object handlerResult =
                application.invokeRoute(match);

        return convertHandlerResult(
                handlerResult
        );
    }

    private ServerResponse convertHandlerResult(
            Object handlerResult
    ) {
        if (handlerResult
                instanceof TemplateRenderRequest request) {

            String renderedHtml =
                    application.render(request);

            return ServerResponse.html(
                    renderedHtml
            );
        }

        /*
         * Flask also permits route functions to return HTML strings.
         */
        if (handlerResult instanceof String html) {
            return ServerResponse.html(html);
        }

        throw new UnsupportedOperationException(
                "Python route returned an unsupported value: "
                        + (
                        handlerResult == null
                                ? "None"
                                : handlerResult
                                .getClass()
                                .getSimpleName()
                )
        );
    }
}