package Jinja2.visitor;

import Jinja2.models.Root;

public class ASTPrinter {

    public void print(Root node) {
        print(node, 0);
    }

    private void print(Root node, int indent) {
        if (node == null) return;

        String prefix = " ".repeat(indent * 2);
        System.out.println(prefix + node.getNodeName() + " (line " + node.getLineNumber() + ")");

        for (Root child : node.getChildren()) {
            print(child, indent + 1);
        }
    }
}

