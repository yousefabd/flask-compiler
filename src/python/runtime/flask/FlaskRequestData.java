package python.runtime.flask;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public record FlaskRequestData(
        String method,
        String path,
        Map<String, String> form
) {
    public FlaskRequestData {
        Objects.requireNonNull(method);
        Objects.requireNonNull(path);
        Objects.requireNonNull(form);

        if (method.isBlank()) {
            throw new IllegalArgumentException(
                    "HTTP method cannot be blank"
            );
        }

        if (path.isBlank()
                || !path.startsWith("/")) {

            throw new IllegalArgumentException(
                    "Request path must begin with '/'"
            );
        }

        method =
                method.toUpperCase(Locale.ROOT);

        form =
                Collections.unmodifiableMap(
                        new LinkedHashMap<>(form)
                );
    }
}