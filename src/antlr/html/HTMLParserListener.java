// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/html/HTMLParser.g4 by ANTLR 4.13.2

    package antlr.html;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HTMLParser}.
 */
public interface HTMLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void enterHtmlDocument(HTMLParser.HtmlDocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void exitHtmlDocument(HTMLParser.HtmlDocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(HTMLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(HTMLParser.ElementContext ctx);
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
}