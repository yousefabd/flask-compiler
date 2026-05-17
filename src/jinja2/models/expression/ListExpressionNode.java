package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class ListExpressionNode extends ExpressionNode {

    private final List<ExpressionNode> elements;

    public ListExpressionNode(List<ExpressionNode> elements, int lineNumber) {
        super(lineNumber);
        this.elements = elements;
    }

    public List<ExpressionNode> getElements() { return elements; }

    @Override
    public List<? extends TemplateNode> getChildren() { return elements; }

}