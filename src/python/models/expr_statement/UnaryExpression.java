package python.models.expr_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.enums.Operation;

public class UnaryExpression extends Expression 
{
    public Operation operation;
    public Expression expression;

    public UnaryExpression(Operation operation, Expression expression, int line)
    {
        super("UnaryExpression", line);
        this.operation = operation;
        this.expression = expression;
    }
    protected UnaryExpression(String name, int line) {
        super("UnaryExpression." + name, line);
    }
    public String toString() 
    {
        return "Operation: " + operation.name();
    }
    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        if(expression != null) res.add(expression);
        return res;
    }
}
