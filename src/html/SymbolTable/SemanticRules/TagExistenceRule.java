package html.SymbolTable.SemanticRules;

import html.models.ElementNode;
import html.models.Node;
import html.models.NormalElementNode;
import html.models.VoidElementNode;

import java.util.List;
import java.util.Set;

public class TagExistenceRule implements ISemanticRule {

    private static final Set<String> VALID_TAGS = Set.of(
            "html", "head", "body",
            "div", "span", "p",
            "ul", "li",
            "a", "img",
            "form", "input", "label",
            "h1", "h2", "h3"
    );

    @Override
    public void validate(ElementNode root, List<String> errors) {
        visit(root, errors);
    }

    private void visit(ElementNode node, List<String> errors) {
        if (node instanceof VoidElementNode element) {

            if (!VALID_TAGS.contains(element.getTagName())) {
                errors.add("Unknown HTML tag: <" + element.getTagName() + ">" + " at line " + element.getLine());
            }
            if(!(node instanceof NormalElementNode normalElement))
                return;
            for (ElementNode child : normalElement.getChildren()) {
                visit(child, errors);
            }
        }
    }
}
