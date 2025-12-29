package python.models.trailer;

import java.util.ArrayList;

import python.models.expr_statement.Condition;

public class SubscriptArguments extends Arguments{
    public ArrayList<Condition> conditions;
    public SubscriptArguments(ArrayList<Condition> conditions){
        this.conditions = conditions;
    }
}
