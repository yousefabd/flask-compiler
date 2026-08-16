package jinja2.semantic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable names supplied by the Jinja/Flask rendering environment rather
 * than by a render_template context.
 */
public final class JinjaBuiltinCatalog {

    private static final Set<String> NAMES =
            Collections.unmodifiableSet(
                    new LinkedHashSet<>(
                            List.of(
                                    "url_for",
                                    "get_flashed_messages",
                                    "request",
                                    "session",
                                    "config",
                                    "g",
                                    "range",
                                    "dict",
                                    "namespace"
                            )
                    )
            );

    private JinjaBuiltinCatalog() {
    }

    public static Set<String> names() {
        return NAMES;
    }
}
