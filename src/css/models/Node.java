package css.models;

import java.util.List;

public abstract class Node {

    private final String nodeName =
            getClass().getSimpleName();

    protected int line;

    public int getLine() {
        return line;
    }

    public String getNodeName() {
        return nodeName;
    }

    public abstract List<Node> getChildren();
}