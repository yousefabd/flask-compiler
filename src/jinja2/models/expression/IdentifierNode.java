package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.Collections;
import java.util.List;

public class IdentifierNode extends ExpressionNode{
    private final String name;

    public IdentifierNode(
            String name,
            int lineNumber) {

        super(lineNumber);

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return Collections.emptyList();
    }

}
