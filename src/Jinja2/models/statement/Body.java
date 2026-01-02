package Jinja2.models.statement;

import java.util.ArrayList;

import Jinja2.models.Root;
import Jinja2.models.root.Tag;

public class Body extends Root{
    public ArrayList<Tag> tags = new ArrayList<>();

    public Body(int line)
    {
        super("Body", line);
        tags = new ArrayList<>();
    }
    
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public String toString() {
        if(tags != null)
            return "tags: ";
        return "";
    }

    public ArrayList<Root> getChildren() {
        return new ArrayList<>(this.tags);
    }
}
