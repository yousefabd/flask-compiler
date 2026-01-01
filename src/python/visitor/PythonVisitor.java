// cSpell: disable
package python.visitor;

import java.util.ArrayList;

import antlr.python.PythonParser; 
import antlr.python.PythonParserBaseVisitor;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

import python.models.Import_statement.*;
import python.models.atom_statement.*;
import python.models.compound_statement.*;
import python.models.enums.Operation;
import python.models.expr_statement.*;
import python.models.funcdef.*;
import python.models.root.*;
import python.models.small_statement.*;
import python.models.trailer.*;

public class PythonVisitor extends PythonParserBaseVisitor<Object> {

    @Override
    public Program visitProg(PythonParser.ProgContext ctx) {
        Program res = new Program(new ArrayList<>(), ctx.getStart().getLine());
        for (PythonParser.StmtContext stmtCtx : ctx.stmt()) {
            Statement stm = (Statement) visit(stmtCtx);
            res.addStatement(stm);
        }
        return res;
    }

    @Override
    public Statement visitSimpleStatement(PythonParser.SimpleStatementContext ctx) {
        return (Statement) visitSimple_stmt(ctx.simple_stmt());
    }

    @Override
    public Statement visitCompoundStatement(PythonParser.CompoundStatementContext ctx) {
        return (Statement) visit(ctx.compound_stmt());
    }

    @Override
    public SimpleStatement visitSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        SimpleStatement stm = new SimpleStatement(ctx.getStart().getLine());
        for(PythonParser.Small_stmtContext smCtx : ctx.small_stmt())
        {
            SmallStatement sm = (SmallStatement) visit(smCtx);
            stm.addSmallStatement(sm);
        }
        return stm;
    }

    @Override
    public IfStatement visitIfStatement(PythonParser.IfStatementContext ctx) {
        IfStatement ist = new IfStatement(ctx.getStart().getLine());
        Condition cnd = (Condition) visit(ctx.cond());
        Body bd = visitBody(ctx.body());
        ist.addCondBody(cnd, bd);
        for(PythonParser.Elif_clauseContext elifCtx : ctx.elif_clause()) {
            Pair<Condition, Body> elif = visitElif_clause(elifCtx);
            ist.addCondBody(elif.a, elif.b);
        }
        if(ctx.else_clause() != null)
        {
            Body els = visitElse_clause(ctx.else_clause());
            ist.addElse(els);
        }
        
        return ist;
    }

    @Override
    public WhileStatement visitWhileStatement(PythonParser.WhileStatementContext ctx) {
        Condition cnd = (Condition) visit(ctx.cond());
        Body bd = visitBody(ctx.body());
        Body els = null;
        if(ctx.else_clause() != null)
        {
            els = visitElse_clause(ctx.else_clause());
        }
        return new WhileStatement(cnd, bd, els, ctx.getStart().getLine());
    }

    @Override
    public ForStatement visitForStatement(PythonParser.ForStatementContext ctx) {
        ArrayList<ID> iters = visitIter(ctx.iter());
        Expression expr = (Expression) visit(ctx.expr());
        Body bd = visitBody(ctx.body());
        Body els = null;
        if(ctx.else_clause() != null)
        {
            els = visitElse_clause(ctx.else_clause());
        }
        return new ForStatement(iters, expr, bd, els, ctx.getStart().getLine());
    }


    @Override
    public DecoratorStatement visitDecoratorStatement(PythonParser.DecoratorStatementContext ctx) {
        ArrayList<Decorator> dcs = new ArrayList<>();
        FunctionDef fd;
        for(PythonParser.DecoratorContext dcCtx : ctx.decorator())
            dcs.add(visitDecorator(dcCtx));
        fd = visitFuncdef(ctx.funcdef());
        return new DecoratorStatement(dcs, fd, ctx.getStart().getLine());
    }

    @Override
    public ExpressionStatement visitExpressionStatement(PythonParser.ExpressionStatementContext ctx) {
        return visitExpr_stmt(ctx.expr_stmt());
    }

    @Override
    public AugAssignStatement visitAugAssignStatement(PythonParser.AugAssignStatementContext ctx) {
        return visitAugassign_stmt(ctx.augassign_stmt());
    }

    @Override
    public PassStatement visitPassStatement(PythonParser.PassStatementContext ctx) {
        return visitPass_stmt(ctx.pass_stmt());
    }

    @Override
    public BreakStatement visitBreakStatement(PythonParser.BreakStatementContext ctx) {
        return visitBreak_stmt(ctx.break_stmt());
    }

    @Override
    public ContinueStatement visitContinueStatement(PythonParser.ContinueStatementContext ctx) {
        return visitContinue_stmt(ctx.continue_stmt());
    }

    @Override
    public ReturnStatement visitReturnStatement(PythonParser.ReturnStatementContext ctx) {
        return visitReturn_stmt(ctx.return_stmt());
    }

    @Override
    public ImportStatement visitImportStatement(PythonParser.ImportStatementContext ctx) {
        return (ImportStatement) visit(ctx.import_stmt());
    }

    @Override
    public AugAssignStatement visitAugassign_stmt(PythonParser.Augassign_stmtContext ctx) {
        ID id = new ID(ctx.ID().getText(),ctx.getStart().getLine());
        Expression ex = (Expression) visit(ctx.expr());
        Operation op = Operation.NONE;
        if (ctx.ADD_ASSIGN() != null) op = Operation.ADD;
        else if (ctx.SUB_ASSIGN() != null) op = Operation.SUB;
        else if (ctx.MULT_ASSIGN() != null) op = Operation.MULT;
        else if (ctx.DIV_ASSIGN() != null) op = Operation.DIV;
        else if (ctx.MOD_ASSIGN() != null) op = Operation.MOD;
        else if (ctx.IDIV_ASSIGN() != null) op = Operation.IDIV;
        else if (ctx.AND_ASSIGN() != null) op = Operation.AND;
        else if (ctx.OR_ASSIGN() != null) op = Operation.OR;
        else if (ctx.XOR_ASSIGN() != null) op = Operation.XOR;
        else if (ctx.LSHIFT_ASSIGN() != null) op = Operation.LSHIFT;
        else if (ctx.RSHIFT_ASSIGN() != null) op = Operation.RSHIFT;
        else if (ctx.POWER_ASSIGN() != null) op = Operation.POWER;

        return new AugAssignStatement(id,op,ex,ctx.getStart().getLine());
    }

    @Override
    public PassStatement visitPass_stmt(PythonParser.Pass_stmtContext ctx) {
        return new PassStatement(ctx.getStart().getLine());
    }

    @Override
    public BreakStatement visitBreak_stmt(PythonParser.Break_stmtContext ctx) {
        return new BreakStatement(ctx.getStart().getLine());
    }

    @Override
    public ContinueStatement visitContinue_stmt(PythonParser.Continue_stmtContext ctx) {
        return new ContinueStatement(ctx.getStart().getLine());
    }

    @Override
    public ReturnStatement visitReturn_stmt(PythonParser.Return_stmtContext ctx) {
        if(ctx.condlist() != null)
            return new ReturnStatement(visitCondlist(ctx.condlist()),ctx.getStart().getLine());
        else return new ReturnStatement(new ArrayList<>(),ctx.getStart().getLine());
    }

    @Override
    public SimpleImportStatement visitSimpleImport(PythonParser.SimpleImportContext ctx) {
        return new SimpleImportStatement(visitDotted_name(ctx.dotted_name()),ctx.getStart().getLine());
    }

    @Override
    public FromImportStatement visitFromImport(PythonParser.FromImportContext ctx) {
        ArrayList<ID> dottedName = visitDotted_name(ctx.dotted_name());
        ArrayList<ID> targets = null;
        Operation star = Operation.NONE;
        if (ctx.STAR() != null) {
            star = Operation.STAR;
        } 
        else {
            targets = new ArrayList<>();
            for (TerminalNode idNode : ctx.ID()) {
                targets.add(new ID(idNode.getText(),ctx.getStart().getLine()));
            }
        }
        return new FromImportStatement(dottedName, targets, star,ctx.getStart().getLine());
    }

    @Override
    public ArrayList<ID> visitDotted_name(PythonParser.Dotted_nameContext ctx) {
        ArrayList<ID> res = new ArrayList<>();
        for(TerminalNode idNode : ctx.ID())
        {
            res.add(new ID(idNode.getText(),ctx.getStart().getLine()));
        }
        return res;
    }

    @Override
    public ExpressionStatement visitExpr_stmt(PythonParser.Expr_stmtContext ctx) {
        ExpressionStatement exprStm = new ExpressionStatement(ctx.getStart().getLine());
        exprStm.conditions = visitCondlist(ctx.condlist(0));
        exprStm.assigns = null;
        if(ctx.ASSIGN() != null)
        {
            exprStm.setHaveEquals(Operation.EQUALS);
            exprStm.assigns = visitCondlist(ctx.condlist(1));
        }
        return exprStm;
    }

    @Override
    public ArrayList<Condition> visitCondlist(PythonParser.CondlistContext ctx) {

        ArrayList<Condition> res = new ArrayList<>();
        for(PythonParser.CondContext cdCtx: ctx.cond())
        {
            res.add((Condition) visit(cdCtx));
        }
        return res;
    }

    @Override
    public Comparison visitSimpleCondition(PythonParser.SimpleConditionContext ctx) {
        return (Comparison) visit(ctx.comparison());
    }

    @Override
    public CompoundCondition visitNotCondition(PythonParser.NotConditionContext ctx) {
        return new CompoundCondition((Condition) visit(ctx.cond()),ctx.getStart().getLine());
    }

    @Override
    public CompoundCondition visitOrCondition(PythonParser.OrConditionContext ctx) {
        return new CompoundCondition(Operation.OR,
                (Condition) visit(ctx.cond(0)),
                (Condition) visit(ctx.cond(1)),
                ctx.getStart().getLine());
    }

    @Override
    public Object visitAndCondition(PythonParser.AndConditionContext ctx) {
        return new CompoundCondition(Operation.AND,
                (Condition) visit(ctx.cond(0)),
                (Condition) visit(ctx.cond(1)),
                ctx.getStart().getLine());
    }

    @Override
    public RelationalComparison visitRelationalComparison(PythonParser.RelationalComparisonContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        Operation op = Operation.NONE;
        if (ctx.EQUALS() != null) op = Operation.EQUALS;
        else if (ctx.GREATER_THAN() != null) op = Operation.GREATER_THAN;
        else if (ctx.LESS_THAN() != null) op = Operation.LESS_THAN;
        else if (ctx.GT_EQ() != null) op = Operation.GT_EQ;
        else if (ctx.LT_EQ() != null) op = Operation.LT_EQ;
        else if (ctx.NOT_EQ() != null) op = Operation.NOT_EQ;
        else if (ctx.NOT() != null && ctx.IN()  != null) op = Operation.NOTIN;
        else if (ctx.NOT() != null && ctx.IS()  != null) op = Operation.ISNOT;
        else if (ctx.IN() != null) op = Operation.IN;
        else if (ctx.IS() != null) op = Operation.IS;
        return new RelationalComparison(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public Expression visitExpressionComparison(PythonParser.ExpressionComparisonContext ctx) {
        return (Expression) visit(ctx.expr());
    }

    @Override
    public BinaryExpression visitAddSubExpression(PythonParser.AddSubExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        String opSymbol = ctx.op.getText();
        Operation op = Operation.NONE;
        if (opSymbol.equals("+")) {
            op = Operation.ADD;
        } else if (opSymbol.equals("-")) {
            op = Operation.SUB;
        }
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public BinaryExpression visitPowerExpression(PythonParser.PowerExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        Operation op = Operation.POWER;
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public BinaryExpression visitAndExpression(PythonParser.AndExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        Operation op = Operation.AND;
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public IDTrailer visitIDExpression(PythonParser.IDExpressionContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        ArrayList<Trailer> trailers = new ArrayList<>();
        for(PythonParser.TrailerContext trCtx : ctx.trailer()) {
            Trailer tr = visitTrailer(trCtx);
            trailers.add(tr);
        }
        return new IDTrailer(id, trailers, ctx.getStart().getLine());
    }
 
    @Override
    public BinaryExpression visitXorExpression(PythonParser.XorExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        Operation op = Operation.XOR;
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public Atom visitAtomExpression(PythonParser.AtomExpressionContext ctx) {
        return (Atom) visit(ctx.atom());
    }

    @Override
    public UnaryExpression visitUnaryExpression(PythonParser.UnaryExpressionContext ctx) {
        Operation op = Operation.NONE;
        if (ctx.ADD() != null) {
            op = Operation.ADD;
        } else if (ctx.MINUS() != null) {
            op = Operation.SUB;
        } else if (ctx.NOT_OP() != null) {
            op = Operation.NOT_OP;
        }
        return new UnaryExpression(op, (Expression) visit(ctx.expr()), ctx.getStart().getLine());
    }

    @Override
    public BinaryExpression visitShiftExpression(PythonParser.ShiftExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        String opSymbol = ctx.op.getText();
        Operation op = Operation.NONE;
        if (opSymbol.equals("<<")) {
            op = Operation.LSHIFT;
        } else if (opSymbol.equals(">>")) {
            op = Operation.RSHIFT;
        }
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public BinaryExpression visitOrExpression(PythonParser.OrExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        Operation op = Operation.OR;
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public BinaryExpression visitMulDivExpression(PythonParser.MulDivExpressionContext ctx) {
        Expression left = (Expression) visit(ctx.expr(0));
        Expression right = (Expression) visit(ctx.expr(1));
        String opSymbol = ctx.op.getText();
        Operation op = switch (opSymbol) {
            case "*" -> Operation.MULT;
            case "/" -> Operation.DIV;
            case "//" -> Operation.IDIV;
            case "%" -> Operation.MOD;
            case "@" -> Operation.AT;
            default -> Operation.NONE;
        };
        return new BinaryExpression(left, op, right, ctx.getStart().getLine());
    }

    @Override
    public ParenAtom visitParenAtom(PythonParser.ParenAtomContext ctx) {
        return new ParenAtom((Expression) visit(ctx.expr()), ctx.getStart().getLine());
    }

    @Override
    public python.models.atom_statement.List visitListAtom(PythonParser.ListAtomContext ctx) {
        if(ctx.list() == null) 
            return new python.models.atom_statement.List(new ArrayList<>(), ctx.getStart().getLine());
        return (python.models.atom_statement.List) visit(ctx.list());
    }

    @Override
    public Atom visitDicOrSetAtom(PythonParser.DicOrSetAtomContext ctx) {
        if(ctx.dicorset() == null)
            return new python.models.atom_statement.Set(new ArrayList<>(), ctx.getStart().getLine());
        return (Atom) visit(ctx.dicorset());
    }

    @Override
    public ID visitIDAtom(PythonParser.IDAtomContext ctx) {
        return new ID(ctx.ID().getText(), ctx.getStart().getLine());
    }

    @Override
    public BoolAtom visitBoolAtom(PythonParser.BoolAtomContext ctx) {
        return new BoolAtom(ctx.TRUE() != null, ctx.getStart().getLine());
    }

    @Override
    public IntegerAtom visitIntegerAtom(PythonParser.IntegerAtomContext ctx) {
        if(ctx.MINUS() != null) 
            return new IntegerAtom(-Integer.parseInt(ctx.INTEGER().getText()), ctx.getStart().getLine());
        return new IntegerAtom(Integer.parseInt(ctx.INTEGER().getText()), ctx.getStart().getLine());
    }

    @Override
    public FloatAtom visitFloatAtom(PythonParser.FloatAtomContext ctx) {
        if(ctx.MINUS() != null) 
            return new FloatAtom(-Float.parseFloat(ctx.FLOAT().getText()), ctx.getStart().getLine());
        return new FloatAtom(Float.parseFloat(ctx.FLOAT().getText()), ctx.getStart().getLine());
    }

    @Override
    public None visitNoneAtom(PythonParser.NoneAtomContext ctx) {
        return new None(ctx.getStart().getLine());
    }

    @Override
    public StringAtom visitStringAtom(PythonParser.StringAtomContext ctx) {
        return new StringAtom(ctx.STRING().getText(),ctx.getStart().getLine());
    }

    @Override
    public Trailer visitTrailer(PythonParser.TrailerContext ctx) {
        boolean dot = false;
        ID id = null;
        if (ctx.DOT() != null) {
            dot = true;
            id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        }
        if(ctx.arguments() == null)
            return new Trailer(dot, id, null, ctx.getStart().getLine());
        Arguments args = (Arguments) visit(ctx.arguments());
        return new Trailer(dot, id, args, ctx.getStart().getLine());
    }

    @Override
    public CallArguments visitCallArguments(PythonParser.CallArgumentsContext ctx) {
        return new CallArguments(visitArglist(ctx.arglist()), ctx.getStart().getLine());
    }

    @Override
    public SubscriptArguments visitSubscriptArguments(PythonParser.SubscriptArgumentsContext ctx) {
        return new SubscriptArguments(visitSubscriptlist(ctx.subscriptlist()), ctx.getStart().getLine());
    }

    @Override
    public ArrayList<Argument> visitArglist(PythonParser.ArglistContext ctx) {
        ArrayList<Argument> res = new ArrayList<>();
        for(PythonParser.ArgumentContext argCtx : ctx.argument())
            res.add(visitArgument(argCtx));
        return res;
    }

    @Override
    public Argument visitArgument(PythonParser.ArgumentContext ctx) {
        Condition cnd = (Condition) visit(ctx.cond(0));
        if (ctx.ASSIGN() != null) {
            Condition valueCnd = (Condition) visit(ctx.cond(1));
            return new Argument(cnd, valueCnd, ctx.getStart().getLine());
        } else {
            return new Argument(cnd, null, ctx.getStart().getLine());
        }
    }

    @Override
    public ArrayList<Condition> visitSubscriptlist(PythonParser.SubscriptlistContext ctx) {
        ArrayList<Condition> res = new ArrayList<>();
        for(PythonParser.CondContext cndCtx : ctx.cond())
            res.add((Condition) visit(cndCtx));
        return res;
    }

    @Override
    public ArrayList<ID> visitIter(PythonParser.IterContext ctx) {
        ArrayList<ID> res = new ArrayList<>();
        for (TerminalNode idNode : ctx.ID()) {
            String name = idNode.getText();
            ID id = new ID(name, ctx.getStart().getLine());
            res.add(id);
        }
        return res;
    }


    @Override
    public FunctionDef visitFuncdef(PythonParser.FuncdefContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        ArrayList<Parameter> parameters = visitDef_parameters(ctx.def_parameters());
        Condition returnType = null;
        if(ctx.ARROW() != null) {
            returnType = (Condition) visit(ctx.cond());
        }
        Body body = visitBody(ctx.body());
        return new FunctionDef(id, parameters, returnType, body, ctx.getStart().getLine());
    }

    @Override
    public ArrayList<Parameter> visitDef_parameters(PythonParser.Def_parametersContext ctx) {
        ArrayList<Parameter> params = new ArrayList<>();
        for (PythonParser.Def_parameterContext paramCtx : ctx.def_parameter())
            params.add(visitDef_parameter(paramCtx));
        return params;
    }

    @Override
    public Parameter visitDef_parameter(PythonParser.Def_parameterContext ctx) {
        ID id = new ID(ctx.ID().getText(), ctx.getStart().getLine());
        Condition type = null;
        Condition defaultValue = null;
        if (ctx.COLON() != null) {
            type = (Condition) visit(ctx.cond(0));
        }
        if (ctx.ASSIGN() != null) {
            defaultValue = (Condition) visit(ctx.cond(1));
        }
        return new Parameter(id, type, defaultValue, ctx.getStart().getLine());
    }

    @Override
    public Pair<Condition, Body> visitElif_clause(PythonParser.Elif_clauseContext ctx) {
        Condition condition = (Condition) visit(ctx.cond());
        Body body = visitBody(ctx.body());
        return new Pair<>(condition, body);
    }

    @Override
    public Body visitElse_clause(PythonParser.Else_clauseContext ctx) {
        return visitBody(ctx.body());
    }

    @Override
    public Body visitBody(PythonParser.BodyContext ctx) {
        Body bd = new Body(ctx.getStart().getLine());
        if (ctx.simple_stmt() != null) {
            Statement stmt = visitSimple_stmt(ctx.simple_stmt());
            bd.addStatement(stmt);
        }
        if (ctx.stmt() != null && !ctx.stmt().isEmpty()) {
            for (PythonParser.StmtContext stmtCtx : ctx.stmt()) {
                Statement stmt = (Statement) visit(stmtCtx);
                bd.addStatement(stmt);
            }
        }
        return bd;
    }

    @Override
    public python.models.atom_statement.List visitListDef(PythonParser.ListDefContext ctx) {
        python.models.atom_statement.List lst = new python.models.atom_statement.List(ctx.getStart().getLine());
        for(PythonParser.ExprContext exprCtx : ctx.expr())
            lst.addItem((Expression) visit(exprCtx));
        return lst;
    }

    @Override
    public python.models.atom_statement.Set visitSetDef(PythonParser.SetDefContext ctx) {
        python.models.atom_statement.Set set = new python.models.atom_statement.Set(ctx.getStart().getLine());
        for(PythonParser.ExprContext exprCtx : ctx.expr())
            set.addItem((Expression) visit(exprCtx));
        return set;
    }

    @Override
    public python.models.atom_statement.Dictionary visitDictionaryDef(PythonParser.DictionaryDefContext ctx) {
        python.models.atom_statement.Dictionary dict = new python.models.atom_statement.Dictionary(ctx.getStart().getLine());
        java.util.List<PythonParser.ExprContext> exprList = ctx.expr();
        for(int i = 0; i < exprList.size(); i += 2) {
            PythonParser.ExprContext keyExprCtx = exprList.get(i);
            PythonParser.ExprContext valueExprCtx = exprList.get(i + 1);
            dict.addItem((Expression) visit(keyExprCtx), (Expression) visit(valueExprCtx));
        }
        return dict;
    }

    @Override
    public Decorator visitDecorator(PythonParser.DecoratorContext ctx) {
        ArrayList<ID> dottedName = visitDotted_name(ctx.dotted_name());
        ArrayList<Argument> arguments = null;
        if (ctx.arglist() != null) {
            arguments = visitArglist(ctx.arglist());
        }
        else {
            arguments = new ArrayList<>();
        }
        return new Decorator(dottedName, arguments,ctx.getStart().getLine());
    }
}
