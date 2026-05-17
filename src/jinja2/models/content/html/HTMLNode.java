package jinja2.models.content.html;

import jinja2.models.content.ContentNode;

public abstract class HTMLNode extends ContentNode {
    protected HTMLNode(int lineNumber) {
        super(lineNumber);
    }
}
