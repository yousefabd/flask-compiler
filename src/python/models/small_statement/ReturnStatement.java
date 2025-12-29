package python.models.small_statement;

import java.util.ArrayList;

import python.models.expr_statement.Condition;

public class ReturnStatement extends SmallStatement {

    public ArrayList<Condition> conditions;
    public ReturnStatement(ArrayList<Condition> conditions){
        this.conditions = conditions;
    }
}
