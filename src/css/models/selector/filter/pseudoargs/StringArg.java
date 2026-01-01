package css.models.selector.filter.pseudoargs;

public class StringArg extends PseudoArgument{
    private final String value;

    public StringArg(String value,int line) {
        this.value = value;
        this.line = line;
    }

    public String getValue() {
        return value;
    }
}
