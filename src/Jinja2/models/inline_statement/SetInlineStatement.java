package Jinja2.models.inline_statement;

import Jinja2.models.Primary.ID;
import Jinja2.models.expression.Expression;
import Jinja2.models.Root;

import java.util.ArrayList;
import java.util.List;

public class SetInlineStatement extends InlineStatement {
        
    public List<ID> targets;
    public Expression value;


    public SetInlineStatement(List<ID> targets, Expression value, int lineNumber) 
    {
        super("SetInlineStatement", lineNumber);
        this.targets = targets;
        this.value = value;
    }

    protected SetInlineStatement(int line) {
        super("SetInlineStatement",line);
    }

    @Override
    public String toString()
    {
        return "Targets|Value: ";
    }

    @Override
    public List<Root> getChildren() 
    {
        List<Root> children = new ArrayList<>();
        if (targets != null) children.addAll(targets);
        if (value != null) children.add(value);
        return children;
    }
}

