package python.models;

import java.util.List;

public abstract class ASTNode {
    protected String nodeName;
    protected int line;

    protected ASTNode(String nodeName, int line) {
        this.nodeName = nodeName;
        this.line = line;
    }

    public String getNodeName() { return nodeName; }
    public int getLine() { return line; }


    public String describe() {
        return nodeName + " (line " + line + ")";
    }

    public abstract List<ASTNode> getChildren();

    public abstract String toString();

    public String getSimpleName() {
        int idx = getNodeName().lastIndexOf('.');
        return (idx >= 0) ? getNodeName().substring(idx + 1) : getNodeName();
    }

}
