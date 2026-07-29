package jinja2.visitor;

import antlr.html.HTMLLexer;
import antlr.html.HTMLParser;
import antlr.html.HTMLParserBaseVisitor;
import jinja2.models.TemplateNode;
import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeTextNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
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
import jinja2.models.statement.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntlrToTemplateAstVisitor extends HTMLParserBaseVisitor<TemplateNode> {
// =====================================================
    // TEMPLATE
    // =====================================================

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

        return new TemplateFile(contents, ctx.getStart().getLine());
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
    // OUTPUT  {{ expr }}
    // =====================================================

    @Override
    public OutputNode visitVariableStatement(HTMLParser.VariableStatementContext ctx) {
        return visitVariable(ctx.variable());
    }

    @Override
    public OutputNode visitVariable(HTMLParser.VariableContext ctx) {

        ExpressionNode expression = (ExpressionNode) visit(ctx.expr());

        return new OutputNode(expression, ctx.getStart().getLine());
    }

    // =====================================================
    // HTML ELEMENTS
    // =====================================================

    @Override
    public ContentNode visitHtmlStatement(HTMLParser.HtmlStatementContext ctx) {
        return (ContentNode) visit(ctx.htmlElement());
    }

    @Override
    public ContentNode visitHtmlElement(HTMLParser.HtmlElementContext ctx) {

        if (ctx.normalElement() != null)
            return (ContentNode) visit(ctx.normalElement());

        return (ContentNode) visit(ctx.voidElement());
    }

    @Override
    public HTMLNormalElementNode visitNormalElement(HTMLParser.NormalElementContext ctx) {

        String tagName = ctx.beginTag().TAG_ACCEPTED_NAME().getText();
        int line       = ctx.getStart().getLine();

        HTMLNormalElementNode element = new HTMLNormalElementNode(tagName, line);

        for (HTMLParser.AttributeContext attrCtx : ctx.beginTag().attribute()) {
            element.addAttribute((HtmlAttributeNode) visit(attrCtx));
        }

        for (HTMLParser.TagContext tagCtx : ctx.tag()) {
            ContentNode child = (ContentNode) visit(tagCtx);
            if (child != null)
                element.addChild(child);
        }

        return element;
    }

    @Override
    public HTMLVoidElementNode visitVoidElement(HTMLParser.VoidElementContext ctx) {

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
    public HtmlAttributeNode visitAttribute(HTMLParser.AttributeContext ctx) {

        String name = ctx.attributeName().getText();
        List<AttributeValuePartNode> parts = new ArrayList<>();

        if (ctx.ATTVALUE_VALUE() != null) {
            String raw = ctx.ATTVALUE_VALUE().getText().strip();

            // Strip the outer quote characters produced by the ATTVALUE lexer mode
            if ((raw.startsWith("\"") && raw.endsWith("\""))
                    || (raw.startsWith("'") && raw.endsWith("'"))) {
                raw = raw.substring(1, raw.length() - 1);
            }

            parts = parseAttributeValueParts(raw, ctx.getStart().getLine());
        }

        return new HtmlAttributeNode(name, parts, ctx.getStart().getLine());
    }

    /**
     * Splits a raw (already unquoted) attribute value string into a list of
     * {@link AttributeTextNode} and {@link AttributeExpressionNode} parts.
     *
     * <p>Example: {@code "Hello, {{ name }}!"} → [TextNode("Hello, "), ExprNode(name), TextNode("!")]
     *
     * <p><b>NOTE:</b> Expression sub-parsing inside attribute values would require invoking
     * a secondary Jinja expression parser on the captured group. That wiring is left as a
     * TODO — for now, each {@code {{ ... }}} block is represented by an {@link IdentifierNode}
     * stub carrying the raw expression text, so the tree is structurally complete even if the
     * expression is not yet deeply parsed.
     */
    private ExpressionNode parseAttributeExpression(String fullToken, int line) {
        CharStream input = CharStreams.fromString(fullToken);
        HTMLLexer lexer = new HTMLLexer(input);
        HTMLParser parser = new HTMLParser(new CommonTokenStream(lexer));
        OutputNode output = visitVariable(parser.variable());
        return output.getExpression();
    }
    private List<AttributeValuePartNode> parseAttributeValueParts(String raw, int line) {

        List<AttributeValuePartNode> parts = new ArrayList<>();
        // Matches both {{ expr }} and {{- expr -}} (whitespace-control variants)
        Pattern exprPattern = Pattern.compile("\\{\\{-?\\s*(.*?)\\s*-?}}");
        Matcher matcher = exprPattern.matcher(raw);

        int last = 0;

        while (matcher.find()) {

            if (matcher.start() > last) {
                parts.add(new AttributeTextNode(
                        raw.substring(last, matcher.start()), line));
            }

            // TODO: feed matcher.group(1) through the Jinja expression sub-parser
            // to produce a proper ExpressionNode instead of an IdentifierNode stub.
            ExpressionNode exprStub = parseAttributeExpression(matcher.group(0).trim(), line);
            parts.add(new AttributeExpressionNode(exprStub, line));

            last = matcher.end();
        }

        if (last < raw.length()) {
            parts.add(new AttributeTextNode(raw.substring(last), line));
        }
        return parts;
    }

    // =====================================================
    // STATEMENT DISPATCH  (tag → stmt → labeled alt)
    // =====================================================

    @Override
    public ContentNode visitStatement(HTMLParser.StatementContext ctx) {
        // Dispatch to whichever labeled alternative of `stmt` was matched:
        // #ForStatement | #IfStatement | #SetStatement | #MacroStatement | #BlockStatement
        return (ContentNode) visit(ctx.stmt());
    }

    @Override
    public ContentNode visitInlineStatement(HTMLParser.InlineStatementContext ctx) {
        // Dispatch to whichever labeled alternative of `inline_stmt` was matched:
        // #InlineExtendsStatement | #InlineIncludeStatement | #InlineSetStatement
        return (ContentNode) visit(ctx.inline_stmt());
    }

    // =====================================================
    // FOR  {% for x in iterable %} ... {% endfor %}
    // =====================================================

    @Override
    public ForStatementNode visitForStatement(
            HTMLParser.ForStatementContext ctx
    ) {
        HTMLParser.For_blockContext forContext =
                ctx.for_block();

        int line = forContext.getStart().getLine();

        List<IdentifierNode> variables =
                buildIdentifiers(forContext.ID());

        ExpressionNode iterable =
                (ExpressionNode) visit(forContext.expr());

        List<ContentNode> body =
                buildBodyContents(forContext.body());

        return new ForStatementNode(
                variables,
                iterable,
                body,
                line
        );
    }

    // =====================================================
    // IF / ELIF / ELSE
    // =====================================================

    @Override
    public IfStatementNode visitIfStatement(HTMLParser.IfStatementContext ctx) {

        HTMLParser.If_blockContext ifCtx = ctx.if_block();
        int line = ifCtx.getStart().getLine();

        // Grammar:
        //   OPEN_TAG IF expr CLOSE_TAG body          ← if branch
        //   (OPEN_TAG ELIF expr CLOSE_TAG body)*     ← 0.n elif branches
        //   (OPEN_TAG ELSE CLOSE_TAG body)?          ← optional else branch
        //   OPEN_TAG ENDIF CLOSE_TAG
        //
        // ctx.expr()  → conditions in order: [if, elif0, elif1, ...]
        // ctx.body()  → bodies  in order: same length, plus one extra if an else is present

        List<HTMLParser.ExprContext> exprs  = ifCtx.expr();
        List<HTMLParser.BodyContext> bodies = ifCtx.body();
        List<IfBranchNode> branches = new ArrayList<>();

        // if + elif
        for (int i = 0; i < exprs.size(); i++) {
            ExpressionNode condition = (ExpressionNode) visit(exprs.get(i));
            List<ContentNode> body   = buildBodyContents(bodies.get(i));
            branches.add(new IfBranchNode(
                    condition, body, exprs.get(i).getStart().getLine()));
        }

        // else (condition == null signals the else branch to IfStatementNode)
        if (bodies.size() > exprs.size()) {
            List<ContentNode> elseBody =
                    buildBodyContents(bodies.get(bodies.size() - 1));
            branches.add(new IfBranchNode(null, elseBody, line));
        }

        return new IfStatementNode(branches, line);
    }

    // =====================================================
    // SET — block form  {% set x %} ... {% endset %}
    // =====================================================

    @Override
    public SetStatementNode visitSetStatement(
            HTMLParser.SetStatementContext ctx
    ) {
        HTMLParser.Set_blockContext setContext =
                ctx.set_block();

        List<IdentifierNode> targets =
                buildIdentifiers(setContext.ID());

        List<ContentNode> body =
                buildBodyContents(setContext.body());

        return SetStatementNode.block(
                targets,
                body,
                setContext.getStart().getLine()
        );
    }

    // =====================================================
    // SET — inline form  {% set x = expr %}
    // =====================================================

    @Override
    public SetStatementNode visitInlineSetStatement(
            HTMLParser.InlineSetStatementContext ctx
    ) {
        HTMLParser.Set_inlineContext setContext =
                ctx.set_inline();

        List<IdentifierNode> targets =
                buildIdentifiers(setContext.ID());

        ExpressionNode value =
                (ExpressionNode) visit(setContext.expr());

        return SetStatementNode.inline(
                targets,
                value,
                setContext.getStart().getLine()
        );
    }
    // =====================================================
    // MACRO  {% macro name(params) %} ... {% endmacro %}
    // =====================================================

    @Override
    public MacroStatementNode visitMacroStatement(HTMLParser.MacroStatementContext ctx) {

        HTMLParser.Macro_blockContext macroCtx = ctx.macro_block();
        int line = macroCtx.getStart().getLine();

        // Grammar: OPEN_TAG MACRO ID LPAREN parameters? RPAREN CLOSE_TAG body OPEN_TAG ENDMACRO CLOSE_TAG
        String macroName = macroCtx.ID().getText();

        List<ParameterNode> params = macroCtx.parameters() != null
                ? buildParameterList(macroCtx.parameters())
                : new ArrayList<>();

        List<ContentNode> body = buildBodyContents(macroCtx.body());

        return new MacroStatementNode(macroName, params, body, line);
    }

    // =====================================================
    // BLOCK  {% block name %} ... {% endblock %}
    // =====================================================

    @Override
    public BlockStatementNode visitBlockStatement(HTMLParser.BlockStatementContext ctx) {

        HTMLParser.Block_blockContext blockCtx = ctx.block_block();

        // Grammar: OPEN_TAG BLOCK ID CLOSE_TAG body OPEN_TAG ENDBLOCK CLOSE_TAG
        String blockName      = blockCtx.ID().getText();
        List<ContentNode> body = buildBodyContents(blockCtx.body());

        return new BlockStatementNode(blockName, body, blockCtx.getStart().getLine());
    }

    // =====================================================
    // EXTENDS  {% extends "base.html" %}
    // =====================================================

    @Override
    public ExtendsStatementNode visitInlineExtendsStatement(
            HTMLParser.InlineExtendsStatementContext ctx) {

        HTMLParser.Extends_stmtContext extendsCtx = ctx.extends_stmt();

        // Grammar: OPEN_TAG EXTENDS STRING CLOSE_TAG
        String path = stripQuotes(extendsCtx.STRING().getText());

        return new ExtendsStatementNode(path, extendsCtx.getStart().getLine());
    }

    // =====================================================
    // INCLUDE  {% include expr %}
    // =====================================================

    @Override
    public IncludeStatementNode visitInlineIncludeStatement(
            HTMLParser.InlineIncludeStatementContext ctx) {

        HTMLParser.Include_stmtContext includeCtx = ctx.include_stmt();

        // Grammar: OPEN_TAG INCLUDE expr CLOSE_TAG
        // The path is typically a string literal; strip its quotes if present.
        String path = stripQuotes(includeCtx.expr().getText());

        return new IncludeStatementNode(path, includeCtx.getStart().getLine());
    }

    // =====================================================
    // PRIMARY EXPRESSIONS
    // =====================================================

    @Override
    public ExpressionNode visitPrimaryExpression(HTMLParser.PrimaryExpressionContext ctx) {
        // expr → primary  (no trailers, no filters)
        return (ExpressionNode) visit(ctx.primary());
    }

    @Override
    public IdentifierNode visitID(HTMLParser.IDContext ctx) {

        return new IdentifierNode(
                ctx.ID().getText(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public StringLiteralNode visitString(HTMLParser.StringContext ctx) {

        return new StringLiteralNode(
                ctx.STRING().getText(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public NumberLiteralNode visitNumber(HTMLParser.NumberContext ctx) {

        return new NumberLiteralNode(
                ctx.getText().trim(),
                ctx.getStart().getLine()
        );
    }

    @Override
    public BooleanLiteralNode visitBoolean(HTMLParser.BooleanContext ctx) {

        boolean value = ctx.TRUE() != null;

        return new BooleanLiteralNode(value, ctx.getStart().getLine());
    }

    @Override
    public NoneLiteralNode visitNone(HTMLParser.NoneContext ctx) {

        return new NoneLiteralNode(ctx.getStart().getLine());
    }

    @Override
    public ExpressionNode visitParenExpression(HTMLParser.ParenExpressionContext ctx) {
        return (ExpressionNode) visit(ctx.expr());
    }

    // =====================================================
    // LIST  [a, b, c]
    // =====================================================

    @Override
    public ListExpressionNode visitList(HTMLParser.ListContext ctx) {

        List<ExpressionNode> elements = new ArrayList<>();

        for (HTMLParser.ExprContext exprCtx : ctx.listdef().expr()) {
            elements.add((ExpressionNode) visit(exprCtx));
        }

        return new ListExpressionNode(elements, ctx.getStart().getLine());
    }

    // =====================================================
    // DICTIONARY  [key: value, ...]
    // =====================================================

    @Override
    public DictionaryExpressionNode visitDictionary(HTMLParser.DictionaryContext ctx) {

        // Grammar: LBRACK ((expr COLON expr) (COMMA (expr COLON expr))*)? RBRACK
        // ctx.dictdef().expr() returns all expressions interleaved: k0, v0, k1, v1, ...
        List<HTMLParser.ExprContext> exprs = ctx.dictdef().expr();
        List<ExpressionNode> keys   = new ArrayList<>();
        List<ExpressionNode> values = new ArrayList<>();

        for (int i = 0; i + 1 < exprs.size(); i += 2) {
            keys.add((ExpressionNode)   visit(exprs.get(i)));
            values.add((ExpressionNode) visit(exprs.get(i + 1)));
        }

        return new DictionaryExpressionNode(keys, values, ctx.getStart().getLine());
    }

    // =====================================================
    // UNARY
    // =====================================================

    @Override
    public UnaryExpressionNode visitUnaryExpression(HTMLParser.UnaryExpressionContext ctx) {

        ExpressionNode expression = (ExpressionNode) visit(ctx.expr());
        Operation operation       = parseOperation(ctx.op.getText());

        return new UnaryExpressionNode(operation, expression, ctx.getStart().getLine());
    }

    // =====================================================
    // BINARY
    // =====================================================
    // =====================================================
// TEST: value is testName(...)
// =====================================================

    @Override
    public TestExpressionNode visitTestExpression(
            HTMLParser.TestExpressionContext ctx
    ) {
        ExpressionNode value =
                (ExpressionNode) visit(ctx.expr());

        HTMLParser.TestInvocationContext invocation =
                ctx.testInvocation();

        String testName =
                invocation.testName().getText();

        boolean negated =
                ctx.NOT() != null;

        List<ArgumentNode> arguments = new ArrayList<>();

        HTMLParser.TestArgumentsContext testArguments =
                invocation.testArguments();

        if (testArguments != null) {

            // Parenthesized arguments:
            // value is divisibleby(3)
            if (testArguments.arguments() != null) {
                arguments.addAll(
                        buildArgumentList(testArguments.arguments())
                );
            }

            // One argument without parentheses:
            // value is divisibleby 3
            else if (testArguments.primary() != null) {
                ExpressionNode argumentValue =
                        (ExpressionNode) visit(testArguments.primary());

                arguments.add(
                        new ArgumentNode(
                                null,
                                argumentValue,
                                testArguments.getStart().getLine()
                        )
                );
            }
        }

        return new TestExpressionNode(
                value,
                testName,
                arguments,
                negated,
                ctx.getStart().getLine()
        );
    }
    @Override
    public BinaryExpressionNode visitBinaryExpression(HTMLParser.BinaryExpressionContext ctx) {

        ExpressionNode left  = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        Operation operation  = parseOperation(ctx.op.getText());

        return new BinaryExpressionNode(left, operation, right, ctx.getStart().getLine());
    }

    // =====================================================
    // ID + TRAILERS + FILTERS
    // =====================================================

    @Override
    public ExpressionNode visitIDTrFlExpression(HTMLParser.IDTrFlExpressionContext ctx) {

        ExpressionNode current = (ExpressionNode) visit(ctx.primary());

        // Trailers are applied left-to-right, each wrapping the previous result.
        for (HTMLParser.TrailerContext trailerCtx : ctx.trailer()) {

            // Property access:  .name
            if (trailerCtx.DOT() != null) {

                IdentifierNode property = new IdentifierNode(
                        trailerCtx.ID().getText(),
                        trailerCtx.getStart().getLine()
                );

                current = new PropertyAccessNode(
                        current, property, trailerCtx.getStart().getLine());
            }

            // Index access:  [expr]
            else if (trailerCtx.LBRACK() != null) {

                ExpressionNode index = (ExpressionNode) visit(trailerCtx.expr());

                current = new IndexAccessNode(
                        current, index, trailerCtx.getStart().getLine());
            }

            // Call:  (args?)
            else if (trailerCtx.LPAREN() != null) {

                List<ArgumentNode> args = trailerCtx.arguments() != null
                        ? buildArgumentList(trailerCtx.arguments())
                        : new ArrayList<>();

                current = new CallExpressionNode(
                        current, args, trailerCtx.getStart().getLine());
            }
        }

        // Filters are chained after all trailers:  expr | filter1 | filter2(args)
        for (HTMLParser.FilterContext filterCtx : ctx.filter()) {

            String filterName = filterCtx.ID().getText();

            List<ArgumentNode> args = filterCtx.arguments() != null
                    ? buildArgumentList(filterCtx.arguments())
                    : new ArrayList<>();

            current = new FilterExpressionNode(
                    current, filterName, args, filterCtx.getStart().getLine());
        }

        return current;
    }

    // =====================================================
    // HELPERS — arguments
    // =====================================================

    private List<ArgumentNode> buildArgumentList(HTMLParser.ArgumentsContext ctx) {

        List<ArgumentNode> args = new ArrayList<>();

        for (HTMLParser.ArgumentContext argCtx : ctx.argument()) {
            args.add(buildArgument(argCtx));
        }

        return args;
    }

    private ArgumentNode buildArgument(HTMLParser.ArgumentContext ctx) {

        // Grammar: expr (ASSIGN expr)?
        // Keyword arg:  name=value  — the first expr is an identifier used as the key name.
        if (ctx.ASSIGN() != null) {
            String keyword       = ctx.expr(0).getText();
            ExpressionNode value = (ExpressionNode) visit(ctx.expr(1));
            return new ArgumentNode(keyword, value, ctx.getStart().getLine());
        }

        // Positional arg
        ExpressionNode value = (ExpressionNode) visit(ctx.expr(0));
        return new ArgumentNode(null, value, ctx.getStart().getLine());
    }

    // =====================================================
    // HELPERS — macro parameters
    // =====================================================

    private List<ParameterNode> buildParameterList(HTMLParser.ParametersContext ctx) {

        List<ParameterNode> params = new ArrayList<>();

        for (HTMLParser.ParameterContext paramCtx : ctx.parameter()) {
            params.add(buildParameter(paramCtx));
        }

        return params;
    }

    private ParameterNode buildParameter(HTMLParser.ParameterContext ctx) {

        // Grammar: ID (ASSIGN expr)?
        String name = ctx.ID().getText();

        ExpressionNode defaultValue = ctx.expr() != null
                ? (ExpressionNode) visit(ctx.expr())
                : null;

        return new ParameterNode(name, defaultValue, ctx.getStart().getLine());
    }

    // =====================================================
    // HELPERS — body (tag*)
    // =====================================================

    private List<ContentNode> buildBodyContents(HTMLParser.BodyContext ctx) {

        List<ContentNode> body = new ArrayList<>();

        for (HTMLParser.TagContext tagCtx : ctx.tag()) {
            ContentNode node = (ContentNode) visit(tagCtx);
            if (node != null)
                body.add(node);
        }

        return body;
    }

    // =====================================================
    // OPERATION PARSER
    // =====================================================
    private List<IdentifierNode> buildIdentifiers(
            List<TerminalNode> identifierTokens
    ) {
        List<IdentifierNode> identifiers = new ArrayList<>();

        for (TerminalNode token : identifierTokens) {
            identifiers.add(
                    new IdentifierNode(
                            token.getText(),
                            token.getSymbol().getLine()
                    )
            );
        }

        return identifiers;
    }
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

            default -> throw new RuntimeException("Unknown operator: " + text);
        };
    }

    // =====================================================
    // UTILITIES
    // =====================================================

    /** Strips the outer single or double quote characters from a string token. */
    private String stripQuotes(String s) {

        if (s.length() >= 2 &&
                ((s.startsWith("\"") && s.endsWith("\"")) ||
                        (s.startsWith("'")  && s.endsWith("'")))) {
            return s.substring(1, s.length() - 1);
        }

        return s;
    }
}
