package html.SymbolTable;

import html.models.ElementNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    // Declaration: id="x"
    public final Map<String, ElementNode> ids = new HashMap<>();

    public final List<Reference> references = new ArrayList<>();

    public static class Reference{
        public String id;
        public ElementNode source;
        public Reference(String id, ElementNode source) {
            this.id = id;
            this.source = source;
        }
    }
}
