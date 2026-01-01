// import antlr.html.HTMLLexer;
// import antlr.html.HTMLParser;
// import html.AntlrToHTMLDocument;
// import html.SymbolTable.*;
// import html.SymbolTable.SemanticRules.*;
// import org.antlr.v4.runtime.CharStream;
// import org.antlr.v4.runtime.CharStreams;
// import org.antlr.v4.runtime.CommonTokenStream;

// import java.io.IOException;
// import java.util.List;

// public class HTMLApp {
//     public static void html(String[] args) {
//         if(args.length != 1) {
//             System.err.println("Usage: java HTMLApp <input-file-path>");
//             System.exit(1);
//         }
//         String inputFilePath = args[0];
//         HTMLParser parser = createParser(inputFilePath);
//         var tree = parser.htmlDocument();
//         AntlrToHTMLDocument visitor = new AntlrToHTMLDocument();
//         var document = visitor.visitHtmlDocument(tree);
//         if (!visitor.sematicErrors.isEmpty()) {
//             System.out.println("Semantic Errors:");
//             visitor.sematicErrors.forEach(System.out::println);
//             System.exit(1);
//         }
//         var errors = visitor.sematicErrors;
//         SymbolTable table = new SymbolTable();
//         new SymbolTableBuilder(table,errors).build(document.root());

//         List<ISemanticRule> rules = List.of(
//                 new UlLiRule(),
//                 new BrokenReferenceRule(table)
//         );
//         for (ISemanticRule rule : rules) {
//             rule.validate(document.root(), errors);
//         }
//         if(!errors.isEmpty()) {
//             System.out.println("Semantic Errors:");
//             visitor.sematicErrors.forEach(System.out::println);
//             System.exit(1);
//         }
//         //print the document
//         System.out.println(visitor.document.root().toString());
//     }
//     public static HTMLParser createParser(String inputFilePath) {
//         try {
//             CharStream input = CharStreams.fromFileName(inputFilePath);
//             HTMLLexer lexer = new HTMLLexer(input);
//             var tokens = new CommonTokenStream(lexer);
//             return new HTMLParser(tokens);
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }
//     }
// }
