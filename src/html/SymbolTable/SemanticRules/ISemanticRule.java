package html.SymbolTable.SemanticRules;

import html.models.Node;

import java.util.List;

public interface ISemanticRule {
    public void validate(Node root, List<String> errors);
}
