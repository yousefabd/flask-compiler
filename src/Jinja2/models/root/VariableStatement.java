package Jinja2.models.root;
import Jinja2.models.Root;
import Jinja2.models.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class VariableStatement extends Tag
{
    public Expression expression;

    public VariableStatement(Expression expression, int lineNumber) 
    {
        super("VariableStatement", lineNumber);
        this.expression = expression;
    }

    protected VariableStatement(String name, int line)
    {
        super("VariableStatement." + name, line);
    }

    public Expression getExpression() 
    {
        return expression;
    }

    public String toString()
    {
        return "";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (expression != null) children.add(expression);
        return children;
    }
}
