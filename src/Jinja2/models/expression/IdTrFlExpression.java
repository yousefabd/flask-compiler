package Jinja2.models.expression;

import java.util.ArrayList;
import java.util.List;

import Jinja2.models.Root;
import Jinja2.models.Primary.ID;

public class IdTrFlExpression extends Expression {
    ID id;
    Trailer tr;
    Filter fl;

    public IdTrFlExpression(ID id, Trailer tr, Filter fl, int line) {
        super("IdTrFlExpression", line);
        this.id = id;
        this.tr = tr;
        this.fl = fl;
    }

    public boolean haveTrailer(){
        return tr != null;
    }

    public boolean haveFilter(){
        return fl != null;
    }

    @Override
    public String toString()
    {
        if(haveTrailer())
        {
            return "Trailer: ";
        }
        else if (haveFilter())
        {
            return "Filter: ";
        }
        return "";
    }

    @Override
    public List<Root> getChildren() {
        List<Root> children = new ArrayList<>();
        if (id != null) children.add(id);
        if (tr != null) children.add(tr);
        if (fl != null) children.add(fl);
        return children;
    }

}
