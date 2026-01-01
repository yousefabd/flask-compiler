package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;

public class BreakStatement extends SmallStatement{
    protected BreakStatement(String name, int line) {
        super("BreakStatement." + name, line);
    }
    public BreakStatement(int line) {
        super("BreakStatement", line);
    }
    public String toString() {
        return "";
    }

    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>();
    }
}
