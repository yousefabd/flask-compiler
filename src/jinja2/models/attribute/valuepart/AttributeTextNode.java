package jinja2.models.attribute.valuepart;

import jinja2.models.TemplateNode;

import java.util.List;

public class AttributeTextNode extends AttributeValuePartNode {

    private final String text;

    public AttributeTextNode(String text, int lineNumber) {
        super(lineNumber);
        this.text = text;
    }

    public String getText() { return text; }

    @Override
    public List<? extends TemplateNode> getChildren() { return List.of(); }

}