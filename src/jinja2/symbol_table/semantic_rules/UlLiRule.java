package jinja2.symbol_table.semantic_rules;

import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
import jinja2.models.content.html.HTMLNormalElementNode;
import jinja2.models.content.html.HTMLVoidElementNode;
import jinja2.models.statement.*;
import jinja2.symbol_table.CompilerError;

import java.util.List;


public class UlLiRule implements ISemanticRule {

    @Override
    public void validate(SemanticContext semanticContext) {
        for (ContentNode child : semanticContext.root().getContentChildren())
            visitContent(child, null, semanticContext.errors());
    }

    /**
     * @param parentListTag  "ul" or "ol" if we're currently inside one, null otherwise
     */
    private void visitContent(ContentNode node, String parentListTag,
                              List<CompilerError> errors) {

        // ── Jinja2 statements — not HTML, always allowed inside <ul>/<ol>
        //    but recurse into their bodies carrying the parentListTag context
        if (node instanceof BodyStatementNode bs) {
            for (ContentNode child : bs.getBody())
                visitContent(child, parentListTag, errors);
            return;
        }
        if (node instanceof IfStatementNode is) {
            for (IfBranchNode branch : is.getBranches())
                for (ContentNode child : branch.getBody())
                    visitContent(child, parentListTag, errors);
            return;
        }
        if (node instanceof SetStatementNode ss && ss.isBlock()) {
            for (ContentNode child : ss.getBody())
                visitContent(child, parentListTag, errors);
            return;
        }

        // ── {{ expr }} directly inside <ul> renders raw text — flag it
        if (node instanceof OutputNode && parentListTag != null) {
            errors.add(new CompilerError(
                    CompilerError.Kind.INVALID_HTML_STRUCTURE,
                    "{{ }} output directly inside <" + parentListTag + "> — wrap it in <li>",
                    node.getLineNumber()));
            return;
        }

        // ── HtmlTextNode (whitespace/newlines) — always fine, ignore
        if (node instanceof HtmlTextNode) return;

        // ── HTML elements
        if (!(node instanceof HTMLVoidElementNode element)) return;

        if (parentListTag != null
                && !element.getTagName().equalsIgnoreCase("li")) {
            errors.add(new CompilerError(
                    CompilerError.Kind.INVALID_HTML_STRUCTURE,
                    "<" + parentListTag + "> can only contain <li>"
                            + " but found <" + element.getTagName() + ">",
                    element.getLineNumber()));
        }

        if (!(element instanceof HTMLNormalElementNode normal)) return;

        // determine the list context for children
        String listContext = isListTag(element.getTagName())
                ? element.getTagName().toLowerCase()
                : null;

        for (ContentNode child : normal.getChildren())
            visitContent(child, listContext, errors);
    }

    private boolean isListTag(String tagName) {
        return tagName.equalsIgnoreCase("ul")
                || tagName.equalsIgnoreCase("ol");
    }
}