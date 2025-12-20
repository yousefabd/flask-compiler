package html.models;

import java.util.ArrayList;
import java.util.List;

public class ElementNode implements Node{
    private final String tagName;
    private final List<AttributeNode> attributes;
    private final List<Node> children;
    private final int line;
    public enum Type{
        Normal,
        Void,
        Textual
    }
    private Type type;
    public ElementNode(String tagName, Type type,int line){
        this.tagName = tagName;
        this.type = type;
        this.attributes = new ArrayList<>();
        this.children = new ArrayList<>();
        this.line = line;
    }
    public String getTagName() {
        return tagName;
    }
    public int getLine(){
        return line;
    }
    public void AddAttribute(AttributeNode attribute){
        this.attributes.add(attribute);
    }
    public void AddChild(Node child){
        this.children.add(child);
    }
    public List<AttributeNode> getAttributes() {
        return attributes;
    }
    public List<Node> getChildren() {
        return children;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        for(AttributeNode attr : attributes){
            sb.append(" ").append(attr.toString());
        }
        if(type == Type.Void){
            sb.append(" />\n");
            return sb.toString();
        }
        sb.append(">\n");
        for(Node child : children){
            sb.append(child.toString());
        }
        sb.append("</").append(tagName).append(">\n");
        return sb.toString();
    }

}
