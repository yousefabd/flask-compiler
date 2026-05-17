package jinja2.models.content;

import jinja2.models.TemplateNode;

public abstract class ContentNode extends TemplateNode {

    protected ContentNode(int lineNumber) {
        super(lineNumber);
    }
}