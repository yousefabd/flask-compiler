package css.models.declarations;

import css.models.Node;

import java.util.List;

public class Property extends Node {
    private final String name;

    public Property(String name,int line) {
        this.name = name;
        this.line = line;
    }
    public String getName(){
        return name;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
