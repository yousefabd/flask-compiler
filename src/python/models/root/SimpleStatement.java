package python.models.root;

import java.util.ArrayList;

import python.models.small_statement.SmallStatement;

public class SimpleStatement extends Statement {

    public ArrayList<SmallStatement> smallStatementList;

    public SimpleStatement(ArrayList<SmallStatement> smallStatementList) {
        this.smallStatementList = smallStatementList;
    }
}
