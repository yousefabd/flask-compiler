package Jinja2.models.statement;

import Jinja2.models.Primary.ID;
import Jinja2.models.Root;

import java.util.ArrayList;
import java.util.List;

public class SetBlockStatement extends Statement {
    public List<ID> targets;
    public Body body;


    public SetBlockStatement(List<ID> targetVariables, Body body, int lineNumber) {
        super("SetBlockStatement", lineNumber);
        this.targets = targetVariables;
        this.body = body;
    }
    protected SetBlockStatement(int line){
        super("SetBlockStatement",line);
    }

    @Override
    public String toString()
    {
        if(body != null) return "Targets|Body: ";
        return "Targets: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if(targets != null ) children.addAll(targets);
        if (body != null) children.add(body);
        return children;
    }
}
