package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;

import java.util.ArrayList;
import java.util.List;

public class ForStatementNode extends BodyStatementNode  {

    private final List<IdentifierNode> variables;
    private final ExpressionNode iterable;

    public ForStatementNode(
            List<IdentifierNode> variables,
            ExpressionNode iterable,
            List<ContentNode> body,
            int lineNumber
    ) {
        super(body, lineNumber);
        if (variables == null || variables.isEmpty()) {
            throw new IllegalArgumentException(
                    "A for statement must contain at least one loop variable"
            );
        }

        this.variables = List.copyOf(variables);
        this.iterable = iterable;
    }

    public List<IdentifierNode> getVariables() {
        return variables;
    }
    public ExpressionNode   getIterable() { return iterable; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>(variables);
        children.add(iterable);
        children.addAll(getBody());
        return children;
    }

    @Override
    public String describe() {
        return getNodeName() + " (line " + getLineNumber() + ")";
    }
}