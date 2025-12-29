package python.models.Import_statement;

import java.util.ArrayList;

import python.models.atom_statement.ID;

public class SimpleImportStatement {

    public ArrayList<ID> dottedName;

    public SimpleImportStatement(ArrayList<ID> dottedName) {
        this.dottedName = dottedName;
    }
}
