package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;

import java.util.ArrayList;
import java.util.List;

public class ForStatementNode extends BodyStatementNode  {

    private final IdentifierNode variable;
    private final ExpressionNode iterable;

    public ForStatementNode(IdentifierNode variable, ExpressionNode iterable,
                            List<ContentNode> body, int lineNumber) {
        super(body, lineNumber);
        this.variable = variable;
        this.iterable = iterable;
    }

    public IdentifierNode   getVariable() { return variable; }
    public ExpressionNode   getIterable() { return iterable; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        children.add(variable);
        children.add(iterable);
        children.addAll(super.getBody());
        return children;
    }

    @Override
    public String describe() {
        return getNodeName() + " (line " + getLineNumber() + ")";
    }
}