package python.models.expr_statement;

public abstract class Expression extends Comparison {
    protected Expression(String name, int line) {
        super(name, line);
    }

}
