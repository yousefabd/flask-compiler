package html;

import antlr.html.HTMLParser;
import antlr.html.HTMLParserBaseVisitor;
import html.models.*;

public class AntlrToHTMLNode extends HTMLParserBaseVisitor<Node> {


    @Override
    public VoidElementNode visitVoidElement(HTMLParser.VoidElementContext ctx) {
        String tagName = ctx.TAG_ACCEPTED_NAME().getText();
        int line = ctx.TAG_ACCEPTED_NAME().getSymbol().getLine();

        var element =  new VoidElementNode(tagName, line);
        for (var attribute : ctx.attribute()) {
            element.addAttribute(visitAttribute(attribute));
        }
        return element;
    }

    @Override
    public NormalElementNode visitNormalElement(HTMLParser.NormalElementContext ctx) {
        var tagToken = ctx.beginTag().TAG_ACCEPTED_NAME().getSymbol();
        String tagName = tagToken.getText();
        int line = tagToken.getLine();

        NormalElementNode elementNode = new NormalElementNode(tagName, line);
        for (var attribute : ctx.beginTag().attribute()) {
            elementNode.addAttribute(visitAttribute(attribute));
        }
        for (var element : ctx.element()) {
            elementNode.addChild(visitElement(element));
        }
        return elementNode;
    }

    @Override
    public AttributeNode visitAttribute(HTMLParser.AttributeContext ctx) {
        String name = ctx.CHAR_NAME().getText();
        int line = ctx.CHAR_NAME().getSymbol().getLine();
        String value = null;
        if (ctx.ATTVALUE_VALUE() != null) {
            value = ctx.ATTVALUE_VALUE().getText();
        }
        return new AttributeNode(name, value, line);
    }

    @Override
    public ElementNode visitElement(HTMLParser.ElementContext ctx) {
        if (ctx.normalElement() != null) {
            return visitNormalElement(ctx.normalElement());
        } else if (ctx.voidElement() != null) {
            return visitVoidElement(ctx.voidElement());
        }
        String text = ctx.TEXT().getText();
        if(text.trim().isEmpty()){
            return null;
        }
        int line = ctx.TEXT().getSymbol().getLine();
        text = text.trim().replaceAll("[\\t\\n\\r]+", " ");
        return new TextNode(text, line);
    }

}
