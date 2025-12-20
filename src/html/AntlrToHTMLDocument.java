package html;

import antlr.html.HTMLParser;
import antlr.html.HTMLParserBaseVisitor;
import html.models.ElementNode;

import java.util.ArrayList;
import java.util.List;

public class AntlrToHTMLDocument extends HTMLParserBaseVisitor<HTMLDocument> {
    public List<String> sematicErrors = new ArrayList<>();
    public HTMLDocument document;

    @Override
    public HTMLDocument visitHtmlDocument(HTMLParser.HtmlDocumentContext ctx) {
        if(ctx.element().size() > 1){
            sematicErrors.add("HTML Document can only have one root element.");
            return null;
        } else {
            AntlrToHTMLNode nodeVisitor = new AntlrToHTMLNode();
            ElementNode root = (ElementNode) nodeVisitor.visitElement(ctx.element(0));
            document = new HTMLDocument(root);
            return document;
        }
    }
}
