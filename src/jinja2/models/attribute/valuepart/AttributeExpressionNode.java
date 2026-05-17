package jinja2.models.attribute.valuepart;

import jinja2.models.TemplateNode;
import jinja2.models.expression.ExpressionNode;

import java.util.List;

public class AttributeExpressionNode extends AttributeValuePartNode {

    private final ExpressionNode expression;

    public AttributeExpressionNode(ExpressionNode expression, int lineNumber) {
        super(lineNumber);
        this.expression = expression;
    }

    public ExpressionNode getExpression() { return expression; }

    @Override
    public List<? extends TemplateNode> getChildren() { return List.of(expression); }

    @Override
    public String describe() {
        return getNodeName() + " (line " + getLineNumber() + ")";
    }
}