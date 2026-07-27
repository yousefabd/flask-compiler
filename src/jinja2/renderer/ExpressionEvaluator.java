package jinja2.renderer;

import jinja2.models.expression.BinaryExpressionNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.expression.PropertyAccessNode;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import utils.CompilerUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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

            case BinaryExpressionNode binaryExpression ->
                    evaluateBinaryExpression(
                            binaryExpression,
                            context
                    );

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
    private Object evaluateBinaryExpression(
            BinaryExpressionNode expression,
            RenderContext context
    ) {
        Object leftValue = evaluate(
                expression.getLeft(),
                context
        );

        Object rightValue = evaluate(
                expression.getRight(),
                context
        );

        return switch (expression.getOperation()) {
            case EQ ->
                    valuesAreEqual(
                            leftValue,
                            rightValue
                    );

            case NEQ ->
                    !valuesAreEqual(
                            leftValue,
                            rightValue
                    );

            case LT ->
                    compareValues(
                            leftValue,
                            rightValue,
                            expression
                    ) < 0;

            case GT ->
                    compareValues(
                            leftValue,
                            rightValue,
                            expression
                    ) > 0;

            case LTE ->
                    compareValues(
                            leftValue,
                            rightValue,
                            expression
                    ) <= 0;

            case GTE ->
                    compareValues(
                            leftValue,
                            rightValue,
                            expression
                    ) >= 0;

            default -> throw new UnsupportedOperationException(
                    "Binary operation "
                            + expression.getOperation()
                            + " is not supported yet at line "
                            + expression.getLineNumber()
            );
        };
    }
    private int compareValues(
            Object left,
            Object right,
            BinaryExpressionNode expression
    ) {
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return compareNumbers(
                    leftNumber,
                    rightNumber
            );
        }

        if (left instanceof String leftString
                && right instanceof String rightString) {

            return leftString.compareTo(rightString);
        }
        throw new IllegalStateException(
                "Cannot compare "
                        + describeType(left)
                        + " with "
                        + describeType(right)
                        + " using "
                        + expression.getOperation()
                        + " at line "
                        + expression.getLineNumber()
        );
    }

    private String describeType(Object value) {
        if (value == null) {
            return "none";
        }

        return value.getClass().getSimpleName();
    }

    private boolean valuesAreEqual(
            Object left,
            Object right
    ) {
        /*
         * Java normally considers Long(24) and Double(24.0)
         * different. Jinja/Python considers them numerically equal.
         */
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return compareNumbers(
                    leftNumber,
                    rightNumber
            ) == 0;
        }

        return Objects.equals(left, right);
    }

    private int compareNumbers(
            Number left,
            Number right
    ) {
        BigDecimal leftDecimal =
                new BigDecimal(left.toString());

        BigDecimal rightDecimal =
                new BigDecimal(right.toString());

        return leftDecimal.compareTo(rightDecimal);
    }
    private Number parseNumber(
            String rawValue
    ) {
        if (rawValue.contains(".")) {
            return Double.parseDouble(rawValue);
        }

        return Long.parseLong(rawValue);
    }
    public boolean evaluateCondition(
            ExpressionNode expression,
            RenderContext context
    ) {
        Object value = evaluate(
                expression,
                context
        );

        return isTruthy(value);
    }

    private boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        if (value instanceof Number number) {
            return number.doubleValue() != 0;
        }

        if (value instanceof CharSequence text) {
            return !text.isEmpty();
        }

        if (value instanceof Collection<?> collection) {
            return !collection.isEmpty();
        }

        if (value instanceof Map<?, ?> map) {
            return !map.isEmpty();
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }
}