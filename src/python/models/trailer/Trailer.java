package python.models.trailer;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;

public class Trailer extends ASTNode {

    public boolean dot;
    public ID id;
    public Arguments arguments;
    
    public Trailer(boolean dot, ID id, Arguments arguments, int line) {
        super("Trailer", line);
        this.dot = dot;
        this.id = id;
        this.arguments = arguments;
    }

    protected Trailer(String name, int line) {
        super("Trailer." + name, line);
    }

    public boolean isDotIdTrailer() {
        return dot;
    }

    public boolean hasArguments() {
        return arguments != null;
    }

    public String toString() {
        if(isDotIdTrailer() && hasArguments()) return ".IdArguments: ";
        if(isDotIdTrailer()) return ".Id: ";
        if(hasArguments()) return "Arguments: ";
        return "";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(isDotIdTrailer()) res.add(id);
        if(hasArguments()) res.add(arguments);
        return res;
    }
}
