package errors;

/**
 * One reported error, normalized so that syntax, semantic and generation
 * errors from every sub-pipeline (Python, Jinja2/HTML) share a single shape.
 *
 * <p>Printed in the same {@code [KIND] line N: message} format the existing
 * Jinja2 semantic-error display already uses.</p>
 */
public final class CompilerProblem {

    private final CompilerStage stage;
    private final String kind;    // e.g. SYNTAX, UNDEFINED_VARIABLE, DUPLICATE_FUNCTION
    private final String file;    // may be null
    private final int line;       // -1 when unknown
    private final String message;
    // added: the scope/context the problem was found in ("function index", "global"),
    // so a reader can tell two same-named variables apart. Null when not applicable.
    private final String context;

    public CompilerProblem(CompilerStage stage, String kind, String file, int line, String message) {
        this(stage, kind, file, line, message, null);
    }

    public CompilerProblem(CompilerStage stage, String kind, String file, int line,
                           String message, String context) {
        this.stage = stage;
        this.kind = kind;
        this.file = file;
        this.line = line;
        this.message = message;
        this.context = context;
    }

    public CompilerStage getStage() { return stage; }
    public String getKind()         { return kind; }
    public String getFile()         { return file; }
    public int getLine()            { return line; }
    public String getMessage()      { return message; }
    public String getContext()      { return context; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(kind).append(']');
        if (file != null) sb.append(' ').append(file);
        if (line >= 0) sb.append(" line ").append(line);
        sb.append(": ").append(message);
        if (context != null) sb.append(" (in ").append(context).append(')');
        return sb.toString();
    }
}
