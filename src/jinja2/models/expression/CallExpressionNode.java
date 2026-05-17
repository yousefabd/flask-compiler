package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.ArrayList;
import java.util.List;

public class CallExpressionNode extends ExpressionNode {

    private final ExpressionNode   callee;
    private final List<ArgumentNode> arguments;

    public CallExpressionNode(
            ExpressionNode callee,
            List<ArgumentNode> arguments,
            int lineNumber) {
        super(lineNumber);
        this.callee    = callee;
        this.arguments = arguments;
    }

    public ExpressionNode    getCallee()    { return callee; }
    public List<ArgumentNode> getArguments() { return arguments; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        children.add(callee);
        children.addAll(arguments);
        return children;
    }
}