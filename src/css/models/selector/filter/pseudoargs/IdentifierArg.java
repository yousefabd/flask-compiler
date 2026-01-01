package css.models.selector.filter.pseudoargs;

public class IdentifierArg extends PseudoArgument{
    private final String value;

    public IdentifierArg(String value,int line) {
        this.value = value;
        this.line = line;
    }

    public String getValue() {
        return value;
    }
}
