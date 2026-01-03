package jinja2.models.root;

import jinja2.models.Root;

import java.util.ArrayList;
import java.util.List;

public class Text extends Tag {
    public String content;
    public Text(String content, int line)
    {
        super("Text", line);
        this.content = content;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public List<Root> getChildren() {
        return new ArrayList<>();
    }
}
