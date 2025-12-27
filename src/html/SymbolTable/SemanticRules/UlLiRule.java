package html.SymbolTable.SemanticRules;

import html.models.ElementNode;
import html.models.Node;
import html.models.NormalElementNode;
import html.models.VoidElementNode;

import java.util.List;

public class UlLiRule implements ISemanticRule{

    @Override
    public void validate(ElementNode root, List<String> errors) {
        visit(root, null, errors);
    }

    private void visit(ElementNode node, VoidElementNode parent, List<String> errors) {
        if (node instanceof VoidElementNode element) {
            if (parent != null &&
                    parent.getTagName().equals("ul") &&
                    !element.getTagName().equals("li")) {

                errors.add(
                        "<ul> can only contain <li> but found <" +
                                element.getTagName() + ">" +
                        " at line " + element.getLine()
                );
            }
            if(!(element instanceof NormalElementNode normalElement))
                return;
            for (ElementNode child : normalElement.getChildren()) {
                visit(child, element, errors);
            }
        }
    }
}
