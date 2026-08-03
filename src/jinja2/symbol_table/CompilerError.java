package jinja2.symbol_table;
public final class CompilerError {

    // added: display names, so the report names a template problem the same way it
    // names the equivalent Python one — the file path is what tells them apart.
    public enum Kind {
        UNDEFINED_VARIABLE     ("UndefinedError"),
        SCOPE                  ("ScopeError"),
        DUPLICATE_VARIABLE     ("DuplicateDeclarationError"),
        DUPLICATE_MACRO        ("DuplicateMacroError"),
        DUPLICATE_PARAMETER    ("DuplicateParameterError"),
        DUPLICATE_BLOCK        ("DuplicateBlockError"),
        INVALID_HTML_STRUCTURE ("InvalidHtmlStructureError"),
        TYPE_ERROR             ("TypeError"),
        TYPE_MISMATCH          ("TypeMismatchError");

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
        return "[" + kind.errorName() + "] line " + line + ": " + message;
    }
}
