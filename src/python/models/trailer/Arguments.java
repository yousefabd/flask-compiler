package python.models.trailer;

import python.models.ASTNode;

public abstract class Arguments extends ASTNode {
    protected Arguments(String name, int line) {
        super("Arguments." + name, line);
    }
}
