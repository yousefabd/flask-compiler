package html.models;

import java.util.ArrayList;
import java.util.List;

public class VoidElementNode extends ElementNode {
    private final String tagName;
    private final List<AttributeNode> attributes;

    public VoidElementNode(String tagName,int line){
        this.tagName = tagName;
        this.line = line;
        attributes = new ArrayList<>();
    }

    public void addAttribute(AttributeNode attribute){
        this.attributes.add(attribute);
    }
    public List<AttributeNode> getAttributes(){
        return attributes;
    }
    public String getTagName(){
        return tagName;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        for (var attribute : attributes){
            sb.append(" ").append(attribute.toString());
        }
        sb.append(">");
        return sb.toString();
    }
}
