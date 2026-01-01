package python.models.atom_statement;

import python.models.ASTNode;

import java.util.ArrayList;

public class BoolAtom extends Atom {
    public boolean value;
    public BoolAtom(boolean value, int line)
    {
        super("BoolAtom", line);
        this.value = value;
    }

    public String toString()
    {
        return Boolean.toString(value);
    }

    public ArrayList<ASTNode> getChildren() {return new ArrayList<>();}
}
