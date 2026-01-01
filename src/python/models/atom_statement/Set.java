package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Expression;

public class Set extends Atom {
    public ArrayList<Expression> content;
    public Set(ArrayList<Expression> content, int line){
        super("Set", line);
        this.content = content;
    }

    public Set(int line) {
        super("Set", line);
        this.content = new ArrayList<>();
    }

    protected Set(String name, int line) {
        super("Set." + name, line);
        this.content = new ArrayList<>();
    }

    public void addItem(Expression ex)
    {
        this.content.add(ex);
    }

    public String toString ()
    {
        return "Items: ";
    }

    public ArrayList<ASTNode> getChildren() 
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.addAll(content);
        return res;
    }
}
