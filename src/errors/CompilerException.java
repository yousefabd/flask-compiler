package errors;

/**
 * Base class for every recoverable failure inside the compiler pipeline.
 *
 * <p>Stages throw a subclass ({@link ParseError}, {@link SemanticError},
 * {@link CodeGenError}) instead of letting raw exceptions escape; the pipeline
 * driver catches {@code CompilerException} at one place, reports it through
 * {@link ErrorReporter} and aborts the current compilation without crashing
 * the application.</p>
 */
public abstract class CompilerException extends RuntimeException {

    private final CompilerStage stage;
    private final String file;   // source file the error belongs to, may be null
    private final int line;      // 1-based source line, or -1 when unknown

    protected CompilerException(CompilerStage stage, String file, int line, String message) {
        super(message);
        this.stage = stage;
        this.file = file;
        this.line = line;
    }

    protected CompilerException(CompilerStage stage, String file, int line, String message, Throwable cause) {
        super(message, cause);
        this.stage = stage;
        this.file = file;
        this.line = line;
    }

    public CompilerStage getStage() { return stage; }
    public String getFile()         { return file; }
    public int getLine()            { return line; }

    /** Converts this exception into a reportable problem entry. */
    public CompilerProblem toProblem() {
        return new CompilerProblem(stage, stage.name(), file, line, getMessage());
    }
}
