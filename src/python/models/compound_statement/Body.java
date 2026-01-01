package python.models.compound_statement;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.root.Statement;

public class Body extends ASTNode{

    public ArrayList<Statement> statements;

    public Body(int line)
    {
        super("Body", line);
        this.statements = new ArrayList<>();
    }

    public Body(ArrayList<Statement> statements, int line)
    {
        super("Body", line);
        this.statements = statements;
    }

    protected Body(String name, int line)
    {
        super("Body." + name, line);
    }

    public void addStatement(Statement st)
    {
        this.statements.add(st);
    }

    public String toString()
    {
        if(statements == null || statements.size() == 0) return "";
        return "Statements: ";
    }

    public ArrayList<ASTNode> getChildren()
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        res.addAll(statements);
        return res;
    }
}
