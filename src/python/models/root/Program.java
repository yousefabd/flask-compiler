package python.models.root;

import java.util.ArrayList;

import python.models.ASTNode;


public class Program extends ASTNode {

    public ArrayList<Statement> statements;
    
    public Program(ArrayList<Statement> statements, int line) {
        super("Program", line);
        this.statements = statements;
    }

    protected Program(String name, int line)
    {
        super("Program" + "." + name, line);
    }

    public void addStatement(Statement st)
    {
        this.statements.add(st);
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        if(this.statements == null) return new ArrayList<>();
        return new ArrayList<>(this.statements);
    }

    @Override
    public String toString() {
        if(statements == null || statements.size() == 0) return "";
        return "Statements: ";
    }
}
