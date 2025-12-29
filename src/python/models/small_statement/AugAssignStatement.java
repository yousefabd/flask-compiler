package python.models.small_statement;

import python.models.atom_statement.ID;
import python.models.enums.Operation;
import python.models.expr_statement.Expression;

public class AugAssignStatement extends SmallStatement {
    
    public ID id;
    public Operation operation;
    public Expression expression;

    public AugAssignStatement(ID id, Operation operation, Expression expression) {
        this.id = id;
        this.operation = operation;
        this.expression = expression;
    }
}
