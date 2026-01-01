package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;

public class ContinueStatement extends SmallStatement{
    public ContinueStatement(int line) {
        super("ContinueStatement", line);
    }
    protected ContinueStatement(String name, int line) {
        super("ContinueStatement." + name, line);
    }
    public String toString() {
        return "";
    }

    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>();
    }
}
