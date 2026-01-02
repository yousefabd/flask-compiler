package python.models.root;

public abstract class CompoundStatement extends Statement {
    protected CompoundStatement(String name, int line)
    {
        super("CompoundStatement." + name, line);
    }
}
