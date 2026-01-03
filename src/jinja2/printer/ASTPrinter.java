package jinja2.printer;

import jinja2.models.Root;
import python.models.ASTNode;

public class ASTPrinter {

    public void printTree(Root root) {
        printTree(root, 0);
    }

    private void printTree(Root node, int indent) {
        if (node == null) return;

        printNode(node, indent);

        for (Root child : node.getChildren()) {
            printTree(child, indent + 1);
        }
    }



    public void printNode(Root node, int indent) {
        String pad = "|    ".repeat(indent);
        System.out.println(pad + node.describe() + " "  + node.toString());
    }
}

