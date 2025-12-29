package python.models.compouned_statement;

import python.models.expr_statement.Condition;

public class WhileStatement {
    public Condition condition;
    public Body body;
    public Body last;

    public WhileStatement(Condition condition, Body body, Body last) {
        this.condition = condition;
        this.body = body;
        this.last = last;
    }
}
