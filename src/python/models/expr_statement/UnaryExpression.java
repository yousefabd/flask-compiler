package python.models.expr_statement;

import python.models.enums.Operation;

public class UnaryExpression extends Expression {
    public Operation operation;
    public Expression expression;
    public UnaryExpression(Operation operation, Expression expression){
        this.operation = operation;
        this.expression = expression;
    }
}
