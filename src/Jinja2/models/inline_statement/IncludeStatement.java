package Jinja2.models.inline_statement;

import Jinja2.models.Root;
import Jinja2.models.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class IncludeStatement extends InlineStatement 
{
    public Expression template;


    public IncludeStatement(Expression templateExpression, int lineNumber) 
    {
        super("IncludeStatement", lineNumber);
        this.template = templateExpression;
    }

    @Override
    public String toString() {
        return "Template: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (template != null) children.add(template);
        return children;
    }
}
