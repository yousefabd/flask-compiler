package codegen;

import java.util.List;

/**
 * Serializes an AST to JSON for {@code compiler_output/}, without pulling in
 * a JSON library the rest of the project doesn't otherwise depend on. Both
 * AST hierarchies ({@code python.models.ASTNode} and
 * {@code jinja2.models.TemplateNode}) already expose {@code getSimpleName()}
 * (or {@code getNodeName()}), a line number, and {@code getChildren()} — this
 * writer is just that shape walked into {@code {"node":..., "line":..., "children":[...]}}.
 */
public final class AstJsonWriter {

    private AstJsonWriter() {}

    public static String toJson(python.models.ASTNode node) {
        StringBuilder sb = new StringBuilder();
        writePython(node, sb, 0);
        return sb.toString();
    }

    public static String toJson(jinja2.models.TemplateNode node) {
        StringBuilder sb = new StringBuilder();
        writeJinja(node, sb, 0);
        return sb.toString();
    }

    /** Prepends {@code levels * 2} spaces to every line after the first, for embedding as a map value. */
    public static String reindent(String json, int levels) {
        String indent = "  ".repeat(levels);
        String[] lines = json.split("\n", -1);
        StringBuilder sb = new StringBuilder(lines[0]);
        for (int i = 1; i < lines.length; i++)
            sb.append('\n').append(indent).append(lines[i]);
        return sb.toString();
    }

    private static void writePython(python.models.ASTNode node, StringBuilder sb, int indent) {
        if (node == null) { sb.append("null"); return; }
        pad(sb, indent).append("{\n");
        pad(sb, indent + 1).append("\"node\": \"").append(escape(node.getSimpleName())).append("\",\n");
        pad(sb, indent + 1).append("\"line\": ").append(node.getLine()).append(",\n");
        pad(sb, indent + 1).append("\"detail\": \"").append(escape(node.toString())).append("\",\n");
        List<python.models.ASTNode> children = node.getChildren();
        pad(sb, indent + 1).append("\"children\": [");
        if (children != null && !children.isEmpty()) {
            sb.append('\n');
            for (int i = 0; i < children.size(); i++) {
                writePython(children.get(i), sb, indent + 2);
                sb.append(i < children.size() - 1 ? ",\n" : "\n");
            }
            pad(sb, indent + 1);
        }
        sb.append("]\n");
        pad(sb, indent).append("}");
    }

    private static void writeJinja(jinja2.models.TemplateNode node, StringBuilder sb, int indent) {
        if (node == null) { sb.append("null"); return; }
        pad(sb, indent).append("{\n");
        pad(sb, indent + 1).append("\"node\": \"").append(escape(node.getNodeName())).append("\",\n");
        pad(sb, indent + 1).append("\"line\": ").append(node.getLineNumber()).append(",\n");
        pad(sb, indent + 1).append("\"detail\": \"").append(escape(node.describe())).append("\",\n");
        List<? extends jinja2.models.TemplateNode> children = node.getChildren();
        pad(sb, indent + 1).append("\"children\": [");
        if (children != null && !children.isEmpty()) {
            sb.append('\n');
            for (int i = 0; i < children.size(); i++) {
                writeJinja(children.get(i), sb, indent + 2);
                sb.append(i < children.size() - 1 ? ",\n" : "\n");
            }
            pad(sb, indent + 1);
        }
        sb.append("]\n");
        pad(sb, indent).append("}");
    }

    private static StringBuilder pad(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) sb.append("  ");
        return sb;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "");
    }
}
