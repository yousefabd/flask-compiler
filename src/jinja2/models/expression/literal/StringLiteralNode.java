package jinja2.models.expression.literal;

import jinja2.models.TemplateNode;

import java.util.Collections;
import java.util.List;

public class StringLiteralNode extends LiteralExpressionNode {

    private final String value;

    public StringLiteralNode(
            String value,
            int lineNumber) {

        super(lineNumber);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return Collections.emptyList();
    }

}