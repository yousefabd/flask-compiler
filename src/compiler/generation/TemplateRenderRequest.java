package compiler.generation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
//template request contains values produced by CPython
public record TemplateRenderRequest(
        String ownerFunctionName,
        String templateName,
        Map<String, Object> contextValues
) {
    public TemplateRenderRequest {
        contextValues = Collections.unmodifiableMap(
                new LinkedHashMap<>(contextValues)
        );
    }
}