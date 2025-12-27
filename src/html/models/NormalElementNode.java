package html.models;

import java.util.ArrayList;
import java.util.List;

public class NormalElementNode extends VoidElementNode {
    private final List<ElementNode> children;

    public NormalElementNode(String tagName,int line) {
        super(tagName,line);
        children = new ArrayList<>();
    }
    public void addChild(ElementNode child){
        children.add(child);
    }
    public List<ElementNode> getChildren(){
        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        for (var child : children){
            sb.append(child.toString());
        }
        sb.append("</").append(getTagName()).append(">");
        return sb.toString();
    }
}
