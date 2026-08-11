package server;

import compiler.generation.HtmlFileGenerator;
import compiler.generation.TemplateRenderRequest;
import compiler.runtime.CompiledApplication;
import errors.CodeGenError;
import python.runtime.flask.FlaskRouteMatch;
import python.runtime.flask.FlaskRouteMatcher;
import server.http.ServerResponse;
import server.staticfiles.StaticFileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ApplicationRequestDispatcher {

    private final CompiledApplication application;
    private final FlaskRouteMatcher routeMatcher;
    private final HtmlFileGenerator htmlFileGenerator;
    private final StaticFileService staticFileService;

    public ApplicationRequestDispatcher(
            CompiledApplication application,
            HtmlFileGenerator htmlFileGenerator,
            StaticFileService staticFileService
    ) {
        this(
                application,
                new FlaskRouteMatcher(),
                htmlFileGenerator,
                staticFileService
        );
    }

    public ApplicationRequestDispatcher(
            CompiledApplication application,
            FlaskRouteMatcher routeMatcher,
            HtmlFileGenerator htmlFileGenerator,
            StaticFileService staticFileService
    ) {
        this.application =
                Objects.requireNonNull(application);

        this.routeMatcher =
                Objects.requireNonNull(routeMatcher);

        this.htmlFileGenerator =
                Objects.requireNonNull(
                        htmlFileGenerator
                );

        this.staticFileService =
                Objects.requireNonNull(
                        staticFileService
                );
    }
    public ServerResponse dispatch(
            String method,
            String path
    ) {
        Optional<ServerResponse> staticResponse =
                staticFileService.tryServe(
                        method,
                        path
                );

        if (staticResponse.isPresent()) {
            return staticResponse.orElseThrow();
        }
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

            Path generatedFile =
                    htmlFileGenerator.generate(
                            application,
                            request
                    );

            return generatedHtmlResponse(
                    generatedFile
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
    private ServerResponse generatedHtmlResponse(
            Path generatedFile
    ) {
        try {
            byte[] generatedHtml =
                    Files.readAllBytes(
                            generatedFile
                    );

            return new ServerResponse(
                    200,
                    Map.of(
                            "Content-Type",
                            "text/html; charset=utf-8"
                    ),
                    generatedHtml
            );

        } catch (IOException exception) {
            throw new CodeGenError(
                    generatedFile.toString(),
                    "Could not read generated HTML file",
                    exception
            );
        }
    }
}