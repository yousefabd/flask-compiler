package jinja2.models.expression.literal;

import jinja2.models.expression.ExpressionNode;

public abstract class LiteralExpressionNode extends ExpressionNode {

    protected LiteralExpressionNode(int lineNumber) {
        super(lineNumber);
    }
}