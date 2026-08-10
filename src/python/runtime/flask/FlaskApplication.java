package python.runtime.flask;

import java.util.Objects;

public final class FlaskApplication {

    private final String importName;

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
}