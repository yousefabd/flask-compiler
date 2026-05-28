package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;

import java.util.List;

public abstract class BodyStatementNode extends StatementNode {

    private final List<ContentNode> body;

    protected BodyStatementNode(List<ContentNode> body, int lineNumber) {
        super(lineNumber);
        this.body = body;
    }

    public List<ContentNode> getBody() {
        return body;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return body;
    }
}