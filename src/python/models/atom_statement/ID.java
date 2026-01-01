package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;

public class ID extends Atom {
    public String name;
    public ID(String name, int line )
    {
        super("ID", line);
        this.name = name;
    }

    public String toString() {
        return "ID: " + name;
    }

    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>();
    }
}
