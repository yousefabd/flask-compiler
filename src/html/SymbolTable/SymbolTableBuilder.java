package html.SymbolTable;

import html.models.AttributeNode;
import html.models.ElementNode;
import html.models.Node;

import java.util.List;

public class SymbolTableBuilder {

    private final SymbolTable symbols;
    private final List<String> errors;

    public SymbolTableBuilder(SymbolTable symbols, List<String> errors) {
        this.symbols = symbols;
        this.errors = errors;
    }

    public void build(Node root) {
        visit(root);
    }

    private void visit(Node node) {
        if (!(node instanceof ElementNode element)) return;

        for (AttributeNode attr : element.getAttributes()) {
            if (attr.name().equalsIgnoreCase("id")
                    && attr.value().isPresent()) {

                String id = stripQuotes(attr.value().get());

                if (symbols.ids.containsKey(id)) {
                    errors.add("Duplicate id '" + id +
                            "' at line " + element.getLine()
                    );
                } else {
                    symbols.ids.put(id, element);
                }
            }

            // collect references only — no validation here
            if (attr.name().equalsIgnoreCase("href")
                    && attr.value().isPresent()) {

                String value = stripQuotes(attr.value().get());
                if (value.startsWith("#")) {
                    symbols.references.add(
                            new SymbolTable.Reference(
                                    value.substring(1), element
                            )
                    );
                }
            }
        }

        for (Node child : element.getChildren()) {
            visit(child);
        }
    }

    private String stripQuotes(String value) {
        if (value.length() >= 2 &&
                (value.startsWith("\"") && value.endsWith("\"") ||
                        value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}

