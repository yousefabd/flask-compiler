package jinja2.symbol_table;

/**
 * Lightweight value types used by the type checker to validate
 * {% set %} assignments and expression operators.
 *
 * UNKNOWN is the "don't know" type — used for anything the analyzer
 * can't statically determine (function calls, filters, property/index
 * access, macro parameters, undefined identifiers, ...). It is always
 * treated as compatible with every other type so the checker never
 * reports a false positive when it simply lacks information.
 */
public enum Type {
    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    LIST,
    DICTIONARY,
    NONE,
    UNKNOWN;

    /** Human-readable name used inside error messages. */
    public String label() {
        return switch (this) {
            case INTEGER    -> "int";
            case FLOAT      -> "float";
            case STRING     -> "string";
            case BOOLEAN    -> "boolean";
            case LIST       -> "list";
            case DICTIONARY -> "dict";
            case NONE       -> "none";
            case UNKNOWN    -> "unknown";
        };
    }
}
