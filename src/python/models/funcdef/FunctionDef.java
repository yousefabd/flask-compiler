package python.models.funcdef;

import java.util.ArrayList;

import python.models.atom_statement.ID;
import python.models.compouned_statement.Body;
import python.models.expr_statement.Condition;

public class FunctionDef {
    public ID id;                  
    public ArrayList<Parameter> parameters;   
    public Condition returnType;          
    public Body body;

    public FunctionDef(ID id, ArrayList<Parameter> parameters, Condition returnType, Body body) {
        this.id = id;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }
}
