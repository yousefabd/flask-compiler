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
    // added: the same two structured fields python.symbol_table.CompilerError
    // carries, so a template problem and a Python problem report identically.
    private final String context;     // scope the error was found in, may be null
    private final String symbolName;  // variable/macro/block name, may be null

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
