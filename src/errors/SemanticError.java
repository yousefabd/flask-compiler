package errors;

/** Thrown when semantic analysis rejects an otherwise well-formed program. */
public class SemanticError extends CompilerException {

    public SemanticError(String file, int line, String message) {
        super(CompilerStage.SEMANTIC_ANALYSIS, file, line, message);
    }
}
