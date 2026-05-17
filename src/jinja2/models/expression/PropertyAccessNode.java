package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class PropertyAccessNode extends ExpressionNode {

    private final ExpressionNode target;

    private final IdentifierNode property;

    public PropertyAccessNode(
            ExpressionNode target,
            IdentifierNode property,
            int lineNumber) {

        super(lineNumber);

        this.target = target;
        this.property = property;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public IdentifierNode getProperty() {
        return property;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return List.of(target, property);
    }

}