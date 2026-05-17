package jinja2.models.attribute.valuepart;

import jinja2.models.TemplateNode;

public abstract class AttributeValuePartNode extends TemplateNode {

    protected AttributeValuePartNode(int lineNumber) {
        super(lineNumber);
    }
}