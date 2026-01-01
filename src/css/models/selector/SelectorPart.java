package css.models.selector;

import css.models.Node;
import css.models.enums.Combinator;

public class SelectorPart extends Node {
    private final Combinator combinator; // null for first part
    private final SimpleSelector selector;

    public SelectorPart(Combinator combinator, SimpleSelector selector,int line) {
        this.combinator = combinator;
        this.selector = selector;
        this.line = line;
    }
    public Combinator getCombinator() {
        return combinator;
    }
    public SimpleSelector getSelector() {
        return selector;
    }
}
