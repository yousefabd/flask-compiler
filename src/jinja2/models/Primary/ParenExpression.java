package jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import jinja2.models.Root;
import jinja2.models.expression.Expression;

public class ParenExpression extends Primary{
    public Expression expression;
    public ParenExpression(Expression expression,int lineNumber){
        super("ParenExpression",lineNumber);
        this.expression=expression;
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (expression != null) children.add(expression);
        return children;
    }

    @Override
    public String toString()
    {
        return "InnerExpression: ";
    }
}
