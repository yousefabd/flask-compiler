package python.printer;

import python.models.ASTNode;

public class ASTPrinter {

    public void printTree(ASTNode root) {
        printTree(root, 0);
    }

    private void printTree(ASTNode node, int indent) {
        if (node == null) return;

        printNode(node, indent);

        for (ASTNode child : node.getChildren()) {
            printTree(child, indent + 1);
        }
    }

    public void printNode(ASTNode node, int indent) {
        String pad = "|    ".repeat(indent);
        System.out.println(pad + node.describe() + " "  + node.toString());
    }

    // added: same tree, captured as text instead of printed directly — used to embed
    // the AST inside the generated compiler report file without touching stdout output.
    public String treeToString(ASTNode root) {
        StringBuilder sb = new StringBuilder();
        treeToString(root, 0, sb);
        return sb.toString();
    }

    private void treeToString(ASTNode node, int indent, StringBuilder sb) {
        if (node == null) return;
        String pad = "|    ".repeat(indent);
        sb.append(pad).append(node.describe()).append(' ').append(node.toString()).append('\n');
        for (ASTNode child : node.getChildren())
            treeToString(child, indent + 1, sb);
    }
}
