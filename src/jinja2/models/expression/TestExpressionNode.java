package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.ArrayList;
import java.util.List;

public final class TestExpressionNode extends ExpressionNode {

    private final ExpressionNode value;
    private final String testName;
    private final List<ArgumentNode> arguments;
    private final boolean negated;

    public TestExpressionNode(
            ExpressionNode value,
            String testName,
            List<ArgumentNode> arguments,
            boolean negated,
            int lineNumber
    ) {
        super(lineNumber);

        this.value = value;
        this.testName = testName;
        this.arguments = List.copyOf(arguments);
        this.negated = negated;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public String getTestName() {
        return testName;
    }

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();

        children.add(value);
        children.addAll(arguments);

        return children;
    }
}