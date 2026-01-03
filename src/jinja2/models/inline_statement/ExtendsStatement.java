package jinja2.models.inline_statement;

import jinja2.models.Root;
import java.util.ArrayList;
import java.util.List;

public class ExtendsStatement extends InlineStatement {

    public String template;

    public ExtendsStatement(String template, int lineNumber) 
    {
        super("ExtendsStatement", lineNumber);
        this.template = template;
    }

    @Override
    public String toString() {
        return "Template: " + template;
    }

    @Override
    public List<Root> getChildren() { 
        return new ArrayList<>(); 
    }
}
