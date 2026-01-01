package python.models.expr_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.enums.Operation;

public class CompoundCondition extends Condition {

    public Operation operation;
    public Condition first;
    public Condition second;

    public CompoundCondition(Operation operation, Condition first, Condition second, int line) {
        super("CompoundCondition", line);
        this.operation = operation;
        this.first = first;
        this.second = second;
    }

    public CompoundCondition(Condition first, int line) {
        super("CompoundCondition", line);
        this.operation = Operation.NOT;
        this.first = first;
        this.second = null;
    }

    protected CompoundCondition(String name, int line) {
        super("CompoundCondition." + name, line);
    }

    public String toString() 
    {
        return "Operation: " + operation.name();
    }

    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(first != null) res.add(first);
        if(second != null) res.add(second);
        return res;
    }
}
