package python.runtime;

import python.models.enums.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PythonArithmeticEvaluator {

    public Object evaluateBinary(
            Operation operation,
            Object left,
            Object right,
            int line
    ) {
        Objects.requireNonNull(operation);

        return switch (operation) {
            case ADD ->
                    add(left, right, line);

            case MULT ->
                    multiply(left, right, line);

            default ->
                    throw new UnsupportedOperationException(
                            "Python binary operation "
                                    + operation
                                    + " is not supported yet"
                                    + " at line "
                                    + line
                    );
        };
    }

    private Object add(
            Object left,
            Object right,
            int line
    ) {
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return addNumbers(
                    leftNumber,
                    rightNumber
            );
        }

        if (left instanceof String leftString
                && right instanceof String rightString) {

            return leftString + rightString;
        }

        if (left instanceof List<?> leftList
                && right instanceof List<?> rightList) {

            ArrayList<Object> result =
                    new ArrayList<>(
                            leftList.size()
                                    + rightList.size()
                    );

            result.addAll(leftList);
            result.addAll(rightList);

            return result;
        }

        throw unsupportedTypes(
                Operation.ADD,
                left,
                right,
                line
        );
    }

    private Object multiply(
            Object left,
            Object right,
            int line
    ) {
        if (left instanceof Number leftNumber
                && right instanceof Number rightNumber) {

            return multiplyNumbers(
                    leftNumber,
                    rightNumber
            );
        }

        throw unsupportedTypes(
                Operation.MULT,
                left,
                right,
                line
        );
    }

    private Object addNumbers(
            Number left,
            Number right
    ) {
        if (isFloating(left)
                || isFloating(right)) {

            return left.doubleValue()
                    + right.doubleValue();
        }

        long result =
                Math.addExact(
                        left.longValue(),
                        right.longValue()
                );

        return narrowInteger(result);
    }

    private Object multiplyNumbers(
            Number left,
            Number right
    ) {
        if (isFloating(left)
                || isFloating(right)) {

            return left.doubleValue()
                    * right.doubleValue();
        }

        long result =
                Math.multiplyExact(
                        left.longValue(),
                        right.longValue()
                );

        return narrowInteger(result);
    }

    private boolean isFloating(Number number) {
        return number instanceof Float
                || number instanceof Double;
    }

    private Object narrowInteger(long value) {
        if (value >= Integer.MIN_VALUE
                && value <= Integer.MAX_VALUE) {

            return (int) value;
        }

        return value;
    }

    private UnsupportedOperationException unsupportedTypes(
            Operation operation,
            Object left,
            Object right,
            int line
    ) {
        return new UnsupportedOperationException(
                "Python operation "
                        + operation
                        + " is not supported between "
                        + describeType(left)
                        + " and "
                        + describeType(right)
                        + " at line "
                        + line
        );
    }

    private String describeType(Object value) {
        return value == null
                ? "None"
                : value.getClass().getSimpleName();
    }
}