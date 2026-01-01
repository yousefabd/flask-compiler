package python.models.compound_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Condition;
import python.models.root.CompoundStatement;

public class WhileStatement extends CompoundStatement {
    public Condition condition;
    public Body body;
    public Body last;

    public WhileStatement(Condition condition, Body body, Body last, int line) 
    {
        super("WhileStatement", line);
        this.condition = condition;
        this.body = body;
        this.last = last;
    }

    protected WhileStatement(String name, int line) {
        super("WhileStatement." + name, line);
    }

    @Override
    public String toString() {
        if(last != null)
            return "Condition|Body|Last: ";
        return "Condition|Body: ";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.add(condition);
        res.add(body);
        if(last != null) res.add(last);
        return res;
    }
}
