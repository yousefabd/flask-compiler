package jinja2.renderer;

import jinja2.models.expression.*;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import utils.CompilerUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

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
            case ListExpressionNode listExpression ->
                    evaluateListExpression(
                            listExpression,
                            context
                    );
            case DictionaryExpressionNode dictionaryExpression ->
                    evaluateDictionaryExpression(
                            dictionaryExpression,
                            context
                    );
            case IndexAccessNode indexAccess ->
                    evaluateIndexAccess(
                            indexAccess,
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
    private Object evaluateArithmetic(
            Operation operation,
            Object left,
            Object right,
            BinaryExpressionNode expression
    ) {
        /*
         * String concatenation.
         */
        if (operation == Operation.PLUS
                && left instanceof String leftString
                && right instanceof String rightString) {

            return leftString + rightString;
        }

        /*
         * String repetition:
         *
         * "ha" * 3 -> "hahaha"
         */
        if (operation == Operation.STAR
                && left instanceof String text
                && right instanceof Number repetitions) {

            return repeatString(
                    text,
                    repetitions,
                    expression
            );
        }

        if (!(left instanceof Number leftNumber)
                || !(right instanceof Number rightNumber)) {

            throw invalidArithmeticOperands(
                    operation,
                    left,
                    right,
                    expression
            );
        }

        boolean integerOperation =
                isIntegerNumber(leftNumber)
                        && isIntegerNumber(rightNumber);

        return switch (operation) {
            case PLUS -> {
                if (integerOperation) {
                    yield Math.addExact(
                            leftNumber.longValue(),
                            rightNumber.longValue()
                    );
                }

                yield leftNumber.doubleValue()
                        + rightNumber.doubleValue();
            }

            case MINUS -> {
                if (integerOperation) {
                    yield Math.subtractExact(
                            leftNumber.longValue(),
                            rightNumber.longValue()
                    );
                }

                yield leftNumber.doubleValue()
                        - rightNumber.doubleValue();
            }

            case STAR -> {
                if (integerOperation) {
                    yield Math.multiplyExact(
                            leftNumber.longValue(),
                            rightNumber.longValue()
                    );
                }

                yield leftNumber.doubleValue()
                        * rightNumber.doubleValue();
            }

            case SLASH -> {
                ensureNotZero(
                        rightNumber,
                        "Division",
                        expression
                );

                /*
                 * Like Python 3, normal division always produces
                 * a floating-point result.
                 */
                yield leftNumber.doubleValue()
                        / rightNumber.doubleValue();
            }

            case PERCENT -> {
                ensureNotZero(
                        rightNumber,
                        "Modulo",
                        expression
                );

                if (integerOperation) {
                    yield leftNumber.longValue()
                            % rightNumber.longValue();
                }

                yield leftNumber.doubleValue()
                        % rightNumber.doubleValue();
            }

            default -> throw new IllegalStateException(
                    "Operation is not arithmetic: "
                            + operation
            );
        };
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
            case PLUS, MINUS, STAR, SLASH, PERCENT ->
                    evaluateArithmetic(
                            expression.getOperation(),
                            leftValue,
                            rightValue,
                            expression
                    );

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
    private String repeatString(
            String text,
            Number repetitions,
            BinaryExpressionNode expression
    ) {
        double numericCount =
                repetitions.doubleValue();

        int count =
                repetitions.intValue();

        if (!Double.isFinite(numericCount)
                || numericCount != count) {

            throw new IllegalStateException(
                    "String repetition requires a whole number "
                            + "at line "
                            + expression.getLineNumber()
            );
        }

        /*
         * Python treats a negative repetition count as empty.
         */
        if (count <= 0) {
            return "";
        }

        return text.repeat(count);
    }

    private boolean isIntegerNumber(
            Number number
    ) {
        return number instanceof Byte
                || number instanceof Short
                || number instanceof Integer
                || number instanceof Long;
    }

    private void ensureNotZero(
            Number number,
            String operationName,
            BinaryExpressionNode expression
    ) {
        if (compareNumbers(number, 0L) == 0) {
            throw new IllegalStateException(
                    operationName
                            + " by zero at line "
                            + expression.getLineNumber()
            );
        }
    }

    private IllegalStateException invalidArithmeticOperands(
            Operation operation,
            Object left,
            Object right,
            BinaryExpressionNode expression
    ) {
        return new IllegalStateException(
                "Operator "
                        + operation
                        + " cannot be applied to "
                        + describeType(left)
                        + " and "
                        + describeType(right)
                        + " at line "
                        + expression.getLineNumber()
        );
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
        switch (value) {
            case null -> {
                return false;
            }
            case Boolean booleanValue -> {
                return booleanValue;
            }
            case Number number -> {
                return number.doubleValue() != 0;
            }
            case CharSequence text -> {
                return !text.isEmpty();
            }
            case Collection<?> collection -> {
                return !collection.isEmpty();
            }
            case Map<?, ?> map -> {
                return !map.isEmpty();
            }
            default -> {
            }
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }
    private List<Object> evaluateListExpression(
            ListExpressionNode expression,
            RenderContext context
    ) {
        List<Object> values =
                new ArrayList<>(expression.getElements().size());

        for (ExpressionNode element : expression.getElements()) {
            values.add(evaluate(element, context));
        }

        return values;
    }
    //region evaluate dictionary
    private Map<Object, Object> evaluateDictionaryExpression(
            DictionaryExpressionNode expression,
            RenderContext context
    ) {
        List<ExpressionNode> keys =
                expression.getKeys();

        List<ExpressionNode> values =
                expression.getValues();

        if (keys.size() != values.size()) {
            throw new IllegalStateException(
                    "Malformed dictionary expression at line "
                            + expression.getLineNumber()
            );
        }

        /*
         * Keeps dictionary iteration in the same order as the
         * entries appeared in the Jinja template.
         */
        Map<Object, Object> dictionary =
                new LinkedHashMap<>();

        for (int index = 0; index < keys.size(); index++) {
            Object key = evaluate(
                    keys.get(index),
                    context
            );

            validateDictionaryKey(
                    key,
                    expression
            );

            Object value = evaluate(
                    values.get(index),
                    context
            );

            /*
             * If the same key occurs more than once, the later
             * value replaces the earlier one, like Python.
             */
            dictionary.put(key, value);
        }

        return dictionary;
    }
    private void validateDictionaryKey(
            Object key,
            DictionaryExpressionNode expression
    ) {
        /*
         * These are the immutable/hashable values currently
         * supported by the expression evaluator.
         */
        if (key == null
                || key instanceof String
                || key instanceof Number
                || key instanceof Boolean) {
            return;
        }

        throw new IllegalStateException(
                "Dictionary key must be a string, number, boolean, or none, "
                        + "but received "
                        + describeType(key)
                        + " at line "
                        + expression.getLineNumber()
        );
    }
    //endregion

    //region evaluate index access
    private Object evaluateIndexAccess(
            IndexAccessNode expression,
            RenderContext context
    ) {
        Object target = evaluate(
                expression.getTarget(),
                context
        );

        Object index = evaluate(
                expression.getIndex(),
                context
        );

        switch (target) {
            case null -> throw new IllegalStateException(
                    "Cannot index none at line "
                            + expression.getLineNumber()
            );
            case Map<?, ?> map -> {
                return evaluateMapIndex(
                        map,
                        index,
                        expression
                );
            }
            case List<?> list -> {
                int normalizedIndex = normalizeSequenceIndex(
                        requireIntegerIndex(index, expression),
                        list.size(),
                        expression
                );

                return list.get(normalizedIndex);
            }
            case CharSequence text -> {
                return evaluateStringIndex(
                        text,
                        index,
                        expression
                );
            }
            default -> {
            }
        }

        if (target.getClass().isArray()) {
            int length = Array.getLength(target);

            int normalizedIndex = normalizeSequenceIndex(
                    requireIntegerIndex(index, expression),
                    length,
                    expression
            );

            return Array.get(target, normalizedIndex);
        }

        throw new IllegalStateException(
                "Value of type "
                        + describeType(target)
                        + " does not support indexing at line "
                        + expression.getLineNumber()
        );
    }
    private Object evaluateMapIndex(
            Map<?, ?> map,
            Object index,
            IndexAccessNode expression
    ) {
        if (map.containsKey(index)) {
            return map.get(index);
        }

        /*
         * Java considers Long(1) and Double(1.0) different map keys,
         * while Python/Jinja considers them numerically equal.
         */
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (valuesAreEqual(entry.getKey(), index)) {
                return entry.getValue();
            }
        }

        throw new IllegalStateException(
                "Dictionary key '"
                        + index
                        + "' does not exist at line "
                        + expression.getLineNumber()
        );
    }
    private int requireIntegerIndex(
            Object index,
            IndexAccessNode expression
    ) {
        if (!(index instanceof Number number)) {
            throw new IllegalStateException(
                    "Sequence index must be a whole number, but received "
                            + describeType(index)
                            + " at line "
                            + expression.getLineNumber()
            );
        }

        try {
            return new BigDecimal(
                    number.toString()
            ).intValueExact();
        } catch (ArithmeticException | NumberFormatException exception) {
            throw new IllegalStateException(
                    "Sequence index must be a whole number within integer range, "
                            + "but received "
                            + number
                            + " at line "
                            + expression.getLineNumber()
            );
        }
    }
    private int normalizeSequenceIndex(
            int index,
            int size,
            IndexAccessNode expression
    ) {
        int normalizedIndex =
                index < 0
                        ? size + index
                        : index;

        if (normalizedIndex < 0 || normalizedIndex >= size) {
            throw new IllegalStateException(
                    "Sequence index "
                            + index
                            + " is out of bounds for sequence of size "
                            + size
                            + " at line "
                            + expression.getLineNumber()
            );
        }

        return normalizedIndex;
    }
    private String evaluateStringIndex(
            CharSequence text,
            Object index,
            IndexAccessNode expression
    ) {
        String string = text.toString();

        int characterCount =
                string.codePointCount(
                        0,
                        string.length()
                );

        int normalizedIndex = normalizeSequenceIndex(
                requireIntegerIndex(index, expression),
                characterCount,
                expression
        );

        int characterOffset =
                string.offsetByCodePoints(
                        0,
                        normalizedIndex
                );

        int codePoint =
                string.codePointAt(characterOffset);

        return new String(
                Character.toChars(codePoint)
        );
    }
    //endregion
}