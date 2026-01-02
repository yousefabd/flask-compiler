package css.models.selector;

import css.models.Node;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        if (combinator != null) children.add(combinator);
        children.add(selector);
        return children;
    }
}
