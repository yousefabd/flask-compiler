package python.models.compound_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.expr_statement.Expression;
import python.models.root.CompoundStatement;

public class ForStatement extends CompoundStatement {

    public ArrayList<ID> iterators;
    public Expression iterable;
    public Body body;
    public Body last;

    public ForStatement(ArrayList<ID> iterators, Expression expression, Body body, Body last, int line)
    {
        super("ForStatement", line);
        this.iterators = iterators;
        this.iterable = expression;
        this.body = body;
        this.last = last;
    }

    protected ForStatement(String name, int line) {
        super("ForStatement." + name, line);
    }

    @Override
    public String toString() {
        if(last != null)
            return "Iters|Iterable|Body|Last: ";
        return "Iters|Iterable|Body: ";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.addAll(iterators);
        res.add(iterable);
        res.add(body);
        if(last != null) res.add(last);
        return res;
    }

}
