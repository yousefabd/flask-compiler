package python.models.expr_statement;

import python.models.enums.Operation;

public class CompoundCondition extends Condition{

    public Operation operation;
    public Condition first;
    public Condition second;

    public CompoundCondition(Operation operation, Condition first, Condition second) {
        this.operation = operation;
        this.first = first;
        this.second = second;
    }

    public CompoundCondition(Condition first) {
        this.operation = Operation.NOT;
        this.first = first;
        this.second = null;
    }

}
