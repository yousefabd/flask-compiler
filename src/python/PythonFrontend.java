package python;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import python.models.root.Program;
import python.symbol_table.CompilerError;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.visitor.PythonVisitor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    public SymbolTable analyzePython(Program program) {

        List<CompilerError> errors =
                new ArrayList<>();

        SymbolTable symbolTable = new SymbolTable();

        SymbolTableBuilder builder =
                new SymbolTableBuilder(symbolTable, errors);

        builder.build(program);

        for (python.symbol_table.CompilerError error : errors) {
            reporter.report(appSource.toString(), error);
        }

        return symbolTable;
    }
}
