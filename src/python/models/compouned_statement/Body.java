package python.models.compouned_statement;

import java.util.ArrayList;

import python.models.root.Statement;

public class Body {
    
    public ArrayList<Statement> statements;
    public Body(ArrayList<Statement> statements){
        this.statements = statements;
    }
}
