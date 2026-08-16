package jinja2.semantic;

import java.util.Objects;

/** One classified name occurrence in a Jinja template. */
public record JinjaNameUse(
        String name,
        int line,
        Kind kind
) {
    public enum Kind {
        BUILTIN,
        TEMPLATE_LOCAL,
        PROPERTY_NAME,
        EXTERNAL,
        DEFINITION_GUARD
    }

    public JinjaNameUse {
        Objects.requireNonNull(name);
        Objects.requireNonNull(kind);
    }
}
