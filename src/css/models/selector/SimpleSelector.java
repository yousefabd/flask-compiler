package css.models.selector;

import css.models.Node;
import css.models.selector.filter.SelectorFilter;

import java.util.List;

public class SimpleSelector extends Node {
    private final ElementSelector elementSelector;
    private final List<SelectorFilter> filters;
    public SimpleSelector(ElementSelector elementSelector, List<SelectorFilter> filters, int line) {
        this.elementSelector = elementSelector;
        this.filters = filters;
        this.line = line;
    }
    public ElementSelector getElementSelector() {
        return elementSelector;
    }
    public List<SelectorFilter> getFilters() {
        return filters;
    }

}
