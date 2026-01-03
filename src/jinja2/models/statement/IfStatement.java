package jinja2.models.statement;

import jinja2.models.Root;
import jinja2.models.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement 
{

    public List<Expression> conditions;
    public List<Body> bodies;

    public IfStatement(int lineNumber) {
        super("IfStatement", lineNumber);
        conditions = new ArrayList<>();
        bodies = new ArrayList<>();
    }

    public void addBranch(Expression condition, Body body) {
        conditions.add(condition);
        bodies.add(body);
    }

    public boolean haveElse()
    {
        return conditions.get(conditions.size() - 1) == null;
    }

    public String toString()
    {
        return "(Condition|body)*(body)?: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        for (int i = 0; i < bodies.size(); i++) 
        {
            Expression c = conditions.get(i);
            Body b = bodies.get(i);
            if (c != null)
                children.add(c);
            if (b != null)
                children.add(b);
        }
        return children;
    }
}
