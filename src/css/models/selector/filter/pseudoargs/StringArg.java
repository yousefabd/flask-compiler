package css.models.selector.filter.pseudoargs;

import css.models.Node;

import java.util.List;

public class StringArg extends PseudoArgument{
    private final String value;

    public StringArg(String value,int line) {
        this.value = value;
        this.line = line;
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
