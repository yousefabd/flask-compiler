package python.models.expr_statement;

public abstract class Comparison extends Condition{
    protected Comparison(String name, int line) {
        super("Comparison." + name, line);
    }
}
