// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/python/pyparser.g4 by ANTLR 4.13.2

    package antlr.python;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link pyparser}.
 */
public interface pyparserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link pyparser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(pyparser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(pyparser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(pyparser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(pyparser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#compouned_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompouned_stmt(pyparser.Compouned_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#compouned_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompouned_stmt(pyparser.Compouned_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimple_stmt(pyparser.Simple_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#simple_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimple_stmt(pyparser.Simple_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSmall_stmt(pyparser.Small_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSmall_stmt(pyparser.Small_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#augassign_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAugassign_stmt(pyparser.Augassign_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#augassign_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAugassign_stmt(pyparser.Augassign_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPass_stmt(pyparser.Pass_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPass_stmt(pyparser.Pass_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(pyparser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(pyparser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(pyparser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(pyparser.Continue_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(pyparser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(pyparser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void enterImport_stmt(pyparser.Import_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#import_stmt}.
	 * @param ctx the parse tree
	 */
	void exitImport_stmt(pyparser.Import_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void enterDotted_name(pyparser.Dotted_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void exitDotted_name(pyparser.Dotted_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#import_targets}.
	 * @param ctx the parse tree
	 */
	void enterImport_targets(pyparser.Import_targetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#import_targets}.
	 * @param ctx the parse tree
	 */
	void exitImport_targets(pyparser.Import_targetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(pyparser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(pyparser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#testlist}.
	 * @param ctx the parse tree
	 */
	void enterTestlist(pyparser.TestlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#testlist}.
	 * @param ctx the parse tree
	 */
	void exitTestlist(pyparser.TestlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#test}.
	 * @param ctx the parse tree
	 */
	void enterTest(pyparser.TestContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#test}.
	 * @param ctx the parse tree
	 */
	void exitTest(pyparser.TestContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(pyparser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(pyparser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(pyparser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(pyparser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(pyparser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(pyparser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(pyparser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(pyparser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(pyparser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(pyparser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(pyparser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(pyparser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#arglist}.
	 * @param ctx the parse tree
	 */
	void enterArglist(pyparser.ArglistContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#arglist}.
	 * @param ctx the parse tree
	 */
	void exitArglist(pyparser.ArglistContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(pyparser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(pyparser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptlist(pyparser.SubscriptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptlist(pyparser.SubscriptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#iter}.
	 * @param ctx the parse tree
	 */
	void enterIter(pyparser.IterContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#iter}.
	 * @param ctx the parse tree
	 */
	void exitIter(pyparser.IterContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#iterable}.
	 * @param ctx the parse tree
	 */
	void enterIterable(pyparser.IterableContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#iterable}.
	 * @param ctx the parse tree
	 */
	void exitIterable(pyparser.IterableContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#call_expr}.
	 * @param ctx the parse tree
	 */
	void enterCall_expr(pyparser.Call_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#call_expr}.
	 * @param ctx the parse tree
	 */
	void exitCall_expr(pyparser.Call_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#funcdef}.
	 * @param ctx the parse tree
	 */
	void enterFuncdef(pyparser.FuncdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#funcdef}.
	 * @param ctx the parse tree
	 */
	void exitFuncdef(pyparser.FuncdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void enterTypedargslist(pyparser.TypedargslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void exitTypedargslist(pyparser.TypedargslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(pyparser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(pyparser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameters(pyparser.Def_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#def_parameters}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameters(pyparser.Def_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void enterDef_parameter(pyparser.Def_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#def_parameter}.
	 * @param ctx the parse tree
	 */
	void exitDef_parameter(pyparser.Def_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#named_parameter}.
	 * @param ctx the parse tree
	 */
	void enterNamed_parameter(pyparser.Named_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#named_parameter}.
	 * @param ctx the parse tree
	 */
	void exitNamed_parameter(pyparser.Named_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void enterElif_clause(pyparser.Elif_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#elif_clause}.
	 * @param ctx the parse tree
	 */
	void exitElif_clause(pyparser.Elif_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#else_clause}.
	 * @param ctx the parse tree
	 */
	void enterElse_clause(pyparser.Else_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#else_clause}.
	 * @param ctx the parse tree
	 */
	void exitElse_clause(pyparser.Else_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(pyparser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(pyparser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(pyparser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(pyparser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#dicorset}.
	 * @param ctx the parse tree
	 */
	void enterDicorset(pyparser.DicorsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#dicorset}.
	 * @param ctx the parse tree
	 */
	void exitDicorset(pyparser.DicorsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link pyparser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecorator(pyparser.DecoratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link pyparser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecorator(pyparser.DecoratorContext ctx);
}