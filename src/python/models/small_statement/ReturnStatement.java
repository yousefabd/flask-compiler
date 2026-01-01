package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Condition;

public class ReturnStatement extends SmallStatement {

    public ArrayList<Condition> conditions;
    public ReturnStatement(ArrayList<Condition> conditions, int line) {
        super("ReturnStatement", line);
        this.conditions = conditions;
    }

    protected ReturnStatement(String name, int line) {
        super("ReturnStatement." + name, line);
    }

    public String toString() {
        if(conditions != null && conditions.size() > 0)
            return "Returns: ";
        return "";
    }

    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>(conditions);
    }


}
