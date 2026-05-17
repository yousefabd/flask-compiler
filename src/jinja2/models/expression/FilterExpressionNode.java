package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.ArrayList;
import java.util.List;

public class FilterExpressionNode extends ExpressionNode {

    private final ExpressionNode   target;
    private final String           filterName;
    private final List<ArgumentNode> arguments;

    public FilterExpressionNode(
            ExpressionNode target,
            String filterName,
            List<ArgumentNode> arguments,
            int lineNumber) {
        super(lineNumber);
        this.target     = target;
        this.filterName = filterName;
        this.arguments  = arguments;
    }

    public ExpressionNode    getTarget()    { return target; }
    public String            getFilterName() { return filterName; }
    public List<ArgumentNode> getArguments() { return arguments; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        children.add(target);
        children.addAll(arguments);
        return children;
    }
}