package python.symbol_table;

/**
 * Semantic error found in the Python/Flask source program.
 * Mirrors {@code jinja2.symbol_table.CompilerError} so both pipelines report
 * errors the same way.
 */
public final class CompilerError {

    public enum Kind {
        DUPLICATE_FUNCTION,
        DUPLICATE_PARAMETER,
        RETURN_OUTSIDE_FUNCTION,
        BREAK_OUTSIDE_LOOP,
        CONTINUE_OUTSIDE_LOOP,
        GLOBAL_AT_MODULE_LEVEL,

        /** Name cannot be resolved through the visible Python scope chain. */
        UNDEFINED_VARIABLE,
        /** Name exists, but only in a scope that is not reachable from here. */
        SCOPE,
        /** Name is declared in this scope but read before it is assigned. */
        USE_BEFORE_ASSIGNMENT,
        /** Operand types are statically known and the operation is invalid. */
        TYPE_ERROR,
        /** A value contradicts an explicit type expectation (a parameter annotation). */
        TYPE_MISMATCH,
        /** A Jinja template needs a variable that no render_template() call supplies. */
        MISSING_FLASK_VARIABLE,
    }

    private final Kind   kind;
    private final String message;
    private final int    line;

    public CompilerError(Kind kind, String message, int line) {
        this.kind    = kind;
        this.message = message;
        this.line    = line;
    }

    public Kind   getKind()    { return kind; }
    public String getMessage() { return message; }
    public int    getLine()    { return line; }

    @Override
    public String toString() {
        return "[" + kind + "] line " + line + ": " + message;
    }
}
