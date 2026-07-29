// Generated from C:/Users/Yousef.Abdulmonaem/Documents/GitHub/flask-compiler/grammars/html/HTMLParser.g4 by ANTLR 4.13.2

    package antlr.html;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HTMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HTMLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HTMLParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(HTMLParser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableStatement(HTMLParser.VariableStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(HTMLParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineStatement(HTMLParser.InlineStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code HtmlStatement}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlStatement(HTMLParser.HtmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Text}
	 * labeled alternative in {@link HTMLParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(HTMLParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlElement(HTMLParser.HtmlElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#normalElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalElement(HTMLParser.NormalElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#beginTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginTag(HTMLParser.BeginTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#endTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndTag(HTMLParser.EndTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#voidElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidElement(HTMLParser.VoidElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(HTMLParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#attributeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeName(HTMLParser.AttributeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(HTMLParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(HTMLParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(HTMLParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStatement(HTMLParser.SetStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MacroStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroStatement(HTMLParser.MacroStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockStatement}
	 * labeled alternative in {@link HTMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(HTMLParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineExtendsStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineExtendsStatement(HTMLParser.InlineExtendsStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineIncludeStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineIncludeStatement(HTMLParser.InlineIncludeStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineSetStatement}
	 * labeled alternative in {@link HTMLParser#inline_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineSetStatement(HTMLParser.InlineSetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#for_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_block(HTMLParser.For_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#if_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_block(HTMLParser.If_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(HTMLParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#set_inline}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_inline(HTMLParser.Set_inlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#set_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_block(HTMLParser.Set_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#macro_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_block(HTMLParser.Macro_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(HTMLParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(HTMLParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#block_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_block(HTMLParser.Block_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#extends_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtends_stmt(HTMLParser.Extends_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#include_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_stmt(HTMLParser.Include_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(HTMLParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpression(HTMLParser.BinaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TestExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestExpression(HTMLParser.TestExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(HTMLParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDTrFlExpression}
	 * labeled alternative in {@link HTMLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDTrFlExpression(HTMLParser.IDTrFlExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#testInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestInvocation(HTMLParser.TestInvocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#testName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestName(HTMLParser.TestNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#testArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestArguments(HTMLParser.TestArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(HTMLParser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(HTMLParser.FilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(HTMLParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(HTMLParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(HTMLParser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ID}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitID(HTMLParser.IDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(HTMLParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(HTMLParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code None}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNone(HTMLParser.NoneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(HTMLParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code List}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(HTMLParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dictionary}
	 * labeled alternative in {@link HTMLParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictionary(HTMLParser.DictionaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#listdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListdef(HTMLParser.ListdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HTMLParser#dictdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictdef(HTMLParser.DictdefContext ctx);
}