package python.models.atom_statement;

import python.models.ASTNode;

import java.util.ArrayList;

public class FloatAtom extends Atom {
    public float value;
    public FloatAtom(float value, int line)
    {
        super("FloatAtom", line);
        this.value = value;
    }

    public String toString()
    {
        return Float.toString(value);
    }

    public ArrayList<ASTNode> getChildren() {return new ArrayList<>();}
}
