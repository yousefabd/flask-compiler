package python.symbol_table;

/**
 * Statically inferred type of a Python symbol or expression.
 *
 * <p>Mirrors {@code jinja2.symbol_table.SymbolType}, but keeps Python's own
 * type names so error messages read like Python ("expected int, got str").
 * {@link #ANY} means "only known at runtime" and never produces an error —
 * the checkers stay silent instead of guessing.</p>
 */
public enum SymbolType {
    INT      ("int"),
    FLOAT    ("float"),
    STRING   ("str"),
    BOOLEAN  ("bool"),
    NONE     ("None"),
    LIST     ("list"),
    DICT     ("dict"),
    SET      ("set"),
    CALLABLE ("function"),
    ANY      ("Any");

    private final String displayName;

    SymbolType(String displayName) {
        this.displayName = displayName;
    }

    public boolean isNumeric() {
        return this == INT || this == FLOAT || this == BOOLEAN;
    }

    public boolean isKnown() {
        return this != ANY;
    }

    /** Maps a Python type annotation ({@code def f(a: int)}) to a SymbolType. */
    public static SymbolType fromAnnotation(String annotation) {
        if (annotation == null) return ANY;
        return switch (annotation) {
            case "int"        -> INT;
            case "float"      -> FLOAT;
            case "str"        -> STRING;
            case "bool"       -> BOOLEAN;
            case "list"       -> LIST;
            case "dict"       -> DICT;
            case "set"        -> SET;
            default           -> ANY;
        };
    }

    @Override
    public String toString() { return displayName; }
}
