package html;

import html.models.ElementNode;
import html.models.Node;

public record HTMLDocument(ElementNode root) implements Node {
    public int getLine(){
        return root.getLine();
    }
}
