package html.SymbolTable.SemanticRules;

import html.SymbolTable.SymbolTable;
import html.models.ElementNode;
import html.models.Node;
import html.models.VoidElementNode;

import java.util.List;

public class BrokenReferenceRule implements ISemanticRule{
    private final SymbolTable table;

    public BrokenReferenceRule(SymbolTable table) {
        this.table = table;
    }

    @Override
    public void validate(ElementNode root, List<String> errors) {

        for (SymbolTable.Reference ref : table.references) {
            if (!table.ids.containsKey(ref.id)) {
                errors.add(
                        "Broken reference '#" + ref.id +
                                "' in <" + ref.source.getTagName() + ">" +
                                " at line " + ref.source.getLine()
                );
            }
        }
    }
}
