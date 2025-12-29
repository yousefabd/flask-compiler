package python.models.expr_statement;

import python.models.enums.Operation;

public class RelationalComparison extends Comparison{
    public Expression left;
    public Expression right;
    public Operation operation;
    public RelationalComparison(Expression left, Operation operation, Expression right){
        this.left = left;
        this.operation = operation;
        this.right = right;
    }
}
