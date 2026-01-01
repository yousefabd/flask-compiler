package css.models.selector.filter.pseudoargs;

public class NumberArg extends PseudoArgument{
    private final double value;

    public NumberArg(double value,int line) {
        this.value = value;
        this.line = line;
    }

    public double getValue() {
        return value;
    }
}
