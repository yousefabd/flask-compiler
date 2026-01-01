package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Expression;

public class ParenAtom extends Atom 
{
    public Expression inner;
    public ParenAtom(Expression inner, int line)
    {
        super("ParenAtom", line);
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "InnerExpression: ";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        res.add(inner);
        return res;
    }
}
