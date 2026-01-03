package jinja2.visitor;

import jinja2.models.Primary.*;
import jinja2.models.Primary.Number;
import jinja2.models.expression.*;
import jinja2.models.inline_statement.ExtendsStatement;
import jinja2.models.inline_statement.IncludeStatement;
import jinja2.models.inline_statement.InlineStatement;
import jinja2.models.inline_statement.SetInlineStatement;
import jinja2.models.root.Tag;
import jinja2.models.root.Template;
import jinja2.models.root.Text;
import jinja2.models.root.VariableStatement;
import jinja2.models.statement.*;
import antlr.jinja2.Jinja2Parser;
import antlr.jinja2.Jinja2ParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayList;
import java.util.List;

public class Jinja2Visitor extends Jinja2ParserBaseVisitor<Object> {

    @Override
    public Template visitTemplate(Jinja2Parser.TemplateContext ctx) {
        Template t = new Template(ctx.getStart().getLine());
        for(Jinja2Parser.TagContext tagCtx: ctx.tag())
            t.addTag((Tag) visit(tagCtx));
        return t;
    }

    @Override
    public VariableStatement visitVariableStatement(Jinja2Parser.VariableStatementContext ctx) {
        return visitVariable(ctx.variable());
    }

    @Override
    public Statement visitStatement(Jinja2Parser.StatementContext ctx) {
        return (Statement) visit(ctx.stmt());
    }

    @Override
    public InlineStatement visitInlineStatement(Jinja2Parser.InlineStatementContext ctx) {
        return (InlineStatement) visit(ctx.inline_stmt());
    }

    @Override
    public Text visitText(Jinja2Parser.TextContext ctx) {
        return new Text(ctx.getText(),ctx.getStart().getLine());
    }

    @Override
    public VariableStatement visitVariable(Jinja2Parser.VariableContext ctx) {
        return new VariableStatement((Expression) visit(ctx.expr()), ctx.getStart().getLine());
    }

    @Override
    public ForStatement visitForStatement(Jinja2Parser.ForStatementContext ctx) {
        return visitFor_block(ctx.for_block());
    }

    @Override
    public IfStatement visitIfStatement(Jinja2Parser.IfStatementContext ctx) {
        return visitIf_block(ctx.if_block());
    }

    @Override
    public SetBlockStatement visitSetStatement(Jinja2Parser.SetStatementContext ctx) {
            return visitSet_block(ctx.set_block());
    }

    @Override
    public MacroStatement visitMacroStatement(Jinja2Parser.MacroStatementContext ctx) {
        return visitMacro_block(ctx.macro_block());
    }

    @Override
    public BlockStatement visitBlockStatement(Jinja2Parser.BlockStatementContext ctx) {
        return visitBlock_block(ctx.block_block());
    }

    @Override
    public ExtendsStatement visitInlineExtendsStatement(Jinja2Parser.InlineExtendsStatementContext ctx) {
        return visitExtends_stmt(ctx.extends_stmt());
    }

    @Override
    public IncludeStatement visitInlineIncludeStatement(Jinja2Parser.InlineIncludeStatementContext ctx) {
        return visitInclude_stmt(ctx.include_stmt());
    }

    @Override
    public SetInlineStatement visitInlineSetStatement(Jinja2Parser.InlineSetStatementContext ctx) {
        return visitSet_inline(ctx.set_inline());
    }

    @Override
    public ForStatement visitFor_block(Jinja2Parser.For_blockContext ctx) {
        ArrayList<ID> ids = new ArrayList<>();
        for(TerminalNode idCtx : ctx.ID())
        {
            ids.add(new ID(idCtx.getText(), ctx.getStart().getLine()));
        }
        Expression iterable = (Expression) visit(ctx.expr());
        Body body = visitBody(ctx.body());
        return new ForStatement(ids, iterable, body, ctx.getStart().getLine());
    }


    @Override
    public IfStatement visitIf_block(Jinja2Parser.If_blockContext ctx) {
        IfStatement ifStatement = new IfStatement(ctx.getStart().getLine());
        ArrayList<Expression> conditions = new ArrayList<>();
        ArrayList<Body> bodies = new ArrayList<>();
        for(Jinja2Parser.BodyContext bodyCtx: ctx.body())
            bodies.add(visitBody(bodyCtx));
        for(Jinja2Parser.ExprContext condCtx: ctx.expr())
            conditions.add((Expression) visit(condCtx));
        for (int i = 0; i < bodies.size() ; i++) {
            if(i >= conditions.size()) {
                conditions.add(null);
            }
            ifStatement.addBranch(conditions.get(i), bodies.get(i));
        }
        return ifStatement;
    }

    @Override
    public Body visitBody(Jinja2Parser.BodyContext ctx) {
        Body body = new Body(ctx.getStart().getLine());
        for(Jinja2Parser.TagContext tagCtx :ctx.tag()){
            body.addTag((Tag) visit(tagCtx));
        }
        return body;
    }

    @Override
    public SetInlineStatement visitSet_inline(Jinja2Parser.Set_inlineContext ctx) {
        List<ID>  ids = new ArrayList<>();
        Expression iterable = (Expression) visit(ctx.expr());
        for (TerminalNode idCtx : ctx.ID()) {
            ids.add(new ID(idCtx.getText(), ctx.getStart().getLine()));
        }
        return new SetInlineStatement(ids, iterable, ctx.getStart().getLine());
    }

    @Override
    public SetBlockStatement visitSet_block(Jinja2Parser.Set_blockContext ctx) {
        List<ID>  ids = new ArrayList<>();
        Body body = visitBody(ctx.body());
        for (TerminalNode idCtx:ctx.ID()) {
            ids.add(new ID(idCtx.getText(), ctx.getStart().getLine()));
        }
        return new SetBlockStatement(ids, body, ctx.getStart().getLine());
    }

    @Override
    public MacroStatement visitMacro_block(Jinja2Parser.Macro_blockContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        Body bodies = visitBody(ctx.body());
        if(ctx.parameters() == null)
            return new MacroStatement(id, new ArrayList<>(), bodies, ctx.getStart().getLine());
        return new MacroStatement(id, visitParameters(ctx.parameters()), bodies, ctx.getStart().getLine());
    }

    @Override
    public BlockStatement visitBlock_block(Jinja2Parser.Block_blockContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        Body body = visitBody(ctx.body());
        return new BlockStatement(id, body, ctx.getStart().getLine());
    }

    @Override
    public ExtendsStatement visitExtends_stmt(Jinja2Parser.Extends_stmtContext ctx) {
        String temp = ctx.STRING().getText();
        return new ExtendsStatement(temp, ctx.getStart().getLine());
    }

    @Override
    public IncludeStatement visitInclude_stmt(Jinja2Parser.Include_stmtContext ctx) {
        Expression temp = (Expression) visit(ctx.expr());
        return new IncludeStatement(temp, ctx.getStart().getLine());
    }

    @Override
    public Primary visitPrimaryExpression(Jinja2Parser.PrimaryExpressionContext ctx) {
        return (Primary) visit(ctx.primary());
    }

    @Override
    public BinaryExpression visitBinaryExpression(Jinja2Parser.BinaryExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
//        String opText = ctx.op.getText();
//        Operation operator = Operation.valueOf(opText.toUpperCase());
        Operation op = null;
        String opSym = ctx.op.getText();
        if (opSym.equals("+")) op = Operation.PLUS;
        else if (opSym.equals("-")) op = Operation.MINUS;
        else if (opSym.equals("not")) op = Operation.NOT;
        else if (opSym.equals("*")) op = Operation.STAR;
        else if (opSym.equals("/")) op = Operation.SLASH;
        else if (opSym.equals("%")) op = Operation.PERCENT;
        else if (opSym.equals("<")) op = Operation.LT;
        else if (opSym.equals(">")) op = Operation.GT;
        else if (opSym.equals("<=")) op = Operation.LTE;
        else if (opSym.equals(">=")) op = Operation.GTE;
        else if (opSym.equals("==")) op = Operation.EQ;
        else if (opSym.equals("!=")) op = Operation.NEQ;
        else if (opSym.equals("in")) op = Operation.IN;
        else if (opSym.equals("is")) op = Operation.IS;
        else if (opSym.equals("and")) op = Operation.AND;
        else if (opSym.equals("or")) op = Operation.OR;
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public UnaryExpression visitUnaryExpression(Jinja2Parser.UnaryExpressionContext ctx) {
        Expression operand = (Expression) visit(ctx.expr());
        Operation op = null;
        String opSym = ctx.op.getText();
        if (opSym.equals("+")) op = Operation.PLUS;
        else if (opSym.equals("-")) op = Operation.MINUS;
        else if (opSym.equals("not")) op = Operation.NOT;
        return new UnaryExpression(op, operand, ctx.getStart().getLine());
    }

    @Override
    public IdTrFlExpression visitIDTrFlExpression(Jinja2Parser.IDTrFlExpressionContext ctx) {
        Primary pr = (Primary) visit(ctx.primary());

        List<Trailer> trailers = new ArrayList<>();
        for(Jinja2Parser.TrailerContext trCtx : ctx.trailer())
            trailers.add(visitTrailer(trCtx));

        List<Filter> filters = new ArrayList<>();
        for(Jinja2Parser.FilterContext filterCtx:ctx.filter())
            filters.add(visitFilter(filterCtx));

        return new IdTrFlExpression(pr, trailers, filters, ctx.getStart().getLine());
    }

    @Override
    public Trailer visitTrailer(Jinja2Parser.TrailerContext ctx) {
        if(ctx.LPAREN() != null)
        {
            if(ctx.arguments() == null)
                return new Trailer(new ArrayList<>(), null, null,ctx.getStart().getLine());
            else
                return new Trailer(visitArguments(ctx.arguments()), null, null, ctx.getStart().getLine());
        }
        else if (ctx.DOT() != null)
        {
            int line = ctx.getStart().getLine();
            return new Trailer(null, null, new ID(ctx.ID().getText(), line), line);
        }
        else if (ctx.LBRACK() != null)
        {
            int line = ctx.getStart().getLine();
            return new Trailer(null, (Expression) visit(ctx.expr()),null, line);
        }
        return new Trailer(null,null,null,4444444);
    }

    @Override
    public Filter visitFilter(Jinja2Parser.FilterContext ctx) {
        ArrayList<Argument> arguments = new ArrayList<>();
        if(ctx.arguments() != null) arguments = visitArguments(ctx.arguments());
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        return new Filter(id,arguments,ctx.getStart().getLine());
    }

    @Override
    public ArrayList<Argument> visitArguments(Jinja2Parser.ArgumentsContext ctx) {
        ArrayList<Argument>  arguments = new ArrayList<>();
        for(Jinja2Parser.ArgumentContext argCtx : ctx.argument()){
            arguments.add(visitArgument(argCtx));
        }
        return arguments;
    }

    @Override
    public List<Parameter> visitParameters(Jinja2Parser.ParametersContext ctx) {
        List<Parameter> parameters = new ArrayList<>();
        for(Jinja2Parser.ParameterContext paramCtx:ctx.parameter()){
            parameters.add(visitParameter(paramCtx));
        }
        return parameters;
    }

    @Override
    public Parameter visitParameter(Jinja2Parser.ParameterContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        Expression expression = null;
        if(ctx.expr() != null) expression = (Expression) visit(ctx.expr());
        return new Parameter(id,expression,ctx.getStart().getLine());
    }

    @Override
    public ParenExpression visitParenExpression(Jinja2Parser.ParenExpressionContext ctx) {
        Expression expression = (Expression) visit(ctx.expr());
        return new ParenExpression(expression,ctx.getStart().getLine());
    }

    @Override
    public ID visitID(Jinja2Parser.IDContext ctx) {
        return new ID(ctx.ID().getText(), ctx.getStart().getLine());
    }

    @Override
    public BooleanPrimary visitBoolean(Jinja2Parser.BooleanContext ctx) {
        boolean value ;
        value = ctx.TRUE() != null;
        return new BooleanPrimary(value,ctx.getStart().getLine());
    }

    @Override
    public Number visitNumber(Jinja2Parser.NumberContext ctx) {
        String text = ctx.getText();
        double value = Double.parseDouble(text);
        return new Number(value, ctx.getStart().getLine());
    }

    @Override
    public None visitNone(Jinja2Parser.NoneContext ctx) {
        return new None(ctx.getStart().getLine());
    }

    @Override
    public StringPrimary visitString(Jinja2Parser.StringContext ctx) {
        return new StringPrimary(ctx.STRING().getText(), ctx.getStart().getLine());
    }

    @Override
    public ListDef visitList(Jinja2Parser.ListContext ctx) {
        return visitListdef(ctx.listdef());
    }

    @Override
    public DictionaryDef visitDictionary(Jinja2Parser.DictionaryContext ctx) {
        return visitDictdef(ctx.dictdef());
    }

    @Override
    public ListDef visitListdef(Jinja2Parser.ListdefContext ctx) {
        ListDef listDef = new ListDef(ctx.getStart().getLine());
        if(ctx.expr() == null) return listDef;
        for(Jinja2Parser.ExprContext argCtx : ctx.expr()){
            listDef.addContent((Expression) visit(argCtx));
        }
        return listDef;
    }

    @Override
    public DictionaryDef visitDictdef(Jinja2Parser.DictdefContext ctx) {
        DictionaryDef dict = new DictionaryDef(ctx.getStart().getLine());
        if(ctx.expr() == null) return dict;
        for (int i = 0; i < ctx.expr().size(); i += 2) {
            Expression key = (Expression) visit(ctx.expr(i));
            Expression value = (Expression) visit(ctx.expr(i + 1));
            dict.addKeyValue(key, value);
        }
        return dict;
    }

    @Override
    public Argument visitArgument(Jinja2Parser.ArgumentContext ctx) {
        Expression first = (Expression) visit(ctx.expr(0));
        Expression second = null;
        if(ctx.ASSIGN() != null)
            second = (Expression) visit(ctx.expr(1));
        return new Argument(first, second, ctx.getStart().getLine());
    }
}
