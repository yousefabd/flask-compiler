package python.models.root;

import python.models.ASTNode;

public abstract class Statement extends ASTNode {
    protected Statement(String name, int line)
    {
        super("Statement." + name, line);
    }
}
