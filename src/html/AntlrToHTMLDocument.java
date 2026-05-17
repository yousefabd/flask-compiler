//package html;
//
//import html.models.NormalElementNode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AntlrToHTMLDocument extends HTMLParserBaseVisitor<HTMLDocument> {
//    public List<String> sematicErrors = new ArrayList<>();
//    public HTMLDocument document;
//
//    @Override
//    public HTMLDocument visitHtmlDocument(HTMLParser.HtmlDocumentContext ctx) {
//        if(ctx.element().size() > 1){
//            sematicErrors.add("HTML Document can only have one root element.");
//            return null;
//        }
//        AntlrToHTMLNode nodeVisitor = new AntlrToHTMLNode();
//        var root = nodeVisitor.visitElement((ctx.element(0)));
//        if(! (root instanceof NormalElementNode)){
//            sematicErrors.add("HTML root should only be a normal element.");
//            return null;
//        }
//        document = new HTMLDocument((NormalElementNode) root);
//        return document;
//    }
//}
