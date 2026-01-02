package Jinja2.models.root;

import Jinja2.models.Root;

import java.util.ArrayList;
import java.util.List;

public class Template extends Root {
    public List<Tag> tags;

    public Template(int lineNumber) {
        super("Template", lineNumber);
        tags = new ArrayList<>();
    }

    protected Template(String name, int line) {
        super("Template." + name, line);
        tags = new ArrayList<>();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override 
    public String toString() {
        if(tags == null || tags.size() == 0) return "";
        return "Tags: ";
    }

    @Override
    public List<Root> getChildren() {
        if (tags == null)
            return new ArrayList<>();
        return new ArrayList<>(this.tags);
    }
}


