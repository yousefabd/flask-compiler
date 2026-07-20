package jinja2.models.content.html;
import jinja2.models.content.ContentNode;

import java.util.ArrayList;
import java.util.List;

public class HTMLNormalElementNode extends HTMLVoidElementNode{
    private final List<ContentNode> children;

    public HTMLNormalElementNode(String tagName,int line) {
        super(tagName,line);
        children = new ArrayList<>();
    }
    public void addChild(ContentNode child){
        children.add(child);
    }

    @Override
    public List<ContentNode> getChildren() {
        return children;
    }

}
