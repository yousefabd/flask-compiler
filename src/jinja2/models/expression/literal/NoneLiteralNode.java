package jinja2.models.expression.literal;

import jinja2.models.TemplateNode;

import java.util.Collections;
import java.util.List;

public class NoneLiteralNode extends LiteralExpressionNode {

    public NoneLiteralNode(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return Collections.emptyList();
    }

}