package python.runtime;

import python.models.atom_statement.*;
import python.models.expr_statement.*;
import python.models.trailer.*;
import utils.CompilerUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PythonExpressionEvaluator {
    private final PythonArithmeticEvaluator arithmeticEvaluator;
    public PythonExpressionEvaluator() {
        this(new PythonArithmeticEvaluator());
    }

    public PythonExpressionEvaluator(
            PythonArithmeticEvaluator arithmeticEvaluator
    ) {
        this.arithmeticEvaluator =
                Objects.requireNonNull(arithmeticEvaluator);
    }
    public Object evaluate(
            Condition expression,
            PythonEnvironment environment
    ) {
        Objects.requireNonNull(expression);
        Objects.requireNonNull(environment);

        return switch (expression) {
            case StringAtom stringAtom ->
                    CompilerUtils.stripStringQuotes(
                            stringAtom.value
                    );

            case IntegerAtom integerAtom ->
                    integerAtom.value;

            case FloatAtom floatAtom ->
                    floatAtom.value;

            case BoolAtom boolAtom ->
                    boolAtom.value;

            case None ignored ->
                    null;

            case ParenAtom parenAtom ->
                    evaluate(
                            parenAtom.inner,
                            environment
                    );

            case python.models.atom_statement.List list ->
                    evaluateList(
                            list,
                            environment
                    );

            case Dictionary dictionary ->
                    evaluateDictionary(
                            dictionary,
                            environment
                    );
            case ID identifier ->
                    environment.resolve(
                            identifier.name
                    );

            case IDTrailer identifierTrailer ->
                    evaluateIdentifierTrailer(
                            identifierTrailer,
                            environment
                    );
            case BinaryExpression binaryExpression ->
                    arithmeticEvaluator.evaluateBinary(
                            binaryExpression.operation,
                            evaluate(
                                    binaryExpression.left,
                                    environment
                            ),
                            evaluate(
                                    binaryExpression.right,
                                    environment
                            ),
                            binaryExpression.getLine()
                    );
            case RelationalComparison comparison ->
                    evaluateComparison(
                            comparison,
                            environment
                    );

            case CompoundCondition condition ->
                    evaluateCompoundCondition(
                            condition,
                            environment
                    );

            default ->
                    throw new UnsupportedOperationException(
                            "Python expression is not supported yet: "
                                    + expression.getSimpleName()
                                    + " at line "
                                    + expression.getLine()
                    );
        };
    }

    private ArrayList<Object> evaluateList(
            python.models.atom_statement.List list,
            PythonEnvironment environment
    ) {
        ArrayList<Object> values =
                new ArrayList<>();

        for (Expression element : list.content) {
            values.add(
                    evaluate(
                            element,
                            environment
                    )
            );
        }

        return values;
    }

    private Map<Object, Object> evaluateDictionary(
            Dictionary dictionary,
            PythonEnvironment environment
    ) {
        if (dictionary.keys.size()
                != dictionary.values.size()) {

            throw new IllegalStateException(
                    "Malformed Python dictionary at line "
                            + dictionary.getLine()
            );
        }

        Map<Object, Object> values =
                new LinkedHashMap<>();

        for (int index = 0;
             index < dictionary.keys.size();
             index++) {

            Object key =
                    evaluate(
                            dictionary.keys.get(index),
                            environment
                    );

            Object value =
                    evaluate(
                            dictionary.values.get(index),
                            environment
                    );

            /*
             * Repeated keys replace earlier values,
             * matching Python dictionary behavior.
             */
            values.put(key, value);
        }

        return values;
    }
    private Object evaluateIdentifierTrailer(
            IDTrailer expression,
            PythonEnvironment environment
    ) {
        Object currentValue =
                environment.resolve(
                        expression.id.name
                );

        if (expression.trailers == null) {
            return currentValue;
        }

        for (Trailer trailer : expression.trailers) {
            currentValue =
                    applyTrailer(
                            currentValue,
                            trailer,
                            environment
                    );
        }

        return currentValue;
    }
    private Object applyTrailer(
            Object target,
            Trailer trailer,
            PythonEnvironment environment
    ) {
        Object currentValue =
                target;

        if (trailer.isDotIdTrailer()) {
            if (trailer.id == null) {
                throw new IllegalStateException(
                        "Malformed Python attribute trailer"
                                + " at line "
                                + trailer.getLine()
                );
            }

            currentValue =
                    resolveAttribute(
                            currentValue,
                            trailer.id.name,
                            trailer.getLine()
                    );
        }

        if (trailer.arguments != null) {
            currentValue =
                    applyArguments(
                            currentValue,
                            trailer.arguments,
                            environment
                    );
        }

        return currentValue;
    }

    private Object applyArguments(
            Object target,
            Arguments arguments,
            PythonEnvironment environment
    ) {
        if (arguments
                instanceof SubscriptArguments subscript) {

            return resolveSubscript(
                    target,
                    subscript,
                    environment
            );
        }

        if (arguments
                instanceof CallArguments callArguments) {

            return invokeCallable(
                    target,
                    callArguments,
                    environment
            );
        }

        throw new UnsupportedOperationException(
                "Python trailer arguments are not supported: "
                        + arguments.getSimpleName()
                        + " at line "
                        + arguments.getLine()
        );
    }

    private Object resolveAttribute(
            Object target,
            String attributeName,
            int line
    ) {
        /*
         * Runtime objects such as request are initially represented
         * by maps of attribute names to values.
         */
        if (target instanceof Map<?, ?> map) {
            if (map.containsKey(attributeName)) {
                return map.get(attributeName);
            }

            throw new IllegalStateException(
                    "Python attribute '"
                            + attributeName
                            + "' does not exist"
                            + " at line "
                            + line
            );
        }
        if (target instanceof java.util.List<?> list
                && attributeName.equals("append")) {

            return createListAppendCallable(list);
        }
        if (target
                instanceof PythonAttributeContainer container) {

            return container.getAttribute(
                    attributeName,
                    line
            );
        }
        throw new UnsupportedOperationException(
                "Python attribute access is not supported on "
                        + describeType(target)
                        + " at line "
                        + line
        );
    }

    private Object resolveSubscript(
            Object target,
            SubscriptArguments subscript,
            PythonEnvironment environment
    ) {
        if (subscript.conditions == null
                || subscript.conditions.size() != 1) {

            throw new UnsupportedOperationException(
                    "Only one Python subscript is supported"
                            + " at line "
                            + subscript.getLine()
            );
        }

        Object index =
                evaluate(
                        subscript.conditions.getFirst(),
                        environment
                );

        if (target instanceof Map<?, ?> map) {
            if (map.containsKey(index)) {
                return map.get(index);
            }

            throw new IllegalStateException(
                    "Python dictionary key does not exist: "
                            + index
                            + " at line "
                            + subscript.getLine()
            );
        }

        if (target instanceof java.util.List<?> list) {
            int resolvedIndex =
                    resolveSequenceIndex(
                            index,
                            list.size(),
                            subscript.getLine()
                    );

            return list.get(resolvedIndex);
        }

        if (target instanceof String string) {
            int resolvedIndex =
                    resolveSequenceIndex(
                            index,
                            string.length(),
                            subscript.getLine()
                    );

            return String.valueOf(
                    string.charAt(resolvedIndex)
            );
        }

        throw new UnsupportedOperationException(
                "Python indexing is not supported on "
                        + describeType(target)
                        + " at line "
                        + subscript.getLine()
        );
    }

    private int resolveSequenceIndex(
            Object index,
            int size,
            int line
    ) {
        if (!(index instanceof Byte
                || index instanceof Short
                || index instanceof Integer
                || index instanceof Long)) {

            throw new UnsupportedOperationException(
                    "Python sequence index must be an integer"
                            + " at line "
                            + line
            );
        }

        long resolvedIndex =
                ((Number) index).longValue();

        if (resolvedIndex < 0) {
            resolvedIndex += size;
        }

        if (resolvedIndex < 0
                || resolvedIndex >= size) {

            throw new IndexOutOfBoundsException(
                    "Python sequence index "
                            + index
                            + " is out of range"
                            + " at line "
                            + line
            );
        }

        return (int) resolvedIndex;
    }

    private String describeType(Object value) {
        return value == null
                ? "None"
                : value.getClass().getSimpleName();
    }
    private Object evaluateComparison(
            RelationalComparison comparison,
            PythonEnvironment environment
    ) {
        Object left =
                evaluate(
                        comparison.left,
                        environment
                );

        Object right =
                evaluate(
                        comparison.right,
                        environment
                );

        return switch (comparison.operation) {
            case EQUALS ->
                    valuesEqual(left, right);

            case NOT_EQ ->
                    !valuesEqual(left, right);

            case LESS_THAN ->
                    compareOrdered(
                            left,
                            right,
                            comparison.getLine()
                    ) < 0;

            case GREATER_THAN ->
                    compareOrdered(
                            left,
                            right,
                            comparison.getLine()
                    ) > 0;

            case LT_EQ ->
                    compareOrdered(
                            left,
                            right,
                            comparison.getLine()
                    ) <= 0;

            case GT_EQ ->
                    compareOrdered(
                            left,
                            right,
                            comparison.getLine()
                    ) >= 0;

            default ->
                    throw new UnsupportedOperationException(
                            "Python comparison "
                                    + comparison.operation
                                    + " is not supported yet"
                                    + " at line "
                                    + comparison.getLine()
                    );
        };
    }

    private Object evaluateCompoundCondition(
            CompoundCondition condition,
            PythonEnvironment environment
    ) {
        return switch (condition.operation) {
            case NOT -> {
                Object value =
                        evaluate(
                                condition.first,
                                environment
                        );

                yield !PythonTruthiness.isTruthy(value);
            }

            /*
             * Python and/or return an operand rather than always
             * returning a Boolean.
             */
            case AND -> {
                Object left =
                        evaluate(
                                condition.first,
                                environment
                        );

                if (!PythonTruthiness.isTruthy(left)) {
                    yield left;
                }

                yield evaluateRequiredSecond(
                        condition,
                        environment
                );
            }

            case OR -> {
                Object left =
                        evaluate(
                                condition.first,
                                environment
                        );

                if (PythonTruthiness.isTruthy(left)) {
                    yield left;
                }

                yield evaluateRequiredSecond(
                        condition,
                        environment
                );
            }

            default ->
                    throw new UnsupportedOperationException(
                            "Python logical operation "
                                    + condition.operation
                                    + " is not supported yet"
                                    + " at line "
                                    + condition.getLine()
                    );
        };
    }

    private Object evaluateRequiredSecond(
            CompoundCondition condition,
            PythonEnvironment environment
    ) {
        if (condition.second == null) {
            throw new IllegalStateException(
                    "Python logical expression has no second operand"
                            + " at line "
                            + condition.getLine()
            );
        }

        return evaluate(
                condition.second,
                environment
        );
    }

    private boolean valuesEqual(
            Object left,
            Object right
    ) {
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return Double.compare(
                    leftNumber.doubleValue(),
                    rightNumber.doubleValue()
            ) == 0;
        }

        return Objects.equals(left, right);
    }

    private int compareOrdered(
            Object left,
            Object right,
            int line
    ) {
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return Double.compare(
                    leftNumber.doubleValue(),
                    rightNumber.doubleValue()
            );
        }

        if (left instanceof String leftString
                && right instanceof String rightString) {

            return leftString.compareTo(
                    rightString
            );
        }

        throw new UnsupportedOperationException(
                "Python ordered comparison is not supported between "
                        + describeType(left)
                        + " and "
                        + describeType(right)
                        + " at line "
                        + line
        );
    }
    private Object invokeCallable(
            Object target,
            CallArguments callArguments,
            PythonEnvironment environment
    ) {
        if (!(target instanceof PythonCallable callable)) {
            throw new UnsupportedOperationException(
                    "Python value of type "
                            + describeType(target)
                            + " is not callable"
                            + " at line "
                            + callArguments.getLine()
            );
        }

        PythonCallArguments runtimeArguments =
                evaluateCallArguments(
                        callArguments,
                        environment
                );

        return callable.call(runtimeArguments);
    }
    private PythonCallArguments evaluateCallArguments(
            CallArguments callArguments,
            PythonEnvironment environment
    ) {
        java.util.List<Object> positional =
                new ArrayList<>();

        Map<String, Object> keywords =
                new LinkedHashMap<>();

        boolean encounteredKeyword = false;

        if (callArguments.args != null) {
            for (Argument argument
                    : callArguments.args) {

                if (argument.isAssigned()) {
                    encounteredKeyword = true;

                    String keywordName =
                            extractKeywordName(
                                    argument.arg,
                                    argument.getLine()
                            );

                    if (keywords.containsKey(keywordName)) {
                        throw new IllegalArgumentException(
                                "Duplicate keyword argument '"
                                        + keywordName
                                        + "' at line "
                                        + argument.getLine()
                        );
                    }

                    Object value =
                            evaluate(
                                    argument.assign,
                                    environment
                            );

                    keywords.put(
                            keywordName,
                            value
                    );

                    continue;
                }

                if (encounteredKeyword) {
                    throw new IllegalArgumentException(
                            "Positional argument cannot follow"
                                    + " a keyword argument"
                                    + " at line "
                                    + argument.getLine()
                    );
                }

                positional.add(
                        evaluate(
                                argument.arg,
                                environment
                        )
                );
            }
        }

        return new PythonCallArguments(
                positional,
                keywords,
                callArguments.getLine()
        );
    }
    private String extractKeywordName(
            Condition node,
            int line
    ) {
        if (node instanceof IDTrailer identifier
                && (identifier.trailers == null
                || identifier.trailers.isEmpty())) {

            return identifier.id.name;
        }

        // Included because ID is also an expression in this AST hierarchy.
        if (node instanceof ID identifier) {
            return identifier.name;
        }

        throw new IllegalArgumentException(
                "Keyword argument name must be"
                        + " a plain identifier"
                        + " at line "
                        + line
        );
    }
    private PythonCallable createListAppendCallable(
            java.util.List<?> list
    ) {
        return new PythonNativeFunction(
                "list.append",
                arguments -> {
                    if (!arguments.keywords().isEmpty()
                            || arguments.positional().size()
                            != 1) {

                        throw new IllegalArgumentException(
                                "list.append() expects exactly"
                                        + " one positional argument"
                                        + " at line "
                                        + arguments.sourceLine()
                        );
                    }

                    @SuppressWarnings("unchecked")
                    java.util.List<Object> mutableList =
                            (java.util.List<Object>) list;

                    try {
                        mutableList.add(
                                arguments.positional().getFirst()
                        );
                    } catch (UnsupportedOperationException exception) {
                        throw new UnsupportedOperationException(
                                "Cannot append to an immutable Python list"
                                        + " at line "
                                        + arguments.sourceLine(),
                                exception
                        );
                    }

                    // Python list.append() returns None.
                    return null;
                }
        );
    }
}