package python.models.expr_statement;

import java.util.ArrayList;

import python.models.enums.Operation;
import python.models.small_statement.SmallStatement;

public class ExpressionStatement extends SmallStatement {

    public ArrayList<Condition> conditions;
    public ArrayList<Condition> assigns;
    public Operation haveEquals;

    public ExpressionStatement(ArrayList<Condition> conditions, ArrayList<Condition> assigns, Operation haveEquals) {
        this.conditions = conditions;
        this.assigns = assigns;
        this.haveEquals = haveEquals;
    }

    public ExpressionStatement(ArrayList<Condition> conditions) {
        this.conditions = conditions;
        this.assigns = null;
        this.haveEquals = Operation.NONE;
    }
}
