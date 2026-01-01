package python.models.root;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.small_statement.SmallStatement;

public class SimpleStatement extends Statement {

    public ArrayList<SmallStatement> smallStatementList;

    public SimpleStatement(ArrayList<SmallStatement> smallStatementList, int line) {
        super("SimpleStatement", line);
        this.smallStatementList = smallStatementList;
    }

    protected SimpleStatement(String name, int line)
    {
        super("SimpleStatement." + name, line);
    }

    public SimpleStatement(int line)
    {
        super("SimpleStatement", line);
        this.smallStatementList= new ArrayList<>();
    }

    public void addSmallStatement(SmallStatement st)
    {
        smallStatementList.add(st);
    }

    public ArrayList<ASTNode> getChildren()
    {
        if(smallStatementList == null) return new ArrayList<>();
        return new ArrayList<>(this.smallStatementList);
    }

    public String toString()
    {
        if(smallStatementList == null || smallStatementList.size() == 0) return "";
        return "SmallStatements: ";
    }
}
