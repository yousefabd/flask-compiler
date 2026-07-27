// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/html/HTMLParser.g4 by ANTLR 4.13.2

    package antlr.html;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HTMLParser}.
 */
public interface HTMLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HTMLParser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(HTMLParser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(HTMLParser.TemplateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterVariableStatement(HTMLParser.VariableStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitVariableStatement(HTMLParser.VariableStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterStatement(HTMLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitStatement(HTMLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterInlineStatement(HTMLParser.InlineStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitInlineStatement(HTMLParser.InlineStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterHtmlStatement(HTMLParser.HtmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitHtmlStatement(HTMLParser.HtmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Text}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterText(HTMLParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Text}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitText(HTMLParser.TextContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterHtmlElement(HTMLParser.HtmlElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitHtmlElement(HTMLParser.HtmlElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#normalElement}.
	 * @param ctx the parse tree
	 */
	void enterNormalElement(HTMLParser.NormalElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#normalElement}.
	 * @param ctx the parse tree
	 */
	void exitNormalElement(HTMLParser.NormalElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#beginTag}.
	 * @param ctx the parse tree
	 */
	void enterBeginTag(HTMLParser.BeginTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#beginTag}.
	 * @param ctx the parse tree
	 */
	void exitBeginTag(HTMLParser.BeginTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#endTag}.
	 * @param ctx the parse tree
	 */
	void enterEndTag(HTMLParser.EndTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#endTag}.
	 * @param ctx the parse tree
	 */
	void exitEndTag(HTMLParser.EndTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#voidElement}.
	 * @param ctx the parse tree
	 */
	void enterVoidElement(HTMLParser.VoidElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#voidElement}.
	 * @param ctx the parse tree
	 */
	void exitVoidElement(HTMLParser.VoidElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(HTMLParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(HTMLParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(HTMLParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(HTMLParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(HTMLParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(HTMLParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(HTMLParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(HTMLParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(HTMLParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(HTMLParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterSetStatement(HTMLParser.SetStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitSetStatement(HTMLParser.SetStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroStatement(HTMLParser.MacroStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroStatement(HTMLParser.MacroStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(HTMLParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(HTMLParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineExtendsStatement(HTMLParser.InlineExtendsStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineExtendsStatement(HTMLParser.InlineExtendsStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineIncludeStatement(HTMLParser.InlineIncludeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineIncludeStatement(HTMLParser.InlineIncludeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInlineSetStatement(HTMLParser.InlineSetStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInlineSetStatement(HTMLParser.InlineSetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#for_block}.
	 * @param ctx the parse tree
	 */
	void enterFor_block(HTMLParser.For_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#for_block}.
	 * @param ctx the parse tree
	 */
	void exitFor_block(HTMLParser.For_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#if_block}.
	 * @param ctx the parse tree
	 */
	void enterIf_block(HTMLParser.If_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#if_block}.
	 * @param ctx the parse tree
	 */
	void exitIf_block(HTMLParser.If_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(HTMLParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(HTMLParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#set_inline}.
	 * @param ctx the parse tree
	 */
	void enterSet_inline(HTMLParser.Set_inlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#set_inline}.
	 * @param ctx the parse tree
	 */
	void exitSet_inline(HTMLParser.Set_inlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#set_block}.
	 * @param ctx the parse tree
	 */
	void enterSet_block(HTMLParser.Set_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#set_block}.
	 * @param ctx the parse tree
	 */
	void exitSet_block(HTMLParser.Set_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#macro_block}.
	 * @param ctx the parse tree
	 */
	void enterMacro_block(HTMLParser.Macro_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#macro_block}.
	 * @param ctx the parse tree
	 */
	void exitMacro_block(HTMLParser.Macro_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(HTMLParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(HTMLParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(HTMLParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(HTMLParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#block_block}.
	 * @param ctx the parse tree
	 */
	void enterBlock_block(HTMLParser.Block_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#block_block}.
	 * @param ctx the parse tree
	 */
	void exitBlock_block(HTMLParser.Block_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#extends_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExtends_stmt(HTMLParser.Extends_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#extends_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExtends_stmt(HTMLParser.Extends_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInclude_stmt(HTMLParser.Include_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#include_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInclude_stmt(HTMLParser.Include_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(HTMLParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(HTMLParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpression(HTMLParser.BinaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpression(HTMLParser.BinaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(HTMLParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(HTMLParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIDTrFlExpression(HTMLParser.IDTrFlExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIDTrFlExpression(HTMLParser.IDTrFlExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(HTMLParser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(HTMLParser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(HTMLParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(HTMLParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(HTMLParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(HTMLParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(HTMLParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(HTMLParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(HTMLParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(HTMLParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ID}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterID(HTMLParser.IDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ID}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitID(HTMLParser.IDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(HTMLParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(HTMLParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNumber(HTMLParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNumber(HTMLParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code None}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNone(HTMLParser.NoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code None}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNone(HTMLParser.NoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterString(HTMLParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitString(HTMLParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code List}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterList(HTMLParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code List}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitList(HTMLParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterDictionary(HTMLParser.DictionaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitDictionary(HTMLParser.DictionaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#listdef}.
	 * @param ctx the parse tree
	 */
	void enterListdef(HTMLParser.ListdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#listdef}.
	 * @param ctx the parse tree
	 */
	void exitListdef(HTMLParser.ListdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#dictdef}.
	 * @param ctx the parse tree
	 */
	void enterDictdef(HTMLParser.DictdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#dictdef}.
	 * @param ctx the parse tree
	 */
	void exitDictdef(HTMLParser.DictdefContext ctx);
}