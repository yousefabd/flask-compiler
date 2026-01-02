package css.models.selector.filter;

import css.models.Node;

import java.util.List;

//#id
public class IdFilter extends SelectorFilter{
    private final String id;

    public IdFilter(String id,int line) {
        this.id = id;
        this.line = line;
    }

    public String getId() {
        return id;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
