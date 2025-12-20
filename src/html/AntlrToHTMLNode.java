package html;

import antlr.html.HTMLParser;
import antlr.html.HTMLParserBaseVisitor;
import html.models.AttributeNode;
import html.models.ElementNode;
import html.models.Node;
import html.models.TextNode;

import java.util.ArrayList;
import java.util.List;

public class AntlrToHTMLNode extends HTMLParserBaseVisitor<Node> {


    @Override
    public Node visitVoidElement(HTMLParser.VoidElementContext ctx) {
        String tagName = ctx.TAG_NAME().getText();
        int line = ctx.TAG_NAME().getSymbol().getLine();

        var element =  new ElementNode(tagName, ElementNode.Type.Void, line);
        for (var attribute : ctx.attribute()) {
            element.AddAttribute(visitAttribute(attribute));
        }
        return element;
    }

    @Override
    public Node visitNormalElement(HTMLParser.NormalElementContext ctx) {
        var tagToken = ctx.beginTag().TAG_NAME().getSymbol();
        String tagName = tagToken.getText();
        int line = tagToken.getLine();

        ElementNode elementNode = new ElementNode(tagName, ElementNode.Type.Normal,line);
        for (var attribute : ctx.beginTag().attribute()) {
            elementNode.AddAttribute(visitAttribute(attribute));
        }
        for (var element : ctx.element()) {
            elementNode.AddChild(visitElement(element));
        }
        return elementNode;
    }

    @Override
    public AttributeNode visitAttribute(HTMLParser.AttributeContext ctx) {
        String name = ctx.TAG_NAME().getText();
        int line = ctx.TAG_NAME().getSymbol().getLine();
        String value = null;
        if (ctx.ATTVALUE_VALUE() != null) {
            value = ctx.ATTVALUE_VALUE().getText();
        }
        return new AttributeNode(name, value, line);
    }

    @Override
    public Node visitElement(HTMLParser.ElementContext ctx) {
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
