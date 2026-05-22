package jinja2.models.content.html;

import jinja2.models.TemplateNode;
import jinja2.models.attribute.HtmlAttributeNode;

import java.util.ArrayList;
import java.util.List;

public class HTMLVoidElementNode extends HTMLNode {
    private final String tagName;
    private final List<HtmlAttributeNode> attributes;

    public HTMLVoidElementNode(String tagName,int line){
        super(line);
        this.tagName = tagName;
        attributes = new ArrayList<>();
    }

    public void addAttribute(HtmlAttributeNode attribute){
        this.attributes.add(attribute);
    }
    public List<HtmlAttributeNode> getAttributes(){
        return attributes;
    }
    public String getTagName(){
        return tagName;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return List.of();
    }

    @Override
    public String describe() {
        // super.describe() + attributes
        StringBuilder sb = new StringBuilder();
        sb.append(super.describe());
        for (HtmlAttributeNode attr : attributes) {
            sb.append(" ").append(attr.describe());
        }
        return sb.toString();
    }
}
