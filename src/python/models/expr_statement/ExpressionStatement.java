package python.models.expr_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.enums.Operation;
import python.models.small_statement.SmallStatement;

public class ExpressionStatement extends SmallStatement {

    public ArrayList<Condition> conditions;
    public ArrayList<Condition> assigns;
    public Operation haveEquals;

    public ExpressionStatement(ArrayList<Condition> conditions, ArrayList<Condition> assigns, Operation haveEquals, int line) {
        super("ExpressionStatement", line);
        this.conditions = conditions;
        this.assigns = assigns;
        this.haveEquals = haveEquals;
    }

    public ExpressionStatement(ArrayList<Condition> conditions, int line) {
        super("ExpressionStatement", line);
        this.conditions = conditions;
        this.assigns = null;
        this.haveEquals = Operation.NONE;
    }

    public ExpressionStatement(int line) {
        super("ExpressionStatement", line);
        this.conditions = new ArrayList<>();
        this.assigns = new ArrayList<>();
        this.haveEquals = Operation.NONE;
    }

    protected ExpressionStatement(String name, int line) {
        super("ExpressionStatement." + name, line);
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void addAssign(Condition assign) {
        this.assigns.add(assign);
    }

    public void setHaveEquals(Operation haveEquals) {
        this.haveEquals = haveEquals;
    }

    public String toString() {
        if(haveEquals == Operation.EQUALS) return "(Item=Item)*: ";
        return "Item*: ";
    }

    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        if(haveEquals == Operation.EQUALS) 
        {
            for(int i = 0; i < conditions.size(); i++) {
                res.add(conditions.get(i));
                res.add(assigns.get(i));
            }
        }
        else res.addAll(conditions);
        return res;
    }
}
