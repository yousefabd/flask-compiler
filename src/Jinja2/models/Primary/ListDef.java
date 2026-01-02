package Jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import Jinja2.models.Root;
import Jinja2.models.expression.Expression;


public class ListDef extends Primary{
    ArrayList<Expression> content;
    public ListDef(int lineNumber)
    {
        super("ListDef", lineNumber);
        content = new ArrayList<>();
    }

    public void addContent(Expression expression)
    {
        content.add(expression);
    }

    public String toString()
    {
        return "Content: ";
    }

    public List<Root> getChildren()
    {
        ArrayList<Root> res = new ArrayList<>();
        res.addAll(content);
        return res;
    }
}
