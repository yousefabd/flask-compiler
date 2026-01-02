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
	 * Visit a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableStatement(Jinja2Parser.VariableStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineStatement(Jinja2Parser.InlineStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Text}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(Jinja2Parser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(Jinja2Parser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(Jinja2Parser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(Jinja2Parser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStatement(Jinja2Parser.SetStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroStatement(Jinja2Parser.MacroStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(Jinja2Parser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineExtendsStatement(Jinja2Parser.InlineExtendsStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineIncludeStatement(Jinja2Parser.InlineIncludeStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineSetStatement(Jinja2Parser.InlineSetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#for_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_block(Jinja2Parser.For_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#if_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_block(Jinja2Parser.If_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(Jinja2Parser.BodyContext ctx);
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
	 * Visit a parse tree produced by {@link Jinja2Parser#macro_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_block(Jinja2Parser.Macro_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(Jinja2Parser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(Jinja2Parser.ParameterContext ctx);
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
	 * Visit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(Jinja2Parser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpression(Jinja2Parser.BinaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(Jinja2Parser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDTrFlExpression(Jinja2Parser.IDTrFlExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(Jinja2Parser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(Jinja2Parser.FilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(Jinja2Parser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(Jinja2Parser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ID}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitID(Jinja2Parser.IDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(Jinja2Parser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(Jinja2Parser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code None}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNone(Jinja2Parser.NoneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(Jinja2Parser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code List}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(Jinja2Parser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionary(Jinja2Parser.DictionaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#listdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListdef(Jinja2Parser.ListdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link Jinja2Parser#dictdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictdef(Jinja2Parser.DictdefContext ctx);
}