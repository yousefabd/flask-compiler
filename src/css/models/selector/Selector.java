package css.models.selector;

import css.models.Node;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Node {
    private final List<SelectorPart> parts;

    public Selector(List<SelectorPart> parts, int line) {
        this.parts = parts;
        this.line = line;
    }
    public List<SelectorPart> getParts() {
        return parts;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(parts);
    }
}
