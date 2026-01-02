package css.models.selector.filter;

import css.models.Node;

import java.util.List;

public class PseudoElement extends SelectorFilter{
    private final String name;

    public PseudoElement(String name,int line) {
        this.name = name;
        this.line = line;
    }
    public String getName() {
        return name;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
