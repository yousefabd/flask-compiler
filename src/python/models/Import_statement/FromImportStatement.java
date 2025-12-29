package python.models.Import_statement;

import java.util.ArrayList;

import python.models.atom_statement.ID;
import python.models.enums.Operation;

public class FromImportStatement {
    public ArrayList<ID> dottedName;
    public ArrayList<ID> targets;
    public Operation star;

    public FromImportStatement(ArrayList<ID> dottedName, ArrayList<ID> targets, Operation star) {
        this.dottedName = dottedName;
        this.targets = targets;
        this.star = star;
    }
}
