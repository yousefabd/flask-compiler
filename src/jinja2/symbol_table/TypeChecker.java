package jinja2.symbol_table;

import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.ListExpressionNode;
import jinja2.models.expression.Operation;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;

import java.util.List;

/**
 * Static type rules for the Jinja2 expression subset.
 *
 * Every check follows the same convention: if either side of an
 * operation is {@link Type#UNKNOWN}, no error is reported and the
 * result is UNKNOWN (we simply don't have enough information). This
 * keeps the checker free of false positives for things like macro
 * parameters, filters, function calls and undefined variables, which
 * are already reported by the existing scope/undefined-variable checks.
 */
public final class TypeChecker {

    private TypeChecker() {}

    /** Booleans behave like integers in arithmetic, mirroring Python/Jinja2. */
    public static boolean isNumeric(Type type) {
        return type == Type.INTEGER || type == Type.FLOAT || type == Type.BOOLEAN;
    }

    private static boolean isIntLike(Type type) {
        return type == Type.INTEGER || type == Type.BOOLEAN;
    }

    // ─────────────────────────────────────────────────────────────
    // BINARY OPERATORS
    // ─────────────────────────────────────────────────────────────

    public static Type checkBinary(Operation op, Type left, Type right,
                                    int line, List<CompilerError> errors) {
        switch (op) {

            case PLUS:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(left) && isNumeric(right))
                    return numericResult(left, right);
                if (left == Type.STRING && right == Type.STRING) return Type.STRING;
                if (left == Type.LIST && right == Type.LIST) return Type.LIST;
                return invalidBinary(op, left, right, line, errors);

            case MINUS:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(left) && isNumeric(right))
                    return numericResult(left, right);
                return invalidBinary(op, left, right, line, errors);

            case STAR:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(left) && isNumeric(right))
                    return numericResult(left, right);
                // string/list repetition:  "ab" * 3   or   3 * [1, 2]
                if (left == Type.STRING && isIntLike(right)) return Type.STRING;
                if (right == Type.STRING && isIntLike(left)) return Type.STRING;
                if (left == Type.LIST && isIntLike(right)) return Type.LIST;
                if (right == Type.LIST && isIntLike(left)) return Type.LIST;
                return invalidBinary(op, left, right, line, errors);

            case SLASH:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(left) && isNumeric(right)) return Type.FLOAT;
                return invalidBinary(op, left, right, line, errors);

            case PERCENT:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(left) && isNumeric(right))
                    return numericResult(left, right);
                return invalidBinary(op, left, right, line, errors);

            case LT, GT, LTE, GTE:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.BOOLEAN;
                if (isNumeric(left) && isNumeric(right)) return Type.BOOLEAN;
                if (left == right && (left == Type.STRING || left == Type.LIST)) return Type.BOOLEAN;
                errors.add(new CompilerError(CompilerError.Kind.TYPE_ERROR,
                        "'" + symbol(op) + "' not supported between instances of '"
                                + left.label() + "' and '" + right.label() + "'",
                        line));
                return Type.BOOLEAN;

            case EQ, NEQ:
                // equality is always well-defined, regardless of operand types
                return Type.BOOLEAN;

            case AND, OR:
                if (left == Type.UNKNOWN || right == Type.UNKNOWN) return Type.UNKNOWN;
                return left == right ? left : Type.UNKNOWN;

            case IN:
                if (right != Type.UNKNOWN
                        && right != Type.LIST
                        && right != Type.DICTIONARY
                        && right != Type.STRING) {
                    errors.add(new CompilerError(CompilerError.Kind.TYPE_ERROR,
                            "argument of type '" + right.label() + "' is not iterable",
                            line));
                }
                return Type.BOOLEAN;

            case IS:
                // right-hand side is a Jinja2 test name, not a value — don't type-check it
                return Type.BOOLEAN;

            default:
                return Type.UNKNOWN;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // UNARY OPERATORS
    // ─────────────────────────────────────────────────────────────

    public static Type checkUnary(Operation op, Type operand, int line, List<CompilerError> errors) {
        switch (op) {

            case NOT:
                return Type.BOOLEAN;

            case PLUS, MINUS:
                if (operand == Type.UNKNOWN) return Type.UNKNOWN;
                if (isNumeric(operand)) return operand == Type.FLOAT ? Type.FLOAT : Type.INTEGER;
                errors.add(new CompilerError(CompilerError.Kind.TYPE_ERROR,
                        "bad operand type for unary '" + symbol(op) + "': '" + operand.label() + "'",
                        line));
                return Type.UNKNOWN;

            default:
                return Type.UNKNOWN;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // ITERABILITY  (used for {% for x in <expr> %})
    // ─────────────────────────────────────────────────────────────

    public static void checkIterable(Type type, int line, List<CompilerError> errors) {
        if (type == Type.UNKNOWN) return;
        if (type == Type.STRING || type == Type.LIST || type == Type.DICTIONARY) return;

        errors.add(new CompilerError(CompilerError.Kind.TYPE_ERROR,
                "'" + type.label() + "' object is not iterable", line));
    }

    // ─────────────────────────────────────────────────────────────
    // ASSIGNMENT  (used for {% set x = ... %} reassignment)
    // ─────────────────────────────────────────────────────────────

    /**
     * Flags reassigning a variable to a value whose type is incompatible
     * with the type it previously held. INTEGER/FLOAT are considered a
     * compatible numeric family; NONE and UNKNOWN are always compatible.
     */
    public static void checkAssignment(Type previous, Type next, String name,
                                        int line, List<CompilerError> errors) {
        if (previous == Type.UNKNOWN || next == Type.UNKNOWN) return;
        if (previous == next) return;
        if (previous == Type.NONE || next == Type.NONE) return;
        if ((previous == Type.INTEGER || previous == Type.FLOAT)
                && (next == Type.INTEGER || next == Type.FLOAT)) return;

        errors.add(new CompilerError(CompilerError.Kind.TYPE_MISMATCH,
                "Variable '" + name + "' was previously assigned type '" + previous.label()
                        + "' but is now assigned a value of type '" + next.label() + "'",
                line));
    }

    // ─────────────────────────────────────────────────────────────
    // LITERAL INFERENCE
    // ─────────────────────────────────────────────────────────────

    /** Type of a literal expression, or UNKNOWN for anything else. */
    public static Type literalType(ExpressionNode expr) {
        if (expr instanceof NumberLiteralNode num)
            return num.getValue().contains(".") ? Type.FLOAT : Type.INTEGER;
        if (expr instanceof StringLiteralNode)  return Type.STRING;
        if (expr instanceof BooleanLiteralNode) return Type.BOOLEAN;
        if (expr instanceof NoneLiteralNode)    return Type.NONE;
        return Type.UNKNOWN;
    }

    /**
     * Infers the element type of a {% for %} loop variable when the
     * iterable is a list literal with elements that all share the same
     * literal type, e.g. {% for n in [1, 2, 3] %} → INTEGER.
     * Returns UNKNOWN for empty/heterogeneous/non-literal lists.
     */
    public static Type homogeneousElementType(ListExpressionNode list) {
        List<ExpressionNode> elements = list.getElements();
        if (elements.isEmpty()) return Type.UNKNOWN;

        Type first = literalType(elements.get(0));
        if (first == Type.UNKNOWN) return Type.UNKNOWN;

        for (ExpressionNode el : elements)
            if (literalType(el) != first) return Type.UNKNOWN;

        return first;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    private static Type numericResult(Type left, Type right) {
        return (left == Type.FLOAT || right == Type.FLOAT) ? Type.FLOAT : Type.INTEGER;
    }

    private static Type invalidBinary(Operation op, Type left, Type right,
                                       int line, List<CompilerError> errors) {
        errors.add(new CompilerError(CompilerError.Kind.TYPE_ERROR,
                "Cannot apply '" + symbol(op) + "' between '" + left.label()
                        + "' and '" + right.label() + "'",
                line));
        return Type.UNKNOWN;
    }

    private static String symbol(Operation op) {
        return switch (op) {
            case PLUS    -> "+";
            case MINUS   -> "-";
            case STAR    -> "*";
            case SLASH   -> "/";
            case PERCENT -> "%";
            case LT      -> "<";
            case GT      -> ">";
            case LTE     -> "<=";
            case GTE     -> ">=";
            case EQ      -> "==";
            case NEQ     -> "!=";
            case AND     -> "and";
            case OR      -> "or";
            case NOT     -> "not";
            case IN      -> "in";
            case IS      -> "is";
        };
    }
}
