package compiler.generation;

import jinja2.runtime.RenderEnvironment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record TemplateRenderRequest(
        String templateName,
        Map<String, Object> context,
        RenderEnvironment environment
) {
    public TemplateRenderRequest {
        Objects.requireNonNull(templateName);
        Objects.requireNonNull(context);
        Objects.requireNonNull(environment);

        if (templateName.isBlank()) {
            throw new IllegalArgumentException(
                    "Template name cannot be blank"
            );
        }

        /*
         * Map.copyOf cannot be used because Python/Jinja values
         * are allowed to contain null.
         */
        context = Collections.unmodifiableMap(
                new LinkedHashMap<>(context)
        );
    }
}