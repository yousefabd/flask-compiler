package jinja2.models.root;

import jinja2.models.Root;

public abstract class Tag extends Root {
    protected Tag(String name, int lineNumber) 
    {
        super("Tag." + name,lineNumber);
    }
}
