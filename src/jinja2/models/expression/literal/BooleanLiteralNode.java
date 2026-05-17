package jinja2.models.expression.literal;


import jinja2.models.TemplateNode;

import java.util.Collections;
import java.util.List;

public class BooleanLiteralNode extends LiteralExpressionNode {

    private final boolean value;

    public BooleanLiteralNode(
            boolean value,
            int lineNumber) {

        super(lineNumber);

        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}