package python.models.trailer;

import python.models.expr_statement.Condition;

// cSpell: disable
public class Argument 
{

    public Condition arg;
    public Condition assign;

    public Argument(Condition arg, Condition assign) {
        this.arg = arg;
        this.assign = assign;
    }

}
