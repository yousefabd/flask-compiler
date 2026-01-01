// Generated from C:/Users/youus/IdeaProjects/flask-compiler/src/antlr/css/CSSParser.g4 by ANTLR 4.13.2
package antlr.css;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CSSParser}.
 */
public interface CSSParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(CSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(CSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void enterRuleset(CSSParser.RulesetContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void exitRuleset(CSSParser.RulesetContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#selectorGroup}.
	 * @param ctx the parse tree
	 */
	void enterSelectorGroup(CSSParser.SelectorGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#selectorGroup}.
	 * @param ctx the parse tree
	 */
	void exitSelectorGroup(CSSParser.SelectorGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(CSSParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(CSSParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void enterDescendant(CSSParser.DescendantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code descendant}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void exitDescendant(CSSParser.DescendantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code child}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void enterChild(CSSParser.ChildContext ctx);
	/**
	 * Exit a parse tree produced by the {@code child}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void exitChild(CSSParser.ChildContext ctx);
	/**
	 * Enter a parse tree produced by the {@code adjacent}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void enterAdjacent(CSSParser.AdjacentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code adjacent}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void exitAdjacent(CSSParser.AdjacentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sibling}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void enterSibling(CSSParser.SiblingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sibling}
	 * labeled alternative in {@link CSSParser#combinator}.
	 * @param ctx the parse tree
	 */
	void exitSibling(CSSParser.SiblingContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#simpleSelectorSequence}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelectorSequence(CSSParser.SimpleSelectorSequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#simpleSelectorSequence}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelectorSequence(CSSParser.SimpleSelectorSequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#typeSelector}.
	 * @param ctx the parse tree
	 */
	void enterTypeSelector(CSSParser.TypeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#typeSelector}.
	 * @param ctx the parse tree
	 */
	void exitTypeSelector(CSSParser.TypeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#selectorSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSelectorSuffix(CSSParser.SelectorSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#selectorSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSelectorSuffix(CSSParser.SelectorSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#idSelector}.
	 * @param ctx the parse tree
	 */
	void enterIdSelector(CSSParser.IdSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#idSelector}.
	 * @param ctx the parse tree
	 */
	void exitIdSelector(CSSParser.IdSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#classSelector}.
	 * @param ctx the parse tree
	 */
	void enterClassSelector(CSSParser.ClassSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#classSelector}.
	 * @param ctx the parse tree
	 */
	void exitClassSelector(CSSParser.ClassSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSelector(CSSParser.AttributeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSelector(CSSParser.AttributeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#pseudo}.
	 * @param ctx the parse tree
	 */
	void enterPseudo(CSSParser.PseudoContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#pseudo}.
	 * @param ctx the parse tree
	 */
	void exitPseudo(CSSParser.PseudoContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#pseudoArgument}.
	 * @param ctx the parse tree
	 */
	void enterPseudoArgument(CSSParser.PseudoArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#pseudoArgument}.
	 * @param ctx the parse tree
	 */
	void exitPseudoArgument(CSSParser.PseudoArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationList(CSSParser.DeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationList(CSSParser.DeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(CSSParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(CSSParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(CSSParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(CSSParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(CSSParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(CSSParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void enterValuePart(CSSParser.ValuePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void exitValuePart(CSSParser.ValuePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSSParser#function_}.
	 * @param ctx the parse tree
	 */
	void enterFunction_(CSSParser.Function_Context ctx);
	/**
	 * Exit a parse tree produced by {@link CSSParser#function_}.
	 * @param ctx the parse tree
	 */
	void exitFunction_(CSSParser.Function_Context ctx);
}