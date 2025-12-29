package python.models.compouned_statement;

import java.util.ArrayList;

import python.models.expr_statement.Condition;

public class IfStatement {
    public ArrayList<Condition> conditions;
    public ArrayList<Body> bodies;
    public Body last;

    public IfStatement(ArrayList<Condition> conditions, ArrayList<Body> bodies, Body last) {
        this.conditions = conditions;
        this.bodies = bodies;
        this.last = last;
    }
}
