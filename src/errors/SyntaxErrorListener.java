package errors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * ANTLR error listener that collects syntax errors instead of letting the
 * default ConsoleErrorListener print them and letting the parser continue
 * with a broken tree.
 *
 * <p>Attach to both the lexer and the parser (after removing the default
 * listeners), then call {@link #throwIfErrors()} once parsing finished.</p>
 */
public class SyntaxErrorListener extends BaseErrorListener {

    private final String file;
    private final List<CompilerProblem> syntaxErrors = new ArrayList<>();

    public SyntaxErrorListener(String file) {
        this.file = file;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        syntaxErrors.add(new CompilerProblem(
                CompilerStage.PARSING, "SYNTAX", file, line,
                msg + " (column " + charPositionInLine + ")"));
    }

    public boolean hasErrors() {
        return !syntaxErrors.isEmpty();
    }

    public List<CompilerProblem> getErrors() {
        return syntaxErrors;
    }

    /** Aborts the pipeline with a {@link ParseError} if any syntax error was seen. */
    public void throwIfErrors() {
        if (syntaxErrors.isEmpty()) return;
        CompilerProblem first = syntaxErrors.get(0);
        throw new ParseError(file, first.getLine(),
                syntaxErrors.size() + " syntax error(s), first: " + first.getMessage());
    }
}
