package jinja2.symbol_table;
public final class CompilerError {

    public enum Kind {
        UNDEFINED_VARIABLE,
        SCOPE,
        DUPLICATE_VARIABLE,
        DUPLICATE_MACRO,
        DUPLICATE_PARAMETER,
        DUPLICATE_BLOCK,
        INVALID_HTML_STRUCTURE,
        TYPE_ERROR,
        TYPE_MISMATCH,
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
