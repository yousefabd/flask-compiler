package python.models.small_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;

// AST node for `global x, y, z` (new: see global_stmt in PythonParser.g4).
// Carries the declared names; semantic meaning (redirecting assignments to the
// module scope) is applied later by SymbolTable/SymbolTableBuilder.
public class GlobalStatement extends SmallStatement {
    public ArrayList<ID> names;

    public GlobalStatement(ArrayList<ID> names, int line) {
        super("GlobalStatement", line);
        this.names = names;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("global ");
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(names.get(i).name);
        }
        return sb.toString();
    }

    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>(names);
    }
}
