// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/python/PythonParser.g4 by ANTLR 4.13.2

    package antlr.python;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PythonParser}.
 */
public interface PythonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PythonParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(PythonParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(PythonParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStatement(PythonParser.SimpleStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStatement(PythonParser.SimpleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompoundStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(PythonParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompoundStatement}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(PythonParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PythonParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(PythonParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(PythonParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(PythonParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(PythonParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecoratorStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDecoratorStatement(PythonParser.DecoratorStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecoratorStatement}
	 * labeled alternative in {@link PythonParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDecoratorStatement(PythonParser.DecoratorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimple_stmt(PythonParser.Simple_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimple_stmt(PythonParser.Simple_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpressionStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(PythonParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpressionStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(PythonParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AugAssignStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAugAssignStatement(PythonParser.AugAssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AugAssignStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAugAssignStatement(PythonParser.AugAssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PassStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PassStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPassStatement(PythonParser.PassStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(PythonParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(PythonParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ContinueStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(PythonParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ContinueStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(PythonParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PythonParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImportStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterImportStatement(PythonParser.ImportStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImportStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitImportStatement(PythonParser.ImportStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GlobalStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GlobalStatement}
	 * labeled alternative in {@link PythonParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStatement(PythonParser.GlobalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#augassign_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAugassign_stmt(PythonParser.Augassign_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#augassign_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAugassign_stmt(PythonParser.Augassign_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPass_stmt(PythonParser.Pass_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPass_stmt(PythonParser.Pass_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(PythonParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(PythonParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(PythonParser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(PythonParser.Continue_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(PythonParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(PythonParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimpleImport(PythonParser.SimpleImportContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FromImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFromImport(PythonParser.FromImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FromImport}
	 * labeled alternative in {@link PythonParser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFromImport(PythonParser.FromImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#global_stmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobal_stmt(PythonParser.Global_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#global_stmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobal_stmt(PythonParser.Global_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void enterDotted_name(PythonParser.Dotted_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void exitDotted_name(PythonParser.Dotted_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(PythonParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(PythonParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#condlist}.
	 * @param ctx the parse tree
	 */
	void enterCondlist(PythonParser.CondlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#condlist}.
	 * @param ctx the parse tree
	 */
	void exitCondlist(PythonParser.CondlistContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SimpleCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCondition(PythonParser.SimpleConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SimpleCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCondition(PythonParser.SimpleConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterNotCondition(PythonParser.NotConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitNotCondition(PythonParser.NotConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterOrCondition(PythonParser.OrConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitOrCondition(PythonParser.OrConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterAndCondition(PythonParser.AndConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndCondition}
	 * labeled alternative in {@link PythonParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitAndCondition(PythonParser.AndConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationalComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterRelationalComparison(PythonParser.RelationalComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationalComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitRelationalComparison(PythonParser.RelationalComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpressionComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterExpressionComparison(PythonParser.ExpressionComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpressionComparison}
	 * labeled alternative in {@link PythonParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitExpressionComparison(PythonParser.ExpressionComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSubExpression(PythonParser.AddSubExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSubExpression(PythonParser.AddSubExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPowerExpression(PythonParser.PowerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPowerExpression(PythonParser.PowerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(PythonParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(PythonParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIDExpression(PythonParser.IDExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIDExpression(PythonParser.IDExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XorExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterXorExpression(PythonParser.XorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XorExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitXorExpression(PythonParser.XorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpression(PythonParser.AtomExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpression(PythonParser.AtomExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(PythonParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(PythonParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ShiftExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(PythonParser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ShiftExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(PythonParser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(PythonParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(PythonParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDivExpression(PythonParser.MulDivExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link PythonParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDivExpression(PythonParser.MulDivExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterParenAtom(PythonParser.ParenAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitParenAtom(PythonParser.ParenAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterListAtom(PythonParser.ListAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitListAtom(PythonParser.ListAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DicOrSetAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterDicOrSetAtom(PythonParser.DicOrSetAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DicOrSetAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitDicOrSetAtom(PythonParser.DicOrSetAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIDAtom(PythonParser.IDAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIDAtom(PythonParser.IDAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterBoolAtom(PythonParser.BoolAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitBoolAtom(PythonParser.BoolAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIntegerAtom(PythonParser.IntegerAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIntegerAtom(PythonParser.IntegerAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterFloatAtom(PythonParser.FloatAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitFloatAtom(PythonParser.FloatAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NoneAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterNoneAtom(PythonParser.NoneAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NoneAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitNoneAtom(PythonParser.NoneAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterStringAtom(PythonParser.StringAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringAtom}
	 * labeled alternative in {@link PythonParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitStringAtom(PythonParser.StringAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(PythonParser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(PythonParser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterCallArguments(PythonParser.CallArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitCallArguments(PythonParser.CallArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubscriptArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptArguments(PythonParser.SubscriptArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubscriptArguments}
	 * labeled alternative in {@link PythonParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptArguments(PythonParser.SubscriptArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#arglist}.
	 * @param ctx the parse tree
	 */
	void enterArglist(PythonParser.ArglistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#arglist}.
	 * @param ctx the parse tree
	 */
	void exitArglist(PythonParser.ArglistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(PythonParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptlist(PythonParser.SubscriptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptlist(PythonParser.SubscriptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#iter}.
	 * @param ctx the parse tree
	 */
	void enterIter(PythonParser.IterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#iter}.
	 * @param ctx the parse tree
	 */
	void exitIter(PythonParser.IterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#funcdef}.
	 * @param ctx the parse tree
	 */
	void enterFuncdef(PythonParser.FuncdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#funcdef}.
	 * @param ctx the parse tree
	 */
	void exitFuncdef(PythonParser.FuncdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameters(PythonParser.Def_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameters(PythonParser.Def_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameter(PythonParser.Def_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameter(PythonParser.Def_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void enterElif_clause(PythonParser.Elif_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void exitElif_clause(PythonParser.Elif_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void enterElse_clause(PythonParser.Else_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void exitElse_clause(PythonParser.Else_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(PythonParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(PythonParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListDef}
	 * labeled alternative in {@link PythonParser#list}.
	 * @param ctx the parse tree
	 */
	void enterListDef(PythonParser.ListDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListDef}
	 * labeled alternative in {@link PythonParser#list}.
	 * @param ctx the parse tree
	 */
	void exitListDef(PythonParser.ListDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 */
	void enterSetDef(PythonParser.SetDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 */
	void exitSetDef(PythonParser.SetDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictionaryDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 */
	void enterDictionaryDef(PythonParser.DictionaryDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictionaryDef}
	 * labeled alternative in {@link PythonParser#dicorset}.
	 * @param ctx the parse tree
	 */
	void exitDictionaryDef(PythonParser.DictionaryDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecorator(PythonParser.DecoratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecorator(PythonParser.DecoratorContext ctx);
}