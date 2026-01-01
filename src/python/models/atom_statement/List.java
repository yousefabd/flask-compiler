package python.models.atom_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.expr_statement.Expression;

public class List extends Atom {

    public ArrayList<Expression> content;
    
    public List(ArrayList<Expression> content, int line)
    {
        super("List", line);
        this.content = content;
    }

    public List(int line) 
    {
        super("List", line);
        this.content = new ArrayList<>();
    }

    public void addItem(Expression ex)
    {
        this.content.add(ex);
    }

    public ArrayList<ASTNode> getChildren()
    {
        return new ArrayList<>(content);
    }

    public String toString ()
    {
        return "Items: ";
    }
}
