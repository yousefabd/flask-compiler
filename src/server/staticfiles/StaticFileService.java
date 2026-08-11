package server.staticfiles;

import python.runtime.flask.FlaskRuntimeDefaults;
import server.http.ServerResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class StaticFileService {

    private final Path staticRoot;

    public StaticFileService(
            Path staticRoot
    ) {
        this.staticRoot =
                Objects.requireNonNull(staticRoot)
                        .toAbsolutePath()
                        .normalize();
    }

    public Optional<ServerResponse> tryServe(
            String method,
            String requestPath
    ) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(requestPath);

        /*
         * This service only handles URLs beginning with
         * Flask's configured static URL prefix.
         */
        if (!requestPath.startsWith(
                FlaskRuntimeDefaults.STATIC_URL_PREFIX
        )) {
            return Optional.empty();
        }

        if (!method.equalsIgnoreCase("GET")) {
            return Optional.of(
                    ServerResponse.text(
                            405,
                            "Method Not Allowed"
                    )
            );
        }

        /*
         * Example:
         *
         * requestPath = "/static/styles.css"
         * relativeName = "styles.css"
         */
        String relativeName =
                requestPath.substring(
                        FlaskRuntimeDefaults
                                .STATIC_URL_PREFIX
                                .length()
                );

        if (relativeName.isBlank()) {
            return Optional.of(notFound());
        }

        Path candidate;

        try {
            /*
             * output/static + styles.css
             * becomes output/static/styles.css
             */
            candidate =
                    staticRoot
                            .resolve(relativeName)
                            .normalize();

        } catch (InvalidPathException exception) {
            return Optional.of(notFound());
        }

        /*
         * Rejects:
         *
         * /static/../app.py
         */
        if (!candidate.startsWith(staticRoot)
                || !Files.isRegularFile(candidate)) {

            return Optional.of(notFound());
        }

        try {
            Path realRoot =
                    staticRoot.toRealPath();

            Path realFile =
                    candidate.toRealPath();

            /*
             * Prevent a symbolic link from escaping
             * output/static.
             */
            if (!realFile.startsWith(realRoot)) {
                return Optional.of(notFound());
            }

            return Optional.of(
                    new ServerResponse(
                            200,
                            Map.of(
                                    "Content-Type",
                                    contentType(realFile)
                            ),
                            Files.readAllBytes(realFile)
                    )
            );

        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Could not read static file: "
                            + candidate,
                    exception
            );
        }
    }

    private ServerResponse notFound() {
        return ServerResponse.text(
                404,
                "Not Found"
        );
    }

    private String contentType(
            Path file
    ) {
        String filename =
                file.getFileName()
                        .toString()
                        .toLowerCase(Locale.ROOT);

        int extensionIndex =
                filename.lastIndexOf('.');

        String extension =
                extensionIndex < 0
                        ? ""
                        : filename.substring(
                        extensionIndex + 1
                );

        return switch (extension) {
            case "css" ->
                    "text/css; charset=utf-8";

            case "js" ->
                    "text/javascript; charset=utf-8";

            case "html" ->
                    "text/html; charset=utf-8";

            case "json" ->
                    "application/json; charset=utf-8";

            case "png" ->
                    "image/png";

            case "jpg", "jpeg" ->
                    "image/jpeg";

            case "gif" ->
                    "image/gif";

            case "svg" ->
                    "image/svg+xml";

            case "ico" ->
                    "image/x-icon";

            default ->
                    "application/octet-stream";
        };
    }
}