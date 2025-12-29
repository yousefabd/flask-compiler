package python.models.funcdef;

import python.models.atom_statement.ID;
import python.models.expr_statement.Condition;

public class Parameter {
    
    public ID id;
    public Condition type;
    public Condition defaultValue;

    public Parameter(ID id, Condition type, Condition defaultValue) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
    }
}
