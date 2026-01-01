package python.models.atom_statement;

import python.models.ASTNode;

import java.util.ArrayList;

public class IntegerAtom extends Atom {
    public int value;
    public IntegerAtom(int value, int line)
    {
        super("IntegerAtom", line);
        this.value = value;
    }

    public String toString()
    {
        return Integer.toString(value);
    }

    public ArrayList<ASTNode> getChildren() {return new ArrayList<>();}
}