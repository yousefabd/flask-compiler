package python.models.atom_statement;

import java.util.ArrayList;

import python.models.expr_statement.Expression;

public class Dictionary extends Atom {
    public ArrayList<Expression> keys;
    public ArrayList<Expression> values;
    public Dictionary(ArrayList<Expression> keys, ArrayList<Expression> values){
        this.keys = keys;
        this.values = values;
    }
}
