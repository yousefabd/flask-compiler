package jinja2.models.expression;

import jinja2.models.Root;

public abstract class Expression extends Root
{
    protected Expression(String name, int line) 
    {
        super("Expression." + name, line);
    }
}
