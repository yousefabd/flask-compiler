package python.models.expr_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.enums.Operation;

public class RelationalComparison extends Comparison {
    public Expression left;
    public Expression right;
    public Operation operation;
    public RelationalComparison(Expression left, Operation operation, Expression right, int line)
    {
        super("RelationalComparison", line);
        this.left = left;
        this.operation = operation;
        this.right = right;
    }
    protected RelationalComparison(String name, int line) {
        super("RelationalComparison." + name, line);
    }

    public String toString() 
    {
        return "Operation: " + operation.name();
    }

    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(left != null) res.add(left);
        if(right != null) res.add(right);
        return res;
    }
}
