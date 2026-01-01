package python.models.funcdef;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.compound_statement.Body;
import python.models.expr_statement.Condition;

public class FunctionDef extends ASTNode {
    public ID id;                  
    public ArrayList<Parameter> parameters;   
    public Condition returnType;          
    public Body body;

    public FunctionDef(ID id, ArrayList<Parameter> parameters, Condition returnType, Body body, int line) {
        super("FunctionDef", line);
        this.id = id;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }

    protected FunctionDef(String name, int line) {
        super("FunctionDef." + name, line);
    }

    public String toString() 
    {
        if((parameters != null && parameters.size() > 0) && returnType != null)
            return "Id|Parameters|ReturnType|Body: ";
        if((parameters != null && parameters.size() > 0))
            return "Id|Parameters|Body: ";
        if(returnType != null)
            return "Id|ReturnType|Body: ";
        return "Id|Body: ";
    }

    public ArrayList<ASTNode> getChildren()
    {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(id != null) res.add(id);
        if(parameters != null) res.addAll(parameters);
        if(returnType != null) res.add(returnType);
        if(body != null) res.add(body);
        return res;
    }
}
