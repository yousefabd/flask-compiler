package jinja2.models.expression;

import jinja2.models.Root;

import java.util.ArrayList;

public class Argument extends Root  {

    public Expression arg;
    public Expression assign;

    public Argument(Expression arg, Expression assign, int line) {
        super("Argument", line);
        this.arg = arg;
        this.assign = assign;
    }

    public boolean isAssigned() {
        return assign != null;
    }

    protected Argument(String name, int line) {
        super("Argument." + name, line);
    }

    @Override
    public String toString() {
        if(isAssigned()) return "Assigned: ";
        else return "";
    }

    @Override
    public ArrayList<Root> getChildren() {
        ArrayList<Root> res = new ArrayList<>();
        res.add(arg);
        if(isAssigned()) res.add(assign);
        return res;
    }
}
