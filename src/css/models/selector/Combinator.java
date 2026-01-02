package css.models.selector;

import css.models.Node;

import java.util.List;

public class Combinator extends Node {

    public static enum Type{
        DESCENDANT,  // space
        CHILD,       // >
        ADJACENT,    // +
        SIBLING      // ~
    }
    private final Type type;
    public Combinator(Type type,int line){
        this.type = type;
        this.line = line;
    }
    public Type getType(){
        return type;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
