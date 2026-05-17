package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class IndexAccessNode extends ExpressionNode {

    private final ExpressionNode target;

    private final ExpressionNode index;

    public IndexAccessNode(
            ExpressionNode target,
            ExpressionNode index,
            int lineNumber) {

        super(lineNumber);

        this.target = target;
        this.index = index;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return List.of(target, index);
    }

}