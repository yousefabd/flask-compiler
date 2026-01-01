package python.models.expr_statement;

import python.models.ASTNode;

public abstract class Condition extends ASTNode {
    protected Condition(String name, int line) {
        super("Condition." + name, line);
    }
}
