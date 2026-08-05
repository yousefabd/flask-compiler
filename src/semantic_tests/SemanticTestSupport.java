package semantic_tests;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;
import errors.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import python.models.root.Program;
import python.semantic.PythonSemanticAnalyzer;
import python.symbol_table.CompilerError;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.visitor.PythonVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Parsing and analysis helpers shared by the semantic test cases. */
public final class SemanticTestSupport {

    private SemanticTestSupport() {
    }

    /** Parses Python source text; throws when the source does not parse. */
    public static Program parse(String source) {
        String text = source.endsWith("\n") ? source : source + "\n";
        return parse(CharStreams.fromString(text), "<test>");
    }

    public static Program parseFile(Path file) {
        try {
            return parse(CharStreams.fromPath(file), file.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + file, e);
        }
    }

    private static Program parse(CharStream input, String name) {
        SyntaxErrorListener listener = new SyntaxErrorListener(name);

        PythonLexer lexer = new PythonLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        ParseTree tree = parser.prog();

        if (listener.hasErrors()) {
            StringBuilder sb = new StringBuilder("Source did not parse:");
            for (errors.CompilerProblem problem : listener.getErrors())
                sb.append("\n    ").append(problem);
            throw new IllegalStateException(sb.toString());
        }

        return (Program) new PythonVisitor().visit(tree);
    }

    /** Name resolution + type checking only. */
    public static List<CompilerError> analyze(String source) {
        return new PythonSemanticAnalyzer().analyze(parse(source)).errors();
    }

    public static List<CompilerError> analyze(Program program) {
        return new PythonSemanticAnalyzer().analyze(program).errors();
    }

    /**
     * Everything the pipeline reports for a Python file: the declaration-level
     * checks from the symbol-table builder plus name resolution and types.
     */
    public static List<CompilerError> analyzeAll(String source) {
        return analyzeAll(parse(source));
    }

    public static List<CompilerError> analyzeAll(Program program) {
        List<CompilerError> errors = new ArrayList<>();
        new SymbolTableBuilder(new SymbolTable(), errors).build(program);
        errors.addAll(analyze(program));
        return errors;
    }

    /** Writes {@code content} to {@code path}, creating parent directories. */
    public static void write(Path path, String content) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write " + path, e);
        }
    }

    public static void deleteRecursively(Path root) {
        if (root == null || !Files.exists(root)) return;
        try (var paths = Files.walk(root)) {
            paths.sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                    // a leftover temp file is not a test failure
                }
            });
        } catch (IOException ignored) {
            // ditto
        }
    }

    /**
     * Runs {@code body} with {@code System.out} redirected, and returns
     * everything it printed. The compiler's only output channel is
     * {@code System.out} (see {@code errors.ErrorReporter#printReport}), so
     * this is how a test observes the integrated pipeline's full report
     * without duplicating its formatting logic. Restores the original
     * {@code System.out} even if {@code body} throws. Test suite runs
     * single-threaded, so redirecting the process-wide stream is safe here.
     */
    public static String captureStdout(Runnable body) {
        java.io.PrintStream original = System.out;
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        try {
            System.setOut(new java.io.PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8));
            body.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
    }
}
