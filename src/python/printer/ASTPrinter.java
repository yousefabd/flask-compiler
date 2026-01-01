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
}
