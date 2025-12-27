package html.SymbolTable.SemanticRules;

import html.models.ElementNode;
import html.models.Node;
import html.models.VoidElementNode;

import java.util.List;

public interface ISemanticRule {
    public void validate(ElementNode root, List<String> errors);
}
