package python.models.trailer;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Condition;

// cSpell: disable
public class Argument extends ASTNode
{

    public Condition arg;
    public Condition assign;

    public Argument(Condition arg, Condition assign, int line) {
        super("Argument", line);
        this.arg = arg;
        this.assign = assign;
    }

    public boolean isAssigned() {
        return assign != null;
    }

    protected Argument(String name, int line) {
        super("Argument." + name, line);
    }

    @Override
    public String toString() {
        if(isAssigned()) return "Assigned: ";
        else return "";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.add(arg);
        if(isAssigned()) res.add(assign);
        return res;
    }

}
