package jinja2.models.expression;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import jinja2.models.Primary.Primary;
import jinja2.models.Root;
import jinja2.models.Primary.ID;

public class IdTrFlExpression extends Expression {
    Primary head;
    List<Trailer> trailers;
    List<Filter> filters;

    public IdTrFlExpression(Primary head, List<Trailer> tr, List<Filter> fl, int line) {
        super("IdTrFlExpression", line);
        this.head = head;
        this.trailers = tr;
        this.filters = fl;
    }

    public boolean haveTrailer() {
        return (trailers != null && trailers.size() > 0);
    }

    public boolean haveFilter() {
        return (filters != null && filters.size() > 0);
    }

    public IdTrFlExpression(Primary head, int line) {
        this(head, new ArrayList<>(), new ArrayList<>(), line);
    }

    public void addTrailer(Trailer tr) {
        this.trailers.add(tr);
    }

    public void addFilter(Filter fl) {
        this.filters.add(fl);
    }

    @Override
    public String toString()
    {
        if(haveTrailer())
            return "Trailers: ";
        else if (haveFilter())
            return "Filters: ";
        return "";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (head != null) children.add(head);
        if (trailers != null) children.addAll(trailers);
        if (filters != null) children.addAll(filters);
        return children;
    }

}
