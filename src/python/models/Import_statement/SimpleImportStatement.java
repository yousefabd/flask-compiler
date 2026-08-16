package python.models.Import_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;

public class SimpleImportStatement extends ImportStatement {

    public ArrayList<ID> dottedName;
    public ID alias;

    public SimpleImportStatement(ArrayList<ID> dottedName, int line) {
        this(dottedName, null, line);
    }

    public SimpleImportStatement(ArrayList<ID> dottedName, ID alias, int line) {
        super("SimpleImportStatement", line);
        this.dottedName = dottedName;
        this.alias = alias;
    }

    public SimpleImportStatement(int line) {
        super("SimpleImportStatement", line);
        this.dottedName = new ArrayList<>();
    }

    protected SimpleImportStatement(String name, int line) {
        super("SimpleImportStatement." + name, line);
    }

    public void addDotName(ID id) {
        this.dottedName.add(id);
    }

    /** The actual name introduced by this import. */
    public ID getBoundName() {
        if (alias != null) return alias;
        return dottedName == null || dottedName.isEmpty() ? null : dottedName.getFirst();
    }

    public String toString()
    {
        return "path: ";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(this.dottedName != null) res.addAll(this.dottedName);
        if(this.alias != null) res.add(this.alias);
        return res;
    }
}
