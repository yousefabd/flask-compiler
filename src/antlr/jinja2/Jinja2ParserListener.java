// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/jinja2/Jinja2Parser.g4 by ANTLR 4.13.2

    package antlr.jinja2;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Jinja2Parser}.
 */
public interface Jinja2ParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(Jinja2Parser.TemplateContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(Jinja2Parser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(Jinja2Parser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlock_stmt(Jinja2Parser.Block_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlock_stmt(Jinja2Parser.Block_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInline_stmt(Jinja2Parser.Inline_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInline_stmt(Jinja2Parser.Inline_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#for_block}.
	 * @param ctx the parse tree
	 */
	void enterFor_block(Jinja2Parser.For_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#for_block}.
	 * @param ctx the parse tree
	 */
	void exitFor_block(Jinja2Parser.For_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#end_for}.
	 * @param ctx the parse tree
	 */
	void enterEnd_for(Jinja2Parser.End_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#end_for}.
	 * @param ctx the parse tree
	 */
	void exitEnd_for(Jinja2Parser.End_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#target}.
	 * @param ctx the parse tree
	 */
	void enterTarget(Jinja2Parser.TargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#target}.
	 * @param ctx the parse tree
	 */
	void exitTarget(Jinja2Parser.TargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#if_block}.
	 * @param ctx the parse tree
	 */
	void enterIf_block(Jinja2Parser.If_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#if_block}.
	 * @param ctx the parse tree
	 */
	void exitIf_block(Jinja2Parser.If_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#set_inline}.
	 * @param ctx the parse tree
	 */
	void enterSet_inline(Jinja2Parser.Set_inlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#set_inline}.
	 * @param ctx the parse tree
	 */
	void exitSet_inline(Jinja2Parser.Set_inlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#set_block}.
	 * @param ctx the parse tree
	 */
	void enterSet_block(Jinja2Parser.Set_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#set_block}.
	 * @param ctx the parse tree
	 */
	void exitSet_block(Jinja2Parser.Set_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#set_targets}.
	 * @param ctx the parse tree
	 */
	void enterSet_targets(Jinja2Parser.Set_targetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#set_targets}.
	 * @param ctx the parse tree
	 */
	void exitSet_targets(Jinja2Parser.Set_targetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#macro_block}.
	 * @param ctx the parse tree
	 */
	void enterMacro_block(Jinja2Parser.Macro_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#macro_block}.
	 * @param ctx the parse tree
	 */
	void exitMacro_block(Jinja2Parser.Macro_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#call_params}.
	 * @param ctx the parse tree
	 */
	void enterCall_params(Jinja2Parser.Call_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#call_params}.
	 * @param ctx the parse tree
	 */
	void exitCall_params(Jinja2Parser.Call_paramsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(Jinja2Parser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(Jinja2Parser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#block_block}.
	 * @param ctx the parse tree
	 */
	void enterBlock_block(Jinja2Parser.Block_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#block_block}.
	 * @param ctx the parse tree
	 */
	void exitBlock_block(Jinja2Parser.Block_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#extends_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExtends_stmt(Jinja2Parser.Extends_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#extends_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExtends_stmt(Jinja2Parser.Extends_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInclude_stmt(Jinja2Parser.Include_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInclude_stmt(Jinja2Parser.Include_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(Jinja2Parser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(Jinja2Parser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(Jinja2Parser.PrimaryContext ctx);
}