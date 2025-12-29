package python.models.compouned_statement;

import python.models.funcdef.FunctionDef;

public class DecoratorStatement {
    
    public Decorator decorator;
    public FunctionDef function;

    public DecoratorStatement(Decorator decorator, FunctionDef function) {
        this.decorator = decorator;
        this.function = function;
    }
}
