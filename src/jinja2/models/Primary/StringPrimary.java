package jinja2.models.Primary;

import jinja2.models.Root;
import python.models.ASTNode;
import python.models.atom_statement.Atom;

import java.util.ArrayList;

public class StringPrimary extends Primary{

    public String value;
    public StringPrimary(String value, int line)
    {
        super("StringPrimary", line);
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public ArrayList<Root> getChildren() {return new ArrayList<>();}
}





