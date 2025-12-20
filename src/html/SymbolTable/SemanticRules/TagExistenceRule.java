package html.SymbolTable.SemanticRules;

import html.models.ElementNode;
import html.models.Node;

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
    public void validate(Node root, List<String> errors) {
        visit(root, errors);
    }

    private void visit(Node node, List<String> errors) {
        if (node instanceof ElementNode element) {

            if (!VALID_TAGS.contains(element.getTagName())) {
                errors.add("Unknown HTML tag: <" + element.getTagName() + ">" + " at line " + element.getLine());
            }

            for (Node child : element.getChildren()) {
                visit(child, errors);
            }
        }
    }
}
