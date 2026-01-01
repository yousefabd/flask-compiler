package python.models.atom_statement;

import python.models.ASTNode;

import java.util.ArrayList;

public class StringAtom extends Atom {
    public String value;
    public StringAtom(String value, int line)
    {
        super("StringAtom", line);
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public ArrayList<ASTNode> getChildren() {return new ArrayList<>();}
}
