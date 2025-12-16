import antlr.python.pylexer;
import antlr.python.pyparser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        // اقرأ ملف بايثون
        CharStream input = CharStreams.fromFileName("test.py");

        // مرره على الـ Lexer
        pylexer lexer = new pylexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

//        lexer.getAllTokens().forEach(t -> {
//            System.out.printf("Token %-15s Text='%s'%n",
//                    pyparser.VOCABULARY.getSymbolicName(t.getType()),
//                    t.getText().replace("\n","\\n"));
//        });


        // مرره على الـ Parser
        pyparser parser = new pyparser(tokens);

        // استدعِ القاعدة العليا (مثلاً file_input)
        ParseTree tree = parser.prog();

        // اطبع الـ Parse Tree
        System.out.println(tree.toStringTree(parser));

    }
}