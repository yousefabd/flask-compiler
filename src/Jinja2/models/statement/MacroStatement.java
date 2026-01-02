package Jinja2.models.statement;

import Jinja2.models.Root;
import Jinja2.models.Primary.ID;

import java.util.ArrayList;
import java.util.List;

public class MacroStatement extends Statement {

    private ID id;
    private List<Parameter> parameters;
    private Body body;


    public MacroStatement(ID id, List<Parameter> parameters, Body body, int lineNumber) {
        super("MacroStatement", lineNumber);
        this.id = id;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String toString()
    {
        if(parameters != null && parameters.size() > 0) return "Id|Parameters|Body: ";
        return "Id|Body: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        children.add(id);
        if (parameters != null) {
            children.addAll(parameters);
        }
        if (body != null) children.add(body);
        return children;
    }
}

