package jinja2.models.statement;

import jinja2.models.TemplateNode;

import java.util.List;

public class ExtendsStatementNode extends StatementNode {

    private final String path;

    public ExtendsStatementNode(String path, int lineNumber) {
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