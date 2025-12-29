package python.models.compouned_statement;

import java.util.ArrayList;

import python.models.atom_statement.ID;
import python.models.trailer.Argument;

public class Decorator {

    public ArrayList<ID> dottedName;
    public ArrayList<Argument> arguments;
    
    public Decorator(ArrayList<ID> dottedName, ArrayList<Argument> arguments) {
        this.dottedName = dottedName;
        this.arguments = arguments;
    }
}
