package jinja2.models.content;

import jinja2.models.TemplateNode;
import jinja2.models.expression.ExpressionNode;

import java.util.List;

/*
* variable
    : DOUBLE_OPEN_BRACE MINUS? expr MINUS? DOUBLE_CLOSE_BRACE
 */
public class OutputNode extends ContentNode {

    private final ExpressionNode expression;

    public OutputNode(
            ExpressionNode expression,
            int lineNumber) {

        super(lineNumber);

        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return List.of(expression);
    }
}
