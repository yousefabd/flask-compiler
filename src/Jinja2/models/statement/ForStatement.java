package Jinja2.models.statement;

import Jinja2.models.Primary.ID;
import Jinja2.models.expression.Expression;
import Jinja2.models.Root;

import java.util.ArrayList;
import java.util.List;

public class ForStatement extends Statement 
{
    public List<ID> iterators;
    public Expression iterable;
    public Body body;

    protected ForStatement(String name, int line) {
        super("ForStatement." + name, line);
    }

    public ForStatement(List<ID> iterators, Expression iterable, Body body, int lineNumber) {
        super("ForStatement", lineNumber);
        this.iterators = iterators;
        this.iterable = iterable;
        this.body = body;
    }


    @Override 
    public String toString()
    {
        if(body != null) return "Iterators|Iterable|Body: ";
        return "Iterators|Iterable: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if(iterators != null) children.addAll(iterators);
        if (iterable != null) children.add(iterable);
        if (body != null) children.add(body);
        return children;
    }
}

