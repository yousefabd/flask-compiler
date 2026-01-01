package python.models.compound_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Condition;
import python.models.root.CompoundStatement;

public class IfStatement extends CompoundStatement {
    public ArrayList<Condition> conditions;
    public ArrayList<Body> bodies;
    public Body last;

    public IfStatement(int line)
    {
        super("IfStatement", line);
        conditions = new ArrayList<>();
        bodies = new ArrayList<>();
    }

    public IfStatement(ArrayList<Condition> conditions, ArrayList<Body> bodies, Body last, int line)
    {
        super("IfStatement", line);
        this.conditions = conditions;
        this.bodies = bodies;
        this.last = last;
    }

    protected IfStatement(String name, int line) {
        super("IfStatement." + name, line);
    }

    public String toString() {
        if(last != null)
            return "(Condition|Body)*|last: ";
        return "(Condition|Body)*: ";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        for(int i = 0; i < conditions.size(); i++) {
            res.add(conditions.get(i));
            res.add(bodies.get(i));
        }
        if(last != null) res.add(last);
        return res;
    }

    public void addCondBody(Condition cond, Body bd)
    {
        this.conditions.add(cond);
        this.bodies.add(bd);
    }

    public void addElse(Body bd)
    {
        this.last = bd;
    }
}
