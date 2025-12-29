package python.models.atom_statement;

public abstract class Number extends Atom {
    public boolean negative;
    public Number(boolean negative)
    {
        this.negative = negative;
    }
}
