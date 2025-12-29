package python.models.atom_statement;

import java.util.List;

import python.models.expr_statement.Expression;
import python.models.trailer.Trailer;

public class IDTrailer extends Expression {
    public ID id;
    public List<Trailer> trailers;
    public IDTrailer(ID id, List<Trailer> trailers){
        this.id = id;
        this.trailers = trailers;
    }
}
