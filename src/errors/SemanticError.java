package errors;

/** Thrown when semantic analysis rejects an otherwise well-formed program. */
public class SemanticError extends CompilerException {
    private final String kind;

    public SemanticError(String file, int line, String message) {
        this(file, line, "SEMANTIC_ERROR", message);
    }

    public SemanticError(String file, int line, String kind, String message) {
        super(CompilerStage.SEMANTIC_ANALYSIS, file, line, message);
        this.kind = kind;
    }

    @Override
    public CompilerProblem toProblem() {
        return new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS,
                kind,
                getFile(),
                getLine(),
                getMessage()
        );
    }
}
