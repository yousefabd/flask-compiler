package jinja2.visitor;

import antlr.html.HTMLParser;
import antlr.html.HTMLParserBaseVisitor;
import antlr.jinja2.Jinja2Parser;
import jinja2.models.TemplateNode;
import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
import jinja2.models.content.html.HTMLNormalElementNode;
import jinja2.models.content.html.HTMLVoidElementNode;
import jinja2.models.expression.*;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import jinja2.models.file.TemplateFile;

import java.util.ArrayList;
import java.util.List;

public class AntlrToTemplateAstVisitor extends HTMLParserBaseVisitor<TemplateNode> {
// =====================================================
    // TEMPLATE
    // =====================================================

    @Override
    public TemplateNode visitTemplate(HTMLParser.TemplateContext ctx) {

        List<ContentNode> contents = new ArrayList<>();

        for (HTMLParser.TagContext tagCtx : ctx.tag()) {

            ContentNode node = (ContentNode) visit(tagCtx);

            if (node != null)
                contents.add(node);
        }

        return new TemplateFile(
                contents,
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // TEXT
    // =====================================================

    @Override
    public HtmlTextNode visitText(HTMLParser.TextContext ctx) {

        return new HtmlTextNode(
                ctx.getText(),
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // OUTPUT
    // =====================================================

    @Override
    public OutputNode visitVariableStatement(
            HTMLParser.VariableStatementContext ctx) {

        return visitVariable(ctx.variable());
    }

    @Override
    public OutputNode visitVariable(
            HTMLParser.VariableContext ctx) {

        ExpressionNode expression =
                (ExpressionNode) visit(ctx.expr());

        return new OutputNode(
                expression,
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // HTML ELEMENTS
    // =====================================================

    @Override
    public ContentNode visitHtmlStatement(
            HTMLParser.HtmlStatementContext ctx) {

        return (ContentNode) visit(ctx.htmlElement());
    }

    @Override
    public ContentNode visitHtmlElement(
            HTMLParser.HtmlElementContext ctx) {

        if (ctx.normalElement() != null)
            return (ContentNode) visit(ctx.normalElement());

        return (ContentNode) visit(ctx.voidElement());
    }

    @Override
    public HTMLNormalElementNode visitNormalElement(
            HTMLParser.NormalElementContext ctx) {

        String tagName = ctx.beginTag().TAG_ACCEPTED_NAME().getText();
        int line       = ctx.getStart().getLine();

        HTMLNormalElementNode element = new HTMLNormalElementNode(tagName, line);

        // attributes on the opening tag
        for (HTMLParser.AttributeContext attrCtx : ctx.beginTag().attribute()) {
            element.addAttribute((HtmlAttributeNode) visit(attrCtx));
        }

        // body: tag* — can be text, {{ expr }}, nested elements, statements
        for (HTMLParser.TagContext tagCtx : ctx.tag()) {
            ContentNode child = (ContentNode) visit(tagCtx);
            if (child != null)
                element.addChild(child);
        }

        return element;
    }

    @Override
    public HTMLVoidElementNode visitVoidElement(
            HTMLParser.VoidElementContext ctx) {

        String tagName = ctx.TAG_ACCEPTED_NAME().getText();
        int line       = ctx.getStart().getLine();

        HTMLVoidElementNode element = new HTMLVoidElementNode(tagName, line);

        for (HTMLParser.AttributeContext attrCtx : ctx.attribute()) {
            element.addAttribute((HtmlAttributeNode) visit(attrCtx));
        }

        return element;
    }

    // =====================================================
    // ATTRIBUTES
    // =====================================================

    @Override
    public HtmlAttributeNode visitAttribute(
            HTMLParser.AttributeContext ctx) {

        String name = ctx.CHAR_NAME().getText();

        // AttributeTextNode / AttributeExpressionNode are not yet implemented.
        // Store an empty parts list for now; wire them in when those nodes are done.
        return new HtmlAttributeNode(
                name,
                new ArrayList<>(),
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // PRIMARY EXPRESSIONS
    // =====================================================

    @Override
    public IdentifierNode visitID(
            HTMLParser.IDContext ctx) {

        return new IdentifierNode(
                ctx.ID().getText(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public StringLiteralNode visitString(
            HTMLParser.StringContext ctx) {

        return new StringLiteralNode(
                ctx.STRING().getText(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public NumberLiteralNode visitNumber(
            HTMLParser.NumberContext ctx) {

        return new NumberLiteralNode(
                ctx.getText().trim(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public BooleanLiteralNode visitBoolean(
            HTMLParser.BooleanContext ctx) {

        boolean value = ctx.TRUE() != null;

        return new BooleanLiteralNode(
                value,
                ctx.getStart().getLine()
        );
    }

    @Override
    public NoneLiteralNode visitNone(
            HTMLParser.NoneContext ctx) {

        return new NoneLiteralNode(
                ctx.getStart().getLine()
        );
    }

    @Override
    public ExpressionNode visitParenExpression(
            HTMLParser.ParenExpressionContext ctx) {

        return (ExpressionNode) visit(ctx.expr());
    }

    // =====================================================
    // UNARY
    // =====================================================

    @Override
    public UnaryExpressionNode visitUnaryExpression(
            HTMLParser.UnaryExpressionContext ctx) {

        ExpressionNode expression =
                (ExpressionNode) visit(ctx.expr());

        Operation operation =
                parseOperation(ctx.op.getText());

        return new UnaryExpressionNode(
                operation,
                expression,
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // BINARY
    // =====================================================

    @Override
    public BinaryExpressionNode visitBinaryExpression(
            HTMLParser.BinaryExpressionContext ctx) {

        ExpressionNode left =
                (ExpressionNode) visit(ctx.expr(0));

        ExpressionNode right =
                (ExpressionNode) visit(ctx.expr(1));

        Operation operation =
                parseOperation(ctx.op.getText());

        return new BinaryExpressionNode(
                left,
                operation,
                right,
                ctx.getStart().getLine()
        );
    }

    // =====================================================
    // ID + TRAILERS
    // =====================================================

    @Override
    public ExpressionNode visitIDTrFlExpression(
            HTMLParser.IDTrFlExpressionContext ctx) {

        ExpressionNode current =
                (ExpressionNode) visit(ctx.primary());

        for (HTMLParser.TrailerContext trailerCtx : ctx.trailer()) {

            // property access:  .name
            if (trailerCtx.DOT() != null) {

                IdentifierNode property =
                        new IdentifierNode(
                                trailerCtx.ID().getText(),
                                trailerCtx.getStart().getLine()
                        );

                current = new PropertyAccessNode(
                        current,
                        property,
                        trailerCtx.getStart().getLine()
                );
            }

            // index access:  [expr]
            else if (trailerCtx.LBRACK() != null) {

                ExpressionNode index =
                        (ExpressionNode) visit(trailerCtx.expr());

                current = new IndexAccessNode(
                        current,
                        index,
                        trailerCtx.getStart().getLine()
                );
            }

            // TODO: call trailer  (expr)  → CallExpressionNode (not yet implemented)
        }

        // TODO: filters  |name(args)  → FilterExpressionNode (not yet implemented)

        return current;
    }

    // =====================================================
    // OPERATION PARSER
    // =====================================================

    private Operation parseOperation(String text) {

        return switch (text) {

            case "+"   -> Operation.PLUS;
            case "-"   -> Operation.MINUS;
            case "*"   -> Operation.STAR;
            case "/"   -> Operation.SLASH;
            case "%"   -> Operation.PERCENT;

            case "=="  -> Operation.EQ;
            case "!="  -> Operation.NEQ;

            case "<"   -> Operation.LT;
            case ">"   -> Operation.GT;
            case "<="  -> Operation.LTE;
            case ">="  -> Operation.GTE;

            case "and" -> Operation.AND;
            case "or"  -> Operation.OR;
            case "not" -> Operation.NOT;

            case "in"  -> Operation.IN;
            case "is"  -> Operation.IS;

            default -> throw new RuntimeException(
                    "Unknown operator: " + text
            );
        };
    }

}
