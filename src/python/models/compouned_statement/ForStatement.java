package python.models.compouned_statement;

import java.util.ArrayList;

import python.models.atom_statement.ID;
import python.models.expr_statement.Expression;

public class ForStatement {

    public ArrayList<ID> iterators;
    public Expression expression;
    public Body body;
    public Body last;

    public ForStatement(ArrayList<ID> iterators, Expression expression, Body body, Body last) {
        this.iterators = iterators;
        this.expression = expression;
        this.body = body;
        this.last = last;
    }

}
