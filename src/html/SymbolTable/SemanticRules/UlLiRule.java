package html.SymbolTable.SemanticRules;

import html.models.ElementNode;
import html.models.Node;

import java.util.List;

public class UlLiRule implements ISemanticRule{

    @Override
    public void validate(Node root, List<String> errors) {
        visit(root, null, errors);
    }

    private void visit(Node node, ElementNode parent, List<String> errors) {
        if (node instanceof ElementNode element) {
            if (parent != null &&
                    parent.getTagName().equals("ul") &&
                    !element.getTagName().equals("li")) {

                errors.add(
                        "<ul> can only contain <li> but found <" +
                                element.getTagName() + ">" +
                        " at line " + element.getLine()
                );
            }

            for (Node child : element.getChildren()) {
                visit(child, element, errors);
            }
        }
    }
}
