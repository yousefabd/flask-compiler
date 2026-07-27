package compiler.template;

import python.models.expr_statement.Condition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * One render_template(...) call discovered in the Python AST.
 *
 * contextArguments contains Python AST expressions, not resolved values.
 */
public record TemplateCall(
        String ownerFunctionName,
        String templateName,
        Map<String, Condition> contextArguments,
        int line
) {
    public TemplateCall {
        Objects.requireNonNull(ownerFunctionName);
        Objects.requireNonNull(templateName);
        Objects.requireNonNull(contextArguments);

        contextArguments = Collections.unmodifiableMap(
                new LinkedHashMap<>(contextArguments)
        );
    }
}