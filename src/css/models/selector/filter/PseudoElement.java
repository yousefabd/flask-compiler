package css.models.selector.filter;

public class PseudoElement extends SelectorFilter{
    private final String name;

    public PseudoElement(String name,int line) {
        this.name = name;
        this.line = line;
    }
    public String getName() {
        return name;
    }
}
