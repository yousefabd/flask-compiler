import antlr.css.CSSLexer;
import antlr.css.CSSParser;
import css.ASTPrinter;
import css.AntlrToStyleSheet;
import css.models.Stylesheet;
import html.SymbolTable.*;
import html.SymbolTable.SemanticRules.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class CSSApp {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java CSSApp <input-file-path>");
            System.exit(1);
        }

        String inputFilePath = args[0];

        CSSParser parser = createParser(inputFilePath);

        // 1. Parse
        var tree = parser.stylesheet();

        // 2. Build AST
        AntlrToStyleSheet visitor = new AntlrToStyleSheet();
        Stylesheet stylesheet = visitor.visitStylesheet(tree);

        // 3. Print AST
        ASTPrinter.print(stylesheet);
    }

    private static CSSParser createParser(String inputFilePath) {
        try {
            CharStream input = CharStreams.fromFileName(inputFilePath);
            CSSLexer lexer = new CSSLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            return new CSSParser(tokens);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}