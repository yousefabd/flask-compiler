package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Expression;

public class Dictionary extends Atom {
    public ArrayList<Expression> keys;
    public ArrayList<Expression> values;

    public Dictionary(ArrayList<Expression> keys, ArrayList<Expression> values, int line)
    {
        super("Dictionary", line);
        this.keys = keys;
        this.values = values;
    }

    public Dictionary(int line) {
        super("Dictionary", line);
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    protected Dictionary(String name, int line) {
        super("Dictionary." + name, line);
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public void addItem(Expression key, Expression value)
    {
        this.keys.add(key);
        this.values.add(value);
    }

    @Override
    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(keys == null || values == null) return res;
        for(int i = 0; i < keys.size(); i++) {
            res.add(keys.get(i));
            res.add(values.get(i));
        }
        return res;
    }

    public String toString() 
    {
        return "Items: (key|value)* ";    
    }
}
