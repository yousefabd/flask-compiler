package jinja2.models.expression;

import java.util.ArrayList;
import java.util.List;

import jinja2.models.Root;
import jinja2.models.Primary.ID;

public class Trailer extends Root {

    protected Trailer(String name, int line) {
        super("Trailer." + name, line);
    }

    ArrayList<Argument> arguments;
    ID attribute;
    Expression index;

    public int getType()
    {
        if(arguments != null) return 1;
        else if(attribute != null) return 2;
        else if(index != null) return 3;
        return 44444;
    }

    public Trailer(ArrayList<Argument> arguments, Expression index, ID attribute, int line) {
        super("Trailer", line);
        this.arguments = arguments;
        this.index = index;
        this.attribute = attribute;
    }

    @Override 
    public String toString()
    {
        if(getType() == 1) return "Arguments: ";
        else if(getType() == 2) return "Index: ";
        else if(getType() == 3) return ".ID: ";
        return "";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (arguments != null) children.addAll(arguments);
        if (index != null) children.add(index);
        if (attribute != null) children.add(attribute);
        return children;
    }

}
