import antlr.jinja2.Jinja2Lexer;
import antlr.jinja2.Jinja2Parser;
import jinja2.models.root.Template;
import jinja2.visitor.Jinja2Visitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;
import python.models.root.Program;
import python.printer.ASTPrinter;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.visitor.PythonVisitor;
import java.io.IOException; 


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void python() throws IOException
    {
         // 1. اقرأ الملف أو النص
        CharStream input = CharStreams.fromFileName("tests/app.py");
        // CharStream input = CharStreams.fromString("a = 5 + 3");

        // 2. Lexer
        PythonLexer lexer = new PythonLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 3. Parser
        PythonParser parser = new PythonParser(tokens);
        // System.out.println("Parser class = " + parser.getClass().getName());

        ParseTree tree = parser.prog();

         // 4. استدعاء نقطة البداية في النحو
        // PythonParser.ProgContext tree = parser.prog();~

        // 5. Visitor لبناء الـ AST
        PythonVisitor visitor = new PythonVisitor();
        Program prog = (Program) visitor.visit(tree);

        

        // System.out.println("AST:\n" + prog.toString());
        ASTPrinter printer = new ASTPrinter();
        System.out.println("AST:");
        printer.printTree(prog);

        SymbolTable symtab = new SymbolTable();
        SymbolTableBuilder stb = new SymbolTableBuilder(symtab);

        stb.build(prog);   
        System.out.println(symtab);


    }

    public static void jinja() throws IOException {
        // اقرأ الملف اللي فيه الكود
        CharStream input = CharStreams.fromFileName("tests/templates/index.html");

        // مرر النص للـ lexer
        Jinja2Lexer lexer = new Jinja2Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // مرر التوكنات للـ parser
        Jinja2Parser parser = new Jinja2Parser(tokens);

        // استدعي القاعدة العليا (مثلاً template)
        ParseTree tree = parser.template();

        // اطبع الشجرة
        // System.out.println(tree.toStringTree(parser));

        Jinja2Visitor visitor = new Jinja2Visitor();
        Template template = (Template) visitor.visit(tree);



        // System.out.println("AST:\n" + template.toString());
        jinja2.printer.ASTPrinter printer = new jinja2.printer.ASTPrinter();
        System.out.println("AST:");
        printer.printTree(template);

    }

    public static void main(String[] args) throws IOException 
    {
        //python();
        jinja();
    }
}