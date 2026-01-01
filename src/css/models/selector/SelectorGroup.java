package css.models.selector;

import css.models.Node;

import java.util.List;

public class SelectorGroup extends Node {
    private final List<Selector> selectors;
    public SelectorGroup(List<Selector> selectors, int line) {
        this.selectors = selectors;
        this.line = line;
    }
    public List<Selector> getSelectors() {
        return selectors;
    }
}
