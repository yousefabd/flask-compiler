package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.expression.ExpressionNode;

import java.util.List;

public class ParameterNode extends TemplateNode {

    private final String name;
    private final ExpressionNode defaultValue; // null if no default

    public ParameterNode(String name, ExpressionNode defaultValue, int lineNumber) {
        super(lineNumber);
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() { return name; }
    public boolean hasDefault() { return defaultValue != null; }
    public ExpressionNode getDefaultValue() { return defaultValue; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return defaultValue != null ? List.of(defaultValue) : List.of();
    }

    @Override
    public String describe() {
        return getNodeName() + " " + name + (hasDefault() ? "=..." : "") + " (line " + getLineNumber() + ")";
    }
}