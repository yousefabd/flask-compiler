package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;

public class None extends Atom {
    public None(int line) {
        super("None", line);
    }

    public String toString()
    {
        return "";
    }

    public ArrayList<ASTNode> getChildren()
    {
        return new ArrayList<>();
    }
}
