package css.models.selector;

import css.models.Node;

import java.util.List;

public final class TypeSelector extends ElementSelector {
    public final String tagName;

    public TypeSelector(String tagName,int line)
    {
        this.tagName = tagName;
        this.line = line;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
