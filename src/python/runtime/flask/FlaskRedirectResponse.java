package python.runtime.flask;

import java.util.Objects;

public record FlaskRedirectResponse(
        String location,
        int statusCode
) {
    public FlaskRedirectResponse {
        Objects.requireNonNull(location);

        if (location.isBlank()) {
            throw new IllegalArgumentException(
                    "Redirect location cannot be blank"
            );
        }

        if (statusCode < 300
                || statusCode >= 400) {

            throw new IllegalArgumentException(
                    "Redirect status must be between 300 and 399"
            );
        }
    }

    public static FlaskRedirectResponse temporary(
            String location
    ) {
        return new FlaskRedirectResponse(
                location,
                302
        );
    }
}