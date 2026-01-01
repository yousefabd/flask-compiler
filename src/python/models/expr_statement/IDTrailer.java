package python.models.expr_statement;

import java.util.ArrayList;
import java.util.List;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.trailer.Trailer;

public class IDTrailer extends Expression {
    public ID id;
    public List<Trailer> trailers;
    public IDTrailer(ID id, List<Trailer> trailers, int line){
        super("IDTrailer", line);
        this.id = id;
        this.trailers = trailers;
    }
    
    protected IDTrailer(String name, int line) {
        super("IDTrailer." + name, line);
    }

    public String toString()
    {
        return "Id|Trailer*";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>(); 
        res.add(id);
        if(trailers != null) res.addAll(trailers);
        return res;
    }
}
