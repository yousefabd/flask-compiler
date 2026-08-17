package jinja2.semantic;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable result of classifying names in one template.
 *
 * <p>The maps preserve first occurrence order and source lines. An external
 * name appears once in {@link #externalVariables()}, even when it is read
 * several times in the template.</p>
 */
public record JinjaFreeVariableResult(
        List<JinjaNameUse> nameUses,
        Map<String, Integer> externalVariables,
        Map<String, Integer> localDeclarations,
        List<JinjaIncludeSite> includeSites
) {
    public JinjaFreeVariableResult {
        Objects.requireNonNull(nameUses);
        Objects.requireNonNull(externalVariables);
        Objects.requireNonNull(localDeclarations);
        Objects.requireNonNull(includeSites);

        nameUses = List.copyOf(nameUses);
        externalVariables = Collections.unmodifiableMap(
                new LinkedHashMap<>(externalVariables)
        );
        localDeclarations = Collections.unmodifiableMap(
                new LinkedHashMap<>(localDeclarations)
        );
        includeSites = List.copyOf(includeSites);
    }
}
