package python.models.atom_statement;

import python.models.expr_statement.Expression;

public class ParenAtom extends Atom {
    public Expression inner;
    public ParenAtom(Expression inner){
        this.inner = inner;
    }
}
