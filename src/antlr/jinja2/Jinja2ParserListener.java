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
	 * Enter a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void enterVariableStatement(Jinja2Parser.VariableStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void exitVariableStatement(Jinja2Parser.VariableStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void enterStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void exitStatement(Jinja2Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void enterInlineStatement(Jinja2Parser.InlineStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void exitInlineStatement(Jinja2Parser.InlineStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Text}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void enterText(Jinja2Parser.TextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Text}
	 * labeled alternative in {@link Jinja2Parser#tag}.
	 * @param ctx the parse tree
	 */
	void exitText(Jinja2Parser.TextContext ctx);
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
	 * Enter a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(Jinja2Parser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(Jinja2Parser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(Jinja2Parser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(Jinja2Parser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterSetStatement(Jinja2Parser.SetStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitSetStatement(Jinja2Parser.SetStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroStatement(Jinja2Parser.MacroStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroStatement(Jinja2Parser.MacroStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(Jinja2Parser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link Jinja2Parser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(Jinja2Parser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineExtendsStatement(Jinja2Parser.InlineExtendsStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineExtendsStatement(Jinja2Parser.InlineExtendsStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineIncludeStatement(Jinja2Parser.InlineIncludeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineIncludeStatement(Jinja2Parser.InlineIncludeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineSetStatement(Jinja2Parser.InlineSetStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link Jinja2Parser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineSetStatement(Jinja2Parser.InlineSetStatementContext ctx);
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
	 * Enter a parse tree produced by {@link Jinja2Parser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(Jinja2Parser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(Jinja2Parser.BodyContext ctx);
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
	 * Enter a parse tree produced by {@link Jinja2Parser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(Jinja2Parser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(Jinja2Parser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(Jinja2Parser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(Jinja2Parser.ParameterContext ctx);
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
	 * Enter a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(Jinja2Parser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(Jinja2Parser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpression(Jinja2Parser.BinaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpression(Jinja2Parser.BinaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(Jinja2Parser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(Jinja2Parser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIDTrFlExpression(Jinja2Parser.IDTrFlExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link Jinja2Parser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIDTrFlExpression(Jinja2Parser.IDTrFlExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(Jinja2Parser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(Jinja2Parser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(Jinja2Parser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(Jinja2Parser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(Jinja2Parser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(Jinja2Parser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(Jinja2Parser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(Jinja2Parser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(Jinja2Parser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(Jinja2Parser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ID}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterID(Jinja2Parser.IDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ID}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitID(Jinja2Parser.IDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(Jinja2Parser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(Jinja2Parser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNumber(Jinja2Parser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNumber(Jinja2Parser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code None}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNone(Jinja2Parser.NoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code None}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNone(Jinja2Parser.NoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterString(Jinja2Parser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitString(Jinja2Parser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code List}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterList(Jinja2Parser.ListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code List}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitList(Jinja2Parser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterDictionary(Jinja2Parser.DictionaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link Jinja2Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitDictionary(Jinja2Parser.DictionaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#listdef}.
	 * @param ctx the parse tree
	 */
	void enterListdef(Jinja2Parser.ListdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#listdef}.
	 * @param ctx the parse tree
	 */
	void exitListdef(Jinja2Parser.ListdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link Jinja2Parser#dictdef}.
	 * @param ctx the parse tree
	 */
	void enterDictdef(Jinja2Parser.DictdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link Jinja2Parser#dictdef}.
	 * @param ctx the parse tree
	 */
	void exitDictdef(Jinja2Parser.DictdefContext ctx);
}