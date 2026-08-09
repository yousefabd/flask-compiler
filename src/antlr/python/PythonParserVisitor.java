// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/python/PythonParser.g4 by ANTLR 4.13.2

    package antlr.python;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PythonParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PythonParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PythonParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(PythonParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStatement(PythonParser.SimpleStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompoundStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(PythonParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(PythonParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(PythonParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DecoratorStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecoratorStatement(PythonParser.DecoratorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_stmt(PythonParser.Simple_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpressionStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(PythonParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AugAssignStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAugAssignStatement(PythonParser.AugAssignStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PassStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(PythonParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(PythonParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImportStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStatement(PythonParser.ImportStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GlobalStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#augassign_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAugassign_stmt(PythonParser.Augassign_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#pass_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPass_stmt(PythonParser.Pass_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#break_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(PythonParser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#continue_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_stmt(PythonParser.Continue_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(PythonParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FromImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromImport(PythonParser.FromImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#global_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_stmt(PythonParser.Global_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#dotted_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_name(PythonParser.Dotted_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#expr_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(PythonParser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#condlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondlist(PythonParser.CondlistContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SimpleCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleCondition(PythonParser.SimpleConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotCondition(PythonParser.NotConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrCondition(PythonParser.OrConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndCondition(PythonParser.AndConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationalComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalComparison(PythonParser.RelationalComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpressionComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionComparison(PythonParser.ExpressionComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpression(PythonParser.AddSubExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpression(PythonParser.PowerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(PythonParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDExpression(PythonParser.IDExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code XorExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXorExpression(PythonParser.XorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AtomExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpression(PythonParser.AtomExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(PythonParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShiftExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpression(PythonParser.ShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(PythonParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpression(PythonParser.MulDivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAtom(PythonParser.ParenAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListAtom(PythonParser.ListAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DicOrSetAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDicOrSetAtom(PythonParser.DicOrSetAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDAtom(PythonParser.IDAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolAtom(PythonParser.BoolAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerAtom(PythonParser.IntegerAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatAtom(PythonParser.FloatAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoneAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoneAtom(PythonParser.NoneAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringAtom(PythonParser.StringAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(PythonParser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallArguments(PythonParser.CallArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubscriptArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptArguments(PythonParser.SubscriptArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#arglist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArglist(PythonParser.ArglistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptlist(PythonParser.SubscriptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#iter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIter(PythonParser.IterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#funcdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncdef(PythonParser.FuncdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#def_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameters(PythonParser.Def_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#def_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameter(PythonParser.Def_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#elif_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElif_clause(PythonParser.Elif_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#else_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_clause(PythonParser.Else_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(PythonParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListDef}
	 * labeled alternative in {@link PythonParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListDef(PythonParser.ListDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDef(PythonParser.SetDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DictionaryDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionaryDef(PythonParser.DictionaryDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecorator(PythonParser.DecoratorContext ctx);
}