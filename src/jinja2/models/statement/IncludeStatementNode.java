package jinja2.models.statement;

import jinja2.models.TemplateNode;

import java.util.List;

public class IncludeStatementNode extends StatementNode {

    private final String path;

    public IncludeStatementNode(String path, int lineNumber) {
        super(lineNumber);
        this.path = path;
    }

    public String getPath() { return path; }

    @Override
    public List<? extends TemplateNode> getChildren() { return List.of(); }

    @Override
    public String describe() {
        return getNodeName() + " \"" + path + "\" (line " + getLineNumber() + ")";
    }
}