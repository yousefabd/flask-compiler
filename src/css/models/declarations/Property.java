package css.models.declarations;

import css.models.Node;

public class Property extends Node {
    private final String name;

    public Property(String name,int line) {
        this.name = name;
        this.line = line;
    }
    public String getName(){
        return name;
    }
}
