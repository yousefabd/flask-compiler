package python.models.Import_statement;

import java.util.ArrayList;
import java.util.List;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.enums.Operation;

public class FromImportStatement extends ImportStatement {
    public ArrayList<ID> dottedName;
    public ArrayList<ID> targets;
    public ArrayList<ID> aliases;
    public Operation star;

    public FromImportStatement(ArrayList<ID> dottedName, ArrayList<ID> targets, Operation star, int line) {
        this(dottedName, targets, null, star, line);
    }

    public FromImportStatement(ArrayList<ID> dottedName, ArrayList<ID> targets,
                               ArrayList<ID> aliases, Operation star, int line) {
        super("FromImportStatement", line);
        this.dottedName = dottedName;
        this.targets = targets;
        this.aliases = aliases;
        this.star = star;
    }

    protected FromImportStatement(String name, int line) {
        super("FromImportStatement." + name, line);
    }

    public boolean hasStar() {
        return star == Operation.STAR;
    }

    /** Names introduced by this import, preserving source order. */
    public List<ID> getBoundNames() {
        if (targets == null) return List.of();
        ArrayList<ID> result = new ArrayList<>(targets.size());
        for (int index = 0; index < targets.size(); index++) {
            ID alias = aliases != null && index < aliases.size() ? aliases.get(index) : null;
            result.add(alias != null ? alias : targets.get(index));
        }
        return List.copyOf(result);
    }

    public String toString() {
        if(hasStar()) return "path|*: ";
        return "path|targets: ";
    }

    public ArrayList<ASTNode> getChildren()
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(this.dottedName != null) res.addAll(dottedName);
        if(this.targets != null) res.addAll(targets);
        if(this.aliases != null) {
            for (ID alias : aliases) if (alias != null) res.add(alias);
        }
        return res;
    }
}
