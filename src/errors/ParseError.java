package errors;

/** Thrown when lexing/parsing a source file fails (syntax errors). */
public class ParseError extends CompilerException {

    public ParseError(String file, int line, String message) {
        super(CompilerStage.PARSING, file, line, message);
    }

    public ParseError(String file, String message, Throwable cause) {
        super(CompilerStage.PARSING, file, -1, message, cause);
    }
}
