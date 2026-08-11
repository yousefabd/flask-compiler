package server.http;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record ServerResponse(
        int statusCode,
        Map<String, String> headers,
        byte[] body
) {
    public ServerResponse {
        if (statusCode < 100 || statusCode > 599) {
            throw new IllegalArgumentException(
                    "Invalid HTTP status code: "
                            + statusCode
            );
        }

        Objects.requireNonNull(headers);
        Objects.requireNonNull(body);

        headers =
                Collections.unmodifiableMap(
                        new LinkedHashMap<>(headers)
                );

        body =
                body.clone();
    }

    @Override
    public byte[] body() {
        return body.clone();
    }

    public String bodyAsText() {
        return new String(
                body,
                StandardCharsets.UTF_8
        );
    }

    public static ServerResponse html(
            String html
    ) {
        Objects.requireNonNull(html);

        return new ServerResponse(
                200,
                Map.of(
                        "Content-Type",
                        "text/html; charset=utf-8"
                ),
                html.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static ServerResponse text(
            int statusCode,
            String text
    ) {
        Objects.requireNonNull(text);

        return new ServerResponse(
                statusCode,
                Map.of(
                        "Content-Type",
                        "text/plain; charset=utf-8"
                ),
                text.getBytes(StandardCharsets.UTF_8)
        );
    }
}