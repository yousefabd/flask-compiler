package python.runtime.flask;

import python.runtime.PythonAttributeContainer;

import java.util.Objects;

public final class FlaskRequestProxy
        implements PythonAttributeContainer {

    private FlaskRequestData currentRequest;

    void bind(
            FlaskRequestData request
    ) {
        Objects.requireNonNull(request);

        if (currentRequest != null) {
            throw new IllegalStateException(
                    "A Flask request is already active"
            );
        }

        currentRequest = request;
    }

    void clear() {
        currentRequest = null;
    }

    @Override
    public Object getAttribute(
            String name,
            int sourceLine
    ) {
        FlaskRequestData request =
                requireCurrentRequest(
                        sourceLine
                );

        return switch (name) {
            case "method" ->
                    request.method();

            case "path" ->
                    request.path();

            case "form" ->
                    request.form();

            default ->
                    throw new IllegalStateException(
                            "Flask request has no attribute '"
                                    + name
                                    + "' at line "
                                    + sourceLine
                    );
        };
    }

    @Override
    public void setAttribute(
            String name,
            Object value,
            int sourceLine
    ) {
        throw new IllegalStateException(
                "Flask request attributes are read-only"
                        + " at line "
                        + sourceLine
        );
    }

    private FlaskRequestData requireCurrentRequest(
            int sourceLine
    ) {
        if (currentRequest == null) {
            throw new IllegalStateException(
                    "Working outside of a request context"
                            + " at line "
                            + sourceLine
            );
        }

        return currentRequest;
    }
}