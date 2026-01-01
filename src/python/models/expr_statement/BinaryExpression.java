package python.models.expr_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.enums.Operation;

public class BinaryExpression extends Expression {
    public Expression left;
    public Expression right;
    public Operation operation;
    public BinaryExpression(Expression left, Operation operation, Expression right, int line){
        super("BinaryExpression", line);
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    protected BinaryExpression(String name, int line) {
        super("BinaryExpression." + name, line);
    }

    @Override
    public String toString() {
        return "Operation: " + operation.name();
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.add(left);
        res.add(right);
        return res;
    }
}
