package python.models.atom_statement;

import java.util.ArrayList;

import python.models.expr_statement.Expression;

public class List extends Atom {

    public ArrayList<Expression> content;
    
    public List(ArrayList<Expression> content){
        this.content = content;
    }
}
