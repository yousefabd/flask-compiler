// Generated from C:/Users/yahia/IdeaProjects/flaskcomp/grammars/css/CSSParser.g4 by ANTLR 4.13.2

    package antlr.css;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CSSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CSSParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CSSParser#stylesheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylesheet(CSSParser.StylesheetContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#ruleset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleset(CSSParser.RulesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#selectorGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorGroup(CSSParser.SelectorGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(CSSParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescendant(CSSParser.DescendantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code child}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChild(CSSParser.ChildContext ctx);
	/**
	 * Visit a parse tree produced by the {@code adjacent}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdjacent(CSSParser.AdjacentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sibling}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSibling(CSSParser.SiblingContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#selectorSequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorSequence(CSSParser.SelectorSequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#typeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSelector(CSSParser.TypeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#selectorSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorSuffix(CSSParser.SelectorSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#idSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdSelector(CSSParser.IdSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#classSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassSelector(CSSParser.ClassSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#attributeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeSelector(CSSParser.AttributeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PseudoClass}
	 * labeled alternative in {@link CSSParser#pseudo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoClass(CSSParser.PseudoClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PseudoClassWithArgs}
	 * labeled alternative in {@link CSSParser#pseudo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoClassWithArgs(CSSParser.PseudoClassWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PseudoElement}
	 * labeled alternative in {@link CSSParser#pseudo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoElement(CSSParser.PseudoElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#pseudoArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoArgument(CSSParser.PseudoArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#declarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationList(CSSParser.DeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(CSSParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(CSSParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(CSSParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#valuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuePart(CSSParser.ValuePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#function_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_(CSSParser.Function_Context ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(CSSParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSSParser#ws}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWs(CSSParser.WsContext ctx);
}