package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;

import java.util.ArrayList;
import java.util.List;

public class ForStatementNode extends StatementNode {

    private final IdentifierNode variable;
    private final ExpressionNode iterable;
    private final List<ContentNode> body;

    public ForStatementNode(
            IdentifierNode variable,
            ExpressionNode iterable,
            List<ContentNode> body,
            int lineNumber) {
        super(lineNumber);
        this.variable = variable;
        this.iterable = iterable;
        this.body     = body;
    }

    public IdentifierNode   getVariable() { return variable; }
    public ExpressionNode   getIterable() { return iterable; }
    public List<ContentNode> getBody()    { return body; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        children.add(variable);
        children.add(iterable);
        children.addAll(body);
        return children;
    }

    @Override
    public String describe() {
        return getNodeName() + " (line " + getLineNumber() + ")";
    }
}