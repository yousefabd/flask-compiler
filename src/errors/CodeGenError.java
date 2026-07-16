package errors;

/**
 * Thrown when the code generator cannot translate an AST node — an unknown
 * node type, a missing operator mapping, or an I/O failure while writing the
 * generated files.
 */
public class CodeGenError extends CompilerException {

    public CodeGenError(String file, int line, String message) {
        super(CompilerStage.CODE_GENERATION, file, line, message);
    }

    public CodeGenError(String file, String message, Throwable cause) {
        super(CompilerStage.CODE_GENERATION, file, -1, message, cause);
    }
}
