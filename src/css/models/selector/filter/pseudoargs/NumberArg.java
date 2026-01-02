package css.models.selector.filter.pseudoargs;

import css.models.Node;

import java.util.List;

public class NumberArg extends PseudoArgument{
    private final double value;

    public NumberArg(double value,int line) {
        this.value = value;
        this.line = line;
    }

    public double getValue() {
        return value;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
