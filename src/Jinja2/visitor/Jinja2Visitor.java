//package Jinja2.visitor;
//
//import Jinja2.models.Primary.ID;
//import Jinja2.models.expression.Expression;
//import Jinja2.models.inline_statement.InlineStatement;
//import Jinja2.models.root.Tag;
//import Jinja2.models.root.Template;
//import Jinja2.models.root.VariableStatement;
//import Jinja2.models.statement.ForStatement;
//import Jinja2.models.statement.IfStatement;
//import Jinja2.models.statement.Statement;
//import antlr.jinja2.Jinja2Parser;
//import antlr.jinja2.Jinja2ParserBaseVisitor;
//import org.antlr.v4.runtime.tree.TerminalNode;
//
//import java.util.ArrayList;
//
//public class Jinja2Visitor extends Jinja2ParserBaseVisitor<Object> {
//    @Override
//    public Template visitTemplate(Jinja2Parser.TemplateContext ctx) {
//        Template t = new Template(ctx.getStart().getLine());
//        for(Jinja2Parser.TagContext tagCtx: ctx.tag())
//            t.addTag((Tag) visit(tagCtx));
//        return t;
//    }
//
//    @Override
//    public VariableStatement visitVariableStatement(Jinja2Parser.VariableStatementContext ctx) {
//        return visitVariable(ctx.variable());
//    }
//
//    @Override
//    public Statement visitStatement(Jinja2Parser.StatementContext ctx) {
//        return (Statement) visit(ctx.stmt());
//    }
//
//    @Override
//    public InlineStatement visitInlineStatement(Jinja2Parser.InlineStatementContext ctx) {
//        return (InlineStatement) visit(ctx.inline_stmt());
//    }
//
//    @Override
//    public String visitText(Jinja2Parser.TextContext ctx) {
//        return ctx.getText();
//    }
//
//    @Override
//    public VariableStatement visitVariable(Jinja2Parser.VariableContext ctx) {
//        return new VariableStatement((Expression) visit(ctx.expr()), ctx.getStart().getLine());
//    }
//
//    @Override
//    public ForStatement visitForStatement(Jinja2Parser.ForStatementContext ctx) {
//        return visitFor_block(ctx.for_block());
//    }
//
//    @Override
//    public IfStatement visitIfStatement(Jinja2Parser.IfStatementContext ctx) {
//        return visitIf_block(ctx.if_block());
//    }
//
//    @Override
//    public Object visitSetStatement(Jinja2Parser.SetStatementContext ctx) {
//        return super.visitSetStatement(ctx);
//    }
//
//    @Override
//    public Object visitMacroStatement(Jinja2Parser.MacroStatementContext ctx) {
//        return super.visitMacroStatement(ctx);
//    }
//
//    @Override
//    public Object visitBlockStatement(Jinja2Parser.BlockStatementContext ctx) {
//        return super.visitBlockStatement(ctx);
//    }
//
//    @Override
//    public Object visitInlineExtendsStatement(Jinja2Parser.InlineExtendsStatementContext ctx) {
//        return super.visitInlineExtendsStatement(ctx);
//    }
//
//    @Override
//    public Object visitInlineIncludeStatement(Jinja2Parser.InlineIncludeStatementContext ctx) {
//        return super.visitInlineIncludeStatement(ctx);
//    }
//
//    @Override
//    public Object visitInlineSetStatement(Jinja2Parser.InlineSetStatementContext ctx) {
//        return super.visitInlineSetStatement(ctx);
//    }
//
//    @Override
//    public ForStatement visitFor_block(Jinja2Parser.For_blockContext ctx) {
//        ArrayList<ID> ids = new ArrayList<>();
//        for(TerminalNode idCtx : ctx.ID())
//        {
//            ids.add(new ID(idCtx.getText(), ctx.getStart().getLine()));
//        }
//        Expression iterable = (Expression) visit(ctx.expr());
//        ArrayList<Tag> body = new ArrayList<>();
//        for(Jinja2Parser.TagContext tagCtx: ctx.tag())
//            body.add((Tag) visit(tagCtx));
//        return new ForStatement(ids, iterable, body, ctx.getStart().getLine());
//    }
//
//    @Override
//    public IfStatement visitIf_block(Jinja2Parser.If_blockContext ctx) {
//        ArrayList<Expression> conditions = new ArrayList<>();
//        ArrayList<Tag> bodies = new ArrayList<>();
//        for(Jinja2Parser.TagContext tagCtx: ctx.tag())
//            bodies.add((Tag) visit(tagCtx));
//        for(Jinja2Parser.ExprContext condCtx: ctx.expr())
//            conditions.add((Expression) visit(condCtx));
//        return new IfStatement(conditions, bodies, null, ctx.getStart().getLine());
//    }
//
//    @Override
//    public Object visitBody(Jinja2Parser.BodyContext ctx) {
//        return super.visitBody(ctx);
//    }
//
//    @Override
//    public Object visitSet_inline(Jinja2Parser.Set_inlineContext ctx) {
//        return super.visitSet_inline(ctx);
//    }
//
//    @Override
//    public Object visitSet_block(Jinja2Parser.Set_blockContext ctx) {
//        return super.visitSet_block(ctx);
//    }
//
//    @Override
//    public Object visitMacro_block(Jinja2Parser.Macro_blockContext ctx) {
//        return super.visitMacro_block(ctx);
//    }
//
//    @Override
//    public Object visitParameters(Jinja2Parser.ParametersContext ctx) {
//        return super.visitParameters(ctx);
//    }
//
//    @Override
//    public Object visitParameter(Jinja2Parser.ParameterContext ctx) {
//        return super.visitParameter(ctx);
//    }
//
//    @Override
//    public Object visitBlock_block(Jinja2Parser.Block_blockContext ctx) {
//        return super.visitBlock_block(ctx);
//    }
//
//    @Override
//    public Object visitExtends_stmt(Jinja2Parser.Extends_stmtContext ctx) {
//        return super.visitExtends_stmt(ctx);
//    }
//
//    @Override
//    public Object visitInclude_stmt(Jinja2Parser.Include_stmtContext ctx) {
//        return super.visitInclude_stmt(ctx);
//    }
//
//    @Override
//    public Object visitPrimaryExpression(Jinja2Parser.PrimaryExpressionContext ctx) {
//        return super.visitPrimaryExpression(ctx);
//    }
//
//    @Override
//    public Object visitBinaryExpression(Jinja2Parser.BinaryExpressionContext ctx) {
//        return super.visitBinaryExpression(ctx);
//    }
//
//    @Override
//    public Object visitUnaryExpression(Jinja2Parser.UnaryExpressionContext ctx) {
//        return super.visitUnaryExpression(ctx);
//    }
//
//    @Override
//    public Object visitIDTrFlExpression(Jinja2Parser.IDTrFlExpressionContext ctx) {
//        return super.visitIDTrFlExpression(ctx);
//    }
//
//    @Override
//    public Object visitTrailer(Jinja2Parser.TrailerContext ctx) {
//        return super.visitTrailer(ctx);
//    }
//
//    @Override
//    public Object visitFilter(Jinja2Parser.FilterContext ctx) {
//        return super.visitFilter(ctx);
//    }
//
//    @Override
//    public Object visitArguments(Jinja2Parser.ArgumentsContext ctx) {
//        return super.visitArguments(ctx);
//    }
//
//    @Override
//    public Object visitParenExpression(Jinja2Parser.ParenExpressionContext ctx) {
//        return super.visitParenExpression(ctx);
//    }
//
//    @Override
//    public Object visitID(Jinja2Parser.IDContext ctx) {
//        return super.visitID(ctx);
//    }
//
//    @Override
//    public Object visitBoolean(Jinja2Parser.BooleanContext ctx) {
//        return super.visitBoolean(ctx);
//    }
//
//    @Override
//    public Object visitNumber(Jinja2Parser.NumberContext ctx) {
//        return super.visitNumber(ctx);
//    }
//
//    @Override
//    public Object visitNone(Jinja2Parser.NoneContext ctx) {
//        return super.visitNone(ctx);
//    }
//
//    @Override
//    public Object visitString(Jinja2Parser.StringContext ctx) {
//        return super.visitString(ctx);
//    }
//
//    @Override
//    public Object visitList(Jinja2Parser.ListContext ctx) {
//        return super.visitList(ctx);
//    }
//
//    @Override
//    public Object visitDictionary(Jinja2Parser.DictionaryContext ctx) {
//        return super.visitDictionary(ctx);
//    }
//
//    @Override
//    public Object visitListdef(Jinja2Parser.ListdefContext ctx) {
//        return super.visitListdef(ctx);
//    }
//
//    @Override
//    public Object visitDictdef(Jinja2Parser.DictdefContext ctx) {
//        return super.visitDictdef(ctx);
//    }
//}
