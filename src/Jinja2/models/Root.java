package Jinja2.models;

import java.util.List;

public abstract class Root {
    public  String nodeName;
    public  int lineNumber;

    protected Root(String nodeName, int lineNumber) {
        this.nodeName = nodeName;
        this.lineNumber = lineNumber;
    }
    public String getNodeName() {
        return nodeName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    public abstract  List<? extends Root> getChildren();
}


