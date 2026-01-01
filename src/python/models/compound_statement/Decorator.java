package python.models.compound_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.trailer.Argument;

public class Decorator extends ASTNode{

    public ArrayList<ID> dottedName;
    public ArrayList<Argument> arguments;
    
    public Decorator(ArrayList<ID> dottedName, ArrayList<Argument> arguments, int line) {
        super("Decorator", line);
        this.dottedName = dottedName;
        this.arguments = arguments;
    }

    protected Decorator(String name, int line) {
        super("Decorator." + name, line);
    }

    public String toString()
    {
        if(arguments != null && arguments.size() > 0)
            return "DottedName|Arguments: ";
        return "DottedName: ";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        res.addAll(dottedName);
        res.addAll(arguments);
        return res;
    }
}
