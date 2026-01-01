package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;

public class PassStatement extends SmallStatement{
    protected PassStatement(String name, int line) {
        super("PassStatement." + name, line);
    }
    public PassStatement(int line) {
        super("PassStatement", line);
    }
    public String toString() {
        return "";
    }

    public ArrayList<ASTNode> getChildren()
    {
        return new ArrayList<>();
    }

}
