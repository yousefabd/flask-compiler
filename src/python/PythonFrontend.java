package python;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;
import errors.CompilerStage;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import python.models.root.Program;
import python.symbol_table.CompilerError;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.symbol_table.semantic_rules.FlaskRouteRule;
import python.symbol_table.semantic_rules.ISemanticRule;
import python.symbol_table.semantic_rules.TypeCheckerRule;
import python.visitor.PythonVisitor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import utils.CompilerUtils;

public class PythonFrontend {

    /**
     * Names Python provides for free. Without these, every {@code print(...)} would
     * be reported as an UndefinedError. Mirrors {@code TemplateFrontend.TEMPLATE_BUILTINS}.
     */
    private static final List<String> PYTHON_BUILTINS =
            List.of(
                    "print", "len", "range", "str", "int", "float", "bool",
                    "list", "dict", "set", "tuple", "sum", "min", "max", "abs",
                    "round", "sorted", "reversed", "enumerate", "zip", "map",
                    "filter", "any", "all", "type", "isinstance", "hasattr",
                    "getattr", "setattr", "open", "input", "format", "repr",
                    "id", "next", "iter", "super", "object",
                    "Exception", "ValueError", "TypeError", "KeyError",
                    "IndexError", "AttributeError", "RuntimeError",
                    "__name__", "__file__", "__doc__",
                    // The lexer spells these literals lowercase (TRUE: 'true'), so
                    // Python's real `True`/`False` arrive here as identifiers. Until the
                    // grammar is regenerated they are covered as builtin names, which
                    // keeps them out of the report either way.
                    "True", "False"
            );

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

    /**
     * Runs the Python semantic analysis and hands every error it finds to the
     * shared {@link ErrorReporter}.
     *
     * <p>The analysis never aborts the run: whatever it collected before an unexpected
     * failure is still reported, and the failure itself is reported as an internal
     * problem instead of escaping the front end. The symbol table is returned either
     * way, so callers always get something usable.</p>
     */
    public SymbolTable analyzePython(Program program) {

        List<CompilerError> errors =
                new ArrayList<>();

        SymbolTable symbolTable = new SymbolTable();

        defineBuiltins(symbolTable);

        List<ISemanticRule> rules = List.of(
                new TypeCheckerRule(),
                new FlaskRouteRule()
        );

        SymbolTableBuilder builder =
                new SymbolTableBuilder(symbolTable, errors, rules);

        try {
            builder.build(program);
        } catch (RuntimeException unexpected) {
            // added: a bug in one check must not take the whole compilation down —
            // report it like any other problem and keep the errors found so far.
            reporter.reportUnexpected(
                    CompilerStage.SEMANTIC_ANALYSIS,
                    appSource.toString(),
                    unexpected);
        }

        for (python.symbol_table.CompilerError error : errors) {
            reporter.report(appSource.toString(), error);
        }

        return symbolTable;
    }

    private static void defineBuiltins(SymbolTable symbolTable) {
        for (String builtin : PYTHON_BUILTINS) {
            symbolTable.define(
                    new Symbol(builtin, SymbolKind.BUILTIN, 0)
            );
        }
    }
}
