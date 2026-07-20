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

    // added: same tree, captured as text instead of printed directly — used to embed
    // the AST inside the generated compiler report file without touching stdout output.
    public String treeToString(TemplateNode root) {
        StringBuilder sb = new StringBuilder();
        treeToString(root, 0, sb);
        return sb.toString();
    }

    private void treeToString(TemplateNode node, int indent, StringBuilder sb) {
        if (node == null) return;
        String pad = "|    ".repeat(indent);
        sb.append(pad).append(node.toString()).append('\n');
        for (TemplateNode child : node.getChildren())
            treeToString(child, indent + 1, sb);
    }
}

