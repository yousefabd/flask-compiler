package python.models.expr_statement;

import java.util.ArrayList;
import java.util.List;

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

    public boolean HaveEquals()
    {
        return this.haveEquals == Operation.EQUALS;
    }

    /**
     * Clear semantic-facing name for the historical {@link #HaveEquals()} API.
     * The parser and interpreter still use the original public fields.
     */
    public boolean isAssignment() {
        return haveEquals == Operation.EQUALS;
    }

    /** Assignment targets. Historically stored in {@link #conditions}. */
    public List<Condition> getTargets() {
        return isAssignment() && conditions != null
                ? List.copyOf(conditions)
                : List.of();
    }

    /** Assignment right-hand sides. Historically stored in {@link #assigns}. */
    public List<Condition> getValues() {
        return isAssignment() && assigns != null
                ? List.copyOf(assigns)
                : List.of();
    }

    /** Expressions in a non-assignment expression statement. */
    public List<Condition> getExpressions() {
        return !isAssignment() && conditions != null
                ? List.copyOf(conditions)
                : List.of();
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
        if (isAssignment()) {
            res.addAll(getTargets());
            res.addAll(getValues());
        } else {
            res.addAll(getExpressions());
        }
        return res;
    }
}
