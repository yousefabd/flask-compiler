package jinja2.semantic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Describes one statically named include and the Jinja locals
 * visible exactly where that include occurs.
 */
public record JinjaIncludeSite(
        String templateName,
        int line,
        Set<String> visibleLocals
) {
    public JinjaIncludeSite {
        Objects.requireNonNull(templateName);
        Objects.requireNonNull(visibleLocals);

        if (templateName.isBlank()) {
            throw new IllegalArgumentException(
                    "Included template name cannot be blank"
            );
        }

        visibleLocals =
                Collections.unmodifiableSet(
                        new LinkedHashSet<>(visibleLocals)
                );
    }
}