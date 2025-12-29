package python.models.atom_statement;

import java.util.ArrayList;

import python.models.expr_statement.Expression;

public class Set extends Atom {
    public ArrayList<Expression> content;
    public Set(ArrayList<Expression> content){
        this.content = content;
    }
}
