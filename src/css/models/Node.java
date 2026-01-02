package css.models;

import java.util.List;

public abstract class Node {
    protected int line;
    public int getLine() {
        return line;
    }
    public abstract List<Node> getChildren();
    public String getNodeName(){
        return getClass().getSimpleName();
    }

}
