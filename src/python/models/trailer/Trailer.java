package python.models.trailer;

import python.models.atom_statement.ID;

public class Trailer {

    public boolean dot;
    public ID id;
    public Arguments arguments;
    
    public Trailer(boolean dot, ID id, Arguments arguments) {
        this.dot = dot;
        this.id = id;
        this.arguments = arguments;
    }
}
