// cSpell: disable

import antlr.html.HTMLLexer;
import antlr.html.HTMLParser;
import jinja2.models.TemplateNode;
import jinja2.visitor.AntlrToTemplateAstVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.python.PythonLexer;
import antlr.python.PythonParser;

import antlr.jinja2.Jinja2Lexer;
import antlr.jinja2.Jinja2Parser;

import antlr.css.CSSLexer;
import antlr.css.CSSParser;

import css.AntlrToStyleSheet;
import css.models.Stylesheet;

//import jinja2.models.root.Template;

import python.models.root.Program;
import python.printer.ASTPrinter;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.visitor.PythonVisitor;

import java.io.IOException; 


public class Main {

    public static void html() throws IOException
    {
       // HTMLApp.html("tests/templates/index.html");
    }

    public static void css() throws IOException
    {
        CharStream input = CharStreams.fromFileName("tests/static/styles.css");
        CSSLexer lexer = new CSSLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSSParser parser = new CSSParser(tokens);

        // return the context of the stylesheet
        var tree = parser.stylesheet();
        
        // build the AST for css
        AntlrToStyleSheet visitor = new AntlrToStyleSheet();
        Stylesheet stylesheet = visitor.visitStylesheet(tree);

        // print the AST
        css.ASTPrinter.print(stylesheet);
    }

    public static void python() throws IOException
    {
        CharStream input = CharStreams.fromFileName("tests/app.py");
        PythonLexer lexer = new PythonLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonParser parser = new PythonParser(tokens);

        // return the context of the prog
        ParseTree tree = parser.prog();

        // build the AST
        PythonVisitor visitor = new PythonVisitor();
        Program prog = (Program) visitor.visit(tree);
        
        // print the AST for python
        // System.out.println("AST:\n" + prog.toString());
        ASTPrinter printer = new ASTPrinter();
        System.out.println("AST:");
        printer.printTree(prog);

        // build the symbol table
        SymbolTable symtab = new SymbolTable();
        SymbolTableBuilder stb = new SymbolTableBuilder(symtab);
        stb.build(prog);
    
        // print the symbol table
        System.out.println(symtab);
    }

    public static void jinja() throws IOException 
    {

        CharStream input = CharStreams.fromFileName("tests/templates/index2.html");
        HTMLLexer lexer = new HTMLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HTMLParser parser = new HTMLParser(tokens);

        // return the context of template
        ParseTree tree = parser.template();
    
        // build the ast now
        AntlrToTemplateAstVisitor visitor = new AntlrToTemplateAstVisitor();
        TemplateNode template = (TemplateNode) visitor.visit(tree);

        // print the ast for jinja2
        jinja2.printer.ASTPrinter printer = new jinja2.printer.ASTPrinter();
        System.out.println("AST:");
        printer.printTree(template);

    }

    public static void main(String[] args) throws IOException 
    {
        //python();
        jinja();
        //css();
    }
}