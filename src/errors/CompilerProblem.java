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

    public CompilerProblem(CompilerStage stage, String kind, String file, int line, String message) {
        this.stage = stage;
        this.kind = kind;
        this.file = file;
        this.line = line;
        this.message = message;
    }

    public CompilerStage getStage() { return stage; }
    public String getKind()         { return kind; }
    public String getFile()         { return file; }
    public int getLine()            { return line; }
    public String getMessage()      { return message; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(kind).append(']');
        if (file != null) sb.append(' ').append(file);
        if (line >= 0) sb.append(" line ").append(line);
        sb.append(": ").append(message);
        return sb.toString();
    }
}
