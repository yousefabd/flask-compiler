package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.enums.Operation;
import python.models.expr_statement.Expression;

public class AugAssignStatement extends SmallStatement {
    
    public ID id;
    public Operation operation;
    public Expression expression;

    public AugAssignStatement(ID id, Operation operation, Expression expression, int line) {
        super("AugAssignStatement", line);
        this.id = id;
        this.operation = operation;
        this.expression = expression;
    }

    protected AugAssignStatement(String name, int line) {
        super("AugAssignStatement." + name, line);
    }

    @Override
    public String toString() {
        return "Operation: " + operation.name();
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        res.add(id);
        res.add(expression);
        return res;
    }

}
