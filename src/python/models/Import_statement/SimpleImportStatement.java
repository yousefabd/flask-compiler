package python.models.Import_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;

public class SimpleImportStatement extends ImportStatement {

    public ArrayList<ID> dottedName;

    public SimpleImportStatement(ArrayList<ID> dottedName, int line) {
        super("SimpleImportStatement", line);
        this.dottedName = dottedName;
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

    public String toString()
    {
        return "path: ";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(this.dottedName != null) res.addAll(this.dottedName);
        return res;
    }
}
