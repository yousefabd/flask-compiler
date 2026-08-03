package python.symbol_table;

/**
 * Semantic error found in the Python/Flask source program.
 * Mirrors {@code jinja2.symbol_table.CompilerError} so both pipelines report
 * errors the same way.
 *
 * <p>Each kind carries a display name ({@code UndefinedError}, {@code ScopeError},
 * ...) that {@link errors.ErrorReporter} prints as the problem kind, so the
 * final report reads
 * {@code [UndefinedError] tests\app.py line 4: variable 'x' is not defined}.</p>
 */
public final class CompilerError {

    public enum Kind {
        // ── the core checks ──────────────────────────────────────
        UNDEFINED_VARIABLE      ("UndefinedError"),
        USE_BEFORE_DECLARATION  ("NameError"),
        SCOPE                   ("ScopeError"),
        TYPE_ERROR              ("TypeError"),
        TYPE_MISMATCH           ("TypeMismatchError"),
        MISSING_FLASK_VARIABLE  ("MissingFlaskVariableError"),

        // ── duplicate declarations ───────────────────────────────
        DUPLICATE_VARIABLE      ("DuplicateDeclarationError"),
        DUPLICATE_FUNCTION      ("DuplicateFunctionError"),
        DUPLICATE_PARAMETER     ("DuplicateParameterError"),
        DUPLICATE_ROUTE         ("DuplicateRouteError"),

        // ── call shape ───────────────────────────────────────────
        ARGUMENT_COUNT          ("ArgumentCountError"),

        // ── statement placement ──────────────────────────────────
        RETURN_OUTSIDE_FUNCTION ("ReturnOutsideFunctionError"),
        BREAK_OUTSIDE_LOOP      ("BreakOutsideLoopError"),
        CONTINUE_OUTSIDE_LOOP   ("ContinueOutsideLoopError"),
        GLOBAL_AT_MODULE_LEVEL  ("GlobalScopeError");

        private final String errorName;

        Kind(String errorName) {
            this.errorName = errorName;
        }

        /** Human-readable error name used in the compiler report. */
        public String errorName() { return errorName; }
    }

    private final Kind   kind;
    private final String message;
    private final int    line;
    private final String context;     // scope the error was found in, may be null
    private final String symbolName;  // variable/function/template name, may be null

    public CompilerError(Kind kind, String message, int line) {
        this(kind, message, line, null, null);
    }

    public CompilerError(Kind kind, String message, int line,
                         String context, String symbolName) {
        this.kind       = kind;
        this.message    = message;
        this.line       = line;
        this.context    = context;
        this.symbolName = symbolName;
    }

    public Kind   getKind()       { return kind; }
    public String getMessage()    { return message; }
    public int    getLine()       { return line; }
    public String getContext()    { return context; }
    public String getSymbolName() { return symbolName; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(kind.errorName()).append("] line ")
          .append(line).append(": ").append(message);
        if (context != null) sb.append(" (in ").append(context).append(')');
        return sb.toString();
    }
}
