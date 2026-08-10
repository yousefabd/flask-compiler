package python.runtime.flask;

import python.runtime.PythonAttributeContainer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class FlaskApplication implements PythonAttributeContainer {

    private final String importName;
    private final Map<String, Object> attributes =
            new LinkedHashMap<>();

    public FlaskApplication(String importName) {
        this.importName =
                Objects.requireNonNull(importName);
    }

    public String importName() {
        return importName;
    }

    @Override
    public String toString() {
        return "FlaskApplication{importName='"
                + importName
                + "'}";
    }

    @Override
    public Object getAttribute(
            String name,
            int sourceLine
    ) {
        Objects.requireNonNull(name);

        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }

        throw new IllegalStateException(
                "Flask application has no attribute '"
                        + name
                        + "' at line "
                        + sourceLine
        );
    }

    @Override
    public void setAttribute(
            String name,
            Object value,
            int sourceLine
    ) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Python attribute name cannot be blank"
                            + " at line "
                            + sourceLine
            );
        }

        attributes.put(
                name,
                value
        );
    }

    public Object secretKey() {
        return attributes.get("secret_key");
    }
}