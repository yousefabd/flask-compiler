package server.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class FormUrlEncodedParser {

    public Map<String, String> parse(
            byte[] body
    ) {
        Objects.requireNonNull(body);

        if (body.length == 0) {
            return Map.of();
        }

        String encodedForm =
                new String(
                        body,
                        StandardCharsets.UTF_8
                );

        Map<String, String> values =
                new LinkedHashMap<>();

        for (String pair :
                encodedForm.split("&", -1)) {

            if (pair.isEmpty()) {
                continue;
            }

            int equalsIndex =
                    pair.indexOf('=');

            String encodedName =
                    equalsIndex < 0
                            ? pair
                            : pair.substring(
                            0,
                            equalsIndex
                    );

            String encodedValue =
                    equalsIndex < 0
                            ? ""
                            : pair.substring(
                            equalsIndex + 1
                    );

            try {
                String name =
                        decode(encodedName);

                String value =
                        decode(encodedValue);

                values.put(
                        name,
                        value
                );

            } catch (IllegalArgumentException exception) {
                throw new BadRequestException(
                        "Malformed URL-encoded form data",
                        exception
                );
            }
        }

        return Collections.unmodifiableMap(
                values
        );
    }

    private String decode(
            String value
    ) {
        /*
         * URLDecoder handles both:
         *
         * Gaming+Mouse      -> Gaming Mouse
         * Fast+%26+accurate -> Fast & accurate
         */
        return URLDecoder.decode(
                value,
                StandardCharsets.UTF_8
        );
    }
}