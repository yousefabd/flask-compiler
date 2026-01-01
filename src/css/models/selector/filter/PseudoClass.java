package css.models.selector.filter;

import css.models.selector.filter.pseudoargs.PseudoArgument;

// class:
public class PseudoClass extends SelectorFilter{
    private final String name;
    private final PseudoArgument argument;
    public PseudoClass(String name, PseudoArgument argument,int line) {
        this.name = name;
        this.argument = argument;
        this.line = line;
    }
    public String getName() {
        return name;
    }
    public PseudoArgument getArgument() {
        return argument;
    }
}
