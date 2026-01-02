package css.models.selector;

import css.models.Node;

import java.util.List;

public final class UniversalSelector extends ElementSelector {
    public UniversalSelector(int line){
        this.line = line;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
