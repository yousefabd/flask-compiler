package jinja2.renderer;

import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.expression.PropertyAccessNode;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import utils.CompilerUtils;

import java.util.Map;

public final class ExpressionEvaluator {

    public Object evaluate(
            ExpressionNode expression,
            RenderContext context
    ) {
        return switch (expression) {
            case IdentifierNode identifier ->
                    context.resolve(
                            identifier.getName()
                    );

            case PropertyAccessNode propertyAccess ->
                    evaluatePropertyAccess(
                            propertyAccess,
                            context
                    );

            case StringLiteralNode stringLiteral ->
                    CompilerUtils.stripStringQuotes(
                            stringLiteral.getValue()
                    );

            case NumberLiteralNode numberLiteral ->
                    parseNumber(
                            numberLiteral.getValue()
                    );

            case BooleanLiteralNode booleanLiteral ->
                    booleanLiteral.getValue();

            case NoneLiteralNode ignored ->
                    null;

            default -> throw new UnsupportedOperationException(
                    "Expression is not supported yet: "
                            + expression.getClass().getSimpleName()
                            + " at line "
                            + expression.getLineNumber()
            );
        };
    }

    private Object evaluatePropertyAccess(
            PropertyAccessNode propertyAccess,
            RenderContext context
    ) {
        /*
         * For user.profile.name:
         *
         * 1. Evaluate user.
         * 2. Read profile.
         * 3. Read name.
         *
         * Recursive evaluate() calls make chained access work
         * automatically.
         */
        Object targetValue = evaluate(
                propertyAccess.getTarget(),
                context
        );

        String propertyName =
                propertyAccess
                        .getProperty()
                        .getName();

        if (targetValue == null) {
            throw new IllegalStateException(
                    "Cannot access property '"
                            + propertyName
                            + "' on none at line "
                            + propertyAccess.getLineNumber()
            );
        }

        /*
         * Python dictionaries and JSON objects become Java Maps
         * after Gson deserialization.
         */
        if (targetValue instanceof Map<?, ?> map) {
            if (!map.containsKey(propertyName)) {
                throw new IllegalStateException(
                        "Property '"
                                + propertyName
                                + "' does not exist at line "
                                + propertyAccess.getLineNumber()
                );
            }

            return map.get(propertyName);
        }

        throw new UnsupportedOperationException(
                "Cannot access property '"
                        + propertyName
                        + "' on value of type "
                        + targetValue
                        .getClass()
                        .getSimpleName()
                        + " at line "
                        + propertyAccess.getLineNumber()
        );
    }

    private Number parseNumber(
            String rawValue
    ) {
        if (rawValue.contains(".")) {
            return Double.parseDouble(rawValue);
        }

        return Long.parseLong(rawValue);
    }
}