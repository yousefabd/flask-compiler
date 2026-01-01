package python.models.funcdef;

import java.util.ArrayList;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.expr_statement.Condition;

public class Parameter extends ASTNode {
    
    public ID id;
    public Condition type;
    public Condition defaultValue;

    public Parameter(ID id, Condition type, Condition defaultValue, int line) {
        super("Parameter", line);
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    protected Parameter(String name, int line) {
        super("Parameter." + name, line);
    }

    public boolean hasType() {
        return this.type != null;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public String toString()
    {
        if(type != null && defaultValue != null)
            return "Id|Type|DefaultValue: ";
        if(type != null)
            return "Id|Type: ";
        if(defaultValue != null)
            return "Id|DefaultValue: ";
        return "Id: ";
    }

    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> res = new ArrayList<>();
        if(id != null) res.add(id);
        if(type != null) res.add(type);
        if(defaultValue != null) res.add(defaultValue);
        return res;
    }
}
