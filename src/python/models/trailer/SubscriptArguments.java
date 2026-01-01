package python.models.trailer;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Condition;

public class SubscriptArguments extends Arguments
{
    public ArrayList<Condition> conditions;
    
    public SubscriptArguments(ArrayList<Condition> conditions, int line){
        super("SubscriptArguments", line);
        this.conditions = conditions;
    }

    protected SubscriptArguments(String name, int line) {
        super("SubscriptArguments." + name, line);
    }

    public String toString() {
        return "SubscriptArguments: ";
    }

    public ArrayList<ASTNode> getChildren() {
        if(conditions == null) return new ArrayList<>();
        return new ArrayList<>(conditions);
    }
}
