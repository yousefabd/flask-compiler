package jinja2.models.file;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;

import java.util.List;

public final class TemplateFile extends TemplateNode {

    private final List<ContentNode> children;

    public TemplateFile(
            List<ContentNode> children,
            int lineNumber) {

        super(lineNumber);

        this.children = children;
    }

    public List<ContentNode> getContentChildren() {
        return children;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ContentNode child : children) {
            sb.append(child.describe());
        }
        return sb.toString();
    }
}