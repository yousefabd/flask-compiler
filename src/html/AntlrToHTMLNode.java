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
    List<String> sematicErrors;

    public AntlrToHTMLNode(List<String> sematicErrors) {
        this.sematicErrors = sematicErrors;
    }

    @Override
    public Node visitVoidElement(HTMLParser.VoidElementContext ctx) {
        String tagName = ctx.TAG_NAME().getText();

        return new ElementNode(tagName, ElementNode.Type.Void);
    }

    @Override
    public Node visitNormalElement(HTMLParser.NormalElementContext ctx) {
        String tagName = ctx.beginTag().TAG_NAME().getText();
        ElementNode elementNode = new ElementNode(tagName, ElementNode.Type.Normal);
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
        String value = null;
        if (ctx.ATTVALUE_VALUE() != null) {
            value = ctx.ATTVALUE_VALUE().getText();
        }
        return new AttributeNode(name, value);
    }

    @Override
    public Node visitElement(HTMLParser.ElementContext ctx) {
        if (ctx.normalElement() != null) {
            return visitNormalElement(ctx.normalElement());
        } else if (ctx.voidElement() != null) {
            return visitVoidElement(ctx.voidElement());
        }
        return new TextNode(ctx.TEXT().getText());
    }

}
