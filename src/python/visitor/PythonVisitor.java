// cSpell: disable
package python.visitor;

import java.util.ArrayList;

import python.models.root.Root;

public class PythonVisitor extends pyparserBaseVisitor<Object> {
    @Override 
    public Root visitProg(ProgContext ctx) {
        return new Root(new ArrayList<>());
    }
    
}
