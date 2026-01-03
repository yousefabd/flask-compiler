package jinja2.models.inline_statement;

import jinja2.models.root.Tag;

public abstract class InlineStatement extends Tag
{
    protected InlineStatement(String name, int line)
    {
        super("InlineStatement." + name, line);
    }
}
