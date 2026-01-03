package jinja2.models.statement;

import java.util.ArrayList;
import java.util.List;

import jinja2.models.Root;
import jinja2.models.Primary.ID;
import jinja2.models.expression.Expression;

public class Parameter extends Root
{
    public ID id;
    public Expression assign;

    public Parameter(ID id, Expression assign, int lineNumber) {
        super("Parameter", lineNumber);
        this.id = id;
        this.assign = assign;
    }

    public boolean haveAssign() {
        return assign != null;
    }

    @Override
    public String toString()
    {
        if(haveAssign())
            return "Id|Assign: ";
        else return "Id: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (id != null) children.add(id);
        if (assign != null) children.add(assign);
        return children;
    }
}
