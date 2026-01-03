package css;

import css.models.Node;

public final class ASTPrinter {

    public static void print(Node node) {
        print(node, 0);
    }

    private static void print(Node node, int depth) {
        System.out.println(
                "|   ".repeat(depth) +
                        node.getNodeName() +
                        " (line " + node.getLine() + ")"
        );

        for (Node child : node.getChildren()) {
            print(child, depth + 1);
        }
    }
}
