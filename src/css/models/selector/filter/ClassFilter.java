package css.models.selector.filter;

import css.models.Node;

import java.util.List;

// .className
public class ClassFilter extends SelectorFilter {
    private final String className;

    public ClassFilter(String className, int line) {
        this.className = className;
        this.line = line;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
