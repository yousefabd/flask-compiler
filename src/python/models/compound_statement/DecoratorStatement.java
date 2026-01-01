package python.models.compound_statement;

import python.models.ASTNode;
import python.models.funcdef.FunctionDef;
import python.models.root.CompoundStatement;

import java.util.ArrayList;

public class DecoratorStatement extends CompoundStatement {
    
    public ArrayList<Decorator> decorators;
    public FunctionDef function;

    public DecoratorStatement(ArrayList<Decorator> decorators, FunctionDef function, int line) {
        super("DecoratorStatement", line);
        this.decorators = decorators;
        this.function = function;
    }

    protected DecoratorStatement(String name, int line) {
        super("DecoratorStatement." + name, line);
    }

    @Override
    public String toString() {
        if(decorators != null && decorators.size() > 0)
            return "Decorators|FunctionDef: ";
        return "FunctionDef: ";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if(decorators != null) children.addAll(decorators);
        children.add(function);
        return children;
    }
}
