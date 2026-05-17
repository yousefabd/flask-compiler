package jinja2.models.attribute;

import jinja2.models.TemplateNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;

import java.util.List;
public class HtmlAttributeNode extends TemplateNode {

    private final String name;

    private final List<AttributeValuePartNode> valueParts;

    public HtmlAttributeNode(
            String name,
            List<AttributeValuePartNode> valueParts,
            int lineNumber) {

        super(lineNumber);

        this.name = name;
        this.valueParts = valueParts;
    }

    public String getName() {
        return name;
    }

    public List<AttributeValuePartNode> getValueParts() {
        return valueParts;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return valueParts;
    }

}