package python;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import python.models.root.Program;
import python.semantic.PythonSemanticAnalyzer;
import python.semantic.PythonSemanticResult;
import python.visitor.PythonVisitor;

import java.nio.file.Path;
import utils.CompilerUtils;

public class PythonFrontend {
    private final Path appSource;
    private final ErrorReporter reporter;

    public PythonFrontend(Path appSource, ErrorReporter reporter) {
        this.appSource = appSource;
        this.reporter = reporter;
    }
    public Program parsePython(){
        CharStream input = CompilerUtils.readSource(appSource);
        SyntaxErrorListener listener = new SyntaxErrorListener(appSource.toString());

        PythonLexer lexer = new PythonLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        ParseTree tree = parser.prog();
        if (listener.hasErrors()) {
            for (errors.CompilerProblem p : listener.getErrors())
                reporter.report(p);
            return null;    // do not build an AST from a broken parse tree
        }

        return (Program) new PythonVisitor().visit(tree);
    }
    public PythonSemanticResult analyzePython(Program program) {
        PythonSemanticResult result =
                new PythonSemanticAnalyzer(appSource.toString()).analyze(program);

        for (var diagnostic : result.diagnostics()) {
            reporter.report(diagnostic);
        }
        return result;
    }
}
