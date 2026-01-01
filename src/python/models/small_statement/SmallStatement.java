package python.models.small_statement;

import python.models.ASTNode;

public abstract class SmallStatement extends ASTNode {
    protected SmallStatement(String name, int line) {
        super("SmallStatement." + name, line);
    }
}
