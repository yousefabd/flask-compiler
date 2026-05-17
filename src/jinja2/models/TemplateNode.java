package jinja2.models;

import java.util.List;

public abstract class TemplateNode {
    private final String nodeName;
    private final int lineNumber;

    protected TemplateNode(int lineNumber) {
        this.lineNumber = lineNumber;
        this.nodeName = getClass().getSimpleName();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String describe() {
        return getNodeName() + " (line " + lineNumber + ")";
    }

    public abstract List<? extends TemplateNode> getChildren();

    @Override
    public String toString() {
        return describe();
    }
}

