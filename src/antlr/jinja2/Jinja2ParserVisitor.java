// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/jinja2/Jinja2Parser.g4 by ANTLR 4.13.2

    package antlr.jinja2;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Jinja2Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Jinja2ParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(Jinja2Parser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#block_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_stmt(Jinja2Parser.Block_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInline_stmt(Jinja2Parser.Inline_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#for_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_block(Jinja2Parser.For_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#end_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd_for(Jinja2Parser.End_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#target}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget(Jinja2Parser.TargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#if_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_block(Jinja2Parser.If_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#set_inline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_inline(Jinja2Parser.Set_inlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#set_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_block(Jinja2Parser.Set_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#set_targets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_targets(Jinja2Parser.Set_targetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#macro_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_block(Jinja2Parser.Macro_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#call_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_params(Jinja2Parser.Call_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(Jinja2Parser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#block_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_block(Jinja2Parser.Block_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#extends_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtends_stmt(Jinja2Parser.Extends_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#include_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_stmt(Jinja2Parser.Include_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(Jinja2Parser.PrimaryContext ctx);
}