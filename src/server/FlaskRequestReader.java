package server;

import com.sun.net.httpserver.HttpExchange;
import python.runtime.flask.FlaskRequestData;
import server.http.BadRequestException;
import server.http.FormUrlEncodedParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public final class FlaskRequestReader {

    private static final String FORM_CONTENT_TYPE =
            "application/x-www-form-urlencoded";

    private static final int MAX_FORM_BODY_BYTES =
            1_048_576;

    private final FormUrlEncodedParser formParser;

    public FlaskRequestReader() {
        this(new FormUrlEncodedParser());
    }

    public FlaskRequestReader(
            FormUrlEncodedParser formParser
    ) {
        this.formParser =
                Objects.requireNonNull(formParser);
    }

    public FlaskRequestData read(
            HttpExchange exchange
    ) throws IOException {
        Objects.requireNonNull(exchange);

        String method =
                exchange.getRequestMethod();

        String path =
                exchange.getRequestURI()
                        .getPath();

        Map<String, String> form =
                readForm(exchange);

        return new FlaskRequestData(
                method,
                path,
                form
        );
    }

    private Map<String, String> readForm(
            HttpExchange exchange
    ) throws IOException {
        String contentType =
                exchange.getRequestHeaders()
                        .getFirst("Content-Type");

        if (!isFormContentType(contentType)) {
            return Map.of();
        }

        byte[] body =
                readLimitedBody(exchange);

        return formParser.parse(body);
    }

    private boolean isFormContentType(
            String contentType
    ) {
        if (contentType == null) {
            return false;
        }

        String mediaType =
                contentType
                        .split(";", 2)[0]
                        .trim();

        return mediaType.equalsIgnoreCase(
                FORM_CONTENT_TYPE
        );
    }

    private byte[] readLimitedBody(
            HttpExchange exchange
    ) throws IOException {
        try (InputStream input =
                     exchange.getRequestBody()) {

            byte[] body =
                    input.readNBytes(
                            MAX_FORM_BODY_BYTES + 1
                    );

            if (body.length
                    > MAX_FORM_BODY_BYTES) {

                throw new BadRequestException(
                        "Submitted form is too large"
                );
            }

            return body;
        }
    }
}