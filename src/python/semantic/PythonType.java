package python.semantic;

/**
 * Statically known type of a Python expression.
 *
 * <p>Mirrors {@code jinja2.symbol_table.SymbolType}. {@link #ANY} is the
 * "unknown at compile time" value: whenever a type cannot be proven, the
 * checker yields ANY and every check involving it is skipped. Types are never
 * guessed just to be able to produce an error.</p>
 */
public enum PythonType {
    INT,
    FLOAT,
    STRING,
    BOOL,
    NONE,
    LIST,
    DICT,
    SET,
    CALLABLE,
    ANY;

    public boolean isNumeric() {
        return this == INT || this == FLOAT || this == BOOL;
    }

    public boolean isKnown() {
        return this != ANY;
    }

    /** Lowercase spelling used in messages, e.g. {@code int}, {@code str}. */
    public String display() {
        return switch (this) {
            case INT -> "int";
            case FLOAT -> "float";
            case STRING -> "str";
            case BOOL -> "bool";
            case NONE -> "None";
            case LIST -> "list";
            case DICT -> "dict";
            case SET -> "set";
            case CALLABLE -> "callable";
            case ANY -> "unknown";
        };
    }

    /** Maps an annotation/builtin constructor name onto a type, or null. */
    public static PythonType fromTypeName(String name) {
        if (name == null) return null;
        return switch (name) {
            case "int" -> INT;
            case "float" -> FLOAT;
            case "str" -> STRING;
            case "bool" -> BOOL;
            case "list" -> LIST;
            case "dict" -> DICT;
            case "set" -> SET;
            default -> null;
        };
    }
}
