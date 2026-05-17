package jinja2.printer;

import jinja2.models.TemplateNode;

public class ASTPrinter {

    public void printTree(TemplateNode root) {
        printTree(root, 0);
    }

    private void printTree(TemplateNode node, int indent) {
        if (node == null) return;

        printNode(node, indent);

        for (TemplateNode child : node.getChildren()) {
            printTree(child, indent + 1);
        }
    }



    public void printNode(TemplateNode node, int indent) {
        String pad = "|    ".repeat(indent);
        System.out.println(pad + node.toString());
    }
}

