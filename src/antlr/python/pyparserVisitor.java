// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/python/pyparser.g4 by ANTLR 4.13.2

    package antlr.python;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link pyparser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface pyparserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link pyparser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(pyparser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(pyparser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#compouned_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompouned_stmt(pyparser.Compouned_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#simple_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_stmt(pyparser.Simple_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmall_stmt(pyparser.Small_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#augassign_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAugassign_stmt(pyparser.Augassign_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#pass_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPass_stmt(pyparser.Pass_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#break_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(pyparser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#continue_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_stmt(pyparser.Continue_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(pyparser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#import_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_stmt(pyparser.Import_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#dotted_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_name(pyparser.Dotted_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#import_targets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_targets(pyparser.Import_targetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#expr_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(pyparser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#testlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist(pyparser.TestlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest(pyparser.TestContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(pyparser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(pyparser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(pyparser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(pyparser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(pyparser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(pyparser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#arglist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArglist(pyparser.ArglistContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(pyparser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#subscriptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptlist(pyparser.SubscriptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#iter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIter(pyparser.IterContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#iterable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterable(pyparser.IterableContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#call_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_expr(pyparser.Call_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#funcdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncdef(pyparser.FuncdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#typedargslist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedargslist(pyparser.TypedargslistContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(pyparser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#def_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameters(pyparser.Def_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#def_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_parameter(pyparser.Def_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#named_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamed_parameter(pyparser.Named_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#elif_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElif_clause(pyparser.Elif_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#else_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_clause(pyparser.Else_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(pyparser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(pyparser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#dicorset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDicorset(pyparser.DicorsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link pyparser#decorator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecorator(pyparser.DecoratorContext ctx);
}