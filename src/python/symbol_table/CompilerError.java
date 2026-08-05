package python.symbol_table;

/**
 * Semantic error found in the Python/Flask source program.
 * Mirrors {@code jinja2.symbol_table.CompilerError} so both pipelines report
 * errors the same way.
 */
public final class CompilerError {

    public enum Kind {
        /** `def f(x, x)` — a real CPython SyntaxError. */
        DUPLICATE_PARAMETER,
        /** `return` outside a function body — a real CPython SyntaxError. */
        RETURN_OUTSIDE_FUNCTION,
        /** `break` outside a loop — a real CPython SyntaxError. */
        BREAK_OUTSIDE_LOOP,
        /** `continue` outside a loop — a real CPython SyntaxError. */
        CONTINUE_OUTSIDE_LOOP,

        /** Name cannot be resolved through the visible Python scope chain. */
        UNDEFINED_VARIABLE,
        /** Name exists, but only in a scope that is not reachable from here. */
        SCOPE,
        /** Operand types are statically known and the operation is invalid. */
        TYPE_ERROR,
        /** A value contradicts an explicit type expectation (an annotation). */
        TYPE_MISMATCH,
        /** A Jinja template needs a variable that no render_template() call supplies. */
        MISSING_FLASK_VARIABLE,

        // Deliberately removed after adversarial review — kept here as a record:
        //   DUPLICATE_FUNCTION      — redefining a function is legal Python
        //                             (ordinary name rebinding); CPython raises
        //                             nothing for it.
        //   GLOBAL_AT_MODULE_LEVEL  — `global x` at module level compiles and
        //                             runs; it is redundant, not an error.
        //   USE_BEFORE_ASSIGNMENT   — required sound control-flow analysis to
        //                             report correctly (which branch/iteration
        //                             a read falls on). The conservative
        //                             version produced both false positives
        //                             (loop bodies that assign before reading
        //                             on the first iteration) and false
        //                             negatives (a name assigned only in one
        //                             `if` branch). Removed rather than left
        //                             half-right; see NameResolver's class
        //                             comment.
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
