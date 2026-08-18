package compiler.preparation;

import css.models.Stylesheet;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record CssCompilationResult(
        Map<String, Stylesheet> stylesheets
) {
    public CssCompilationResult {
        Objects.requireNonNull(stylesheets);

        stylesheets =
                Collections.unmodifiableMap(
                        new LinkedHashMap<>(
                                stylesheets
                        )
                );
    }
}