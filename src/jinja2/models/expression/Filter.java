package jinja2.models.expression;

import java.util.ArrayList;
import java.util.List;

import jinja2.models.Root;
import jinja2.models.Primary.ID;

public class Filter extends Root{
    ID id;
    ArrayList<Argument> arguments;

    public Filter(ID id, ArrayList<Argument> arguments, int lineNumber) {
        super("Fliter", lineNumber);
        this.id = id;
        this.arguments = arguments;
    }

    public boolean haveArguments()
    {
        return arguments != null && arguments.size() > 0;
    }

    @Override
    public String toString()
    {
        if(haveArguments()) return "Id|Arguments: ";
        return "Id: ";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (id != null) children.add(id);
        if (arguments != null) children.addAll(arguments);
        return children;
    }
}
