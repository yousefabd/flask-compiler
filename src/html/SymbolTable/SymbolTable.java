package html.SymbolTable;

import html.models.ElementNode;
import html.models.VoidElementNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    // Declaration: id="x"
    public final Map<String, VoidElementNode> ids = new HashMap<>();

    public final List<Reference> references = new ArrayList<>();

    public static class Reference{
        public String id;
        public VoidElementNode source;
        public Reference(String id, VoidElementNode source) {
            this.id = id;
            this.source = source;
        }
    }
}
