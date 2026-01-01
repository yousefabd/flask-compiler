package python.models.Import_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.enums.Operation;

public class FromImportStatement extends ImportStatement {
    public ArrayList<ID> dottedName;
    public ArrayList<ID> targets;
    public Operation star;

    public FromImportStatement(ArrayList<ID> dottedName, ArrayList<ID> targets, Operation star, int line) {
        super("FromImportStatement", line);
        this.dottedName = dottedName;
        this.targets = targets;
        this.star = star;
    }

    protected FromImportStatement(String name, int line) {
        super("FromImportStatement." + name, line);
    }

    public boolean hasStar() {
        return star == Operation.STAR;
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
        return res;
    }
}
