package css.models;

import css.models.declarations.Declaration;
import css.models.selector.SelectorGroup;

import java.util.ArrayList;
import java.util.List;

public class Ruleset extends Node{
    private final SelectorGroup selectorGroup;
    private final List<Declaration> declarations;
    public Ruleset(SelectorGroup selectorGroup, List<Declaration> declarations, int line) {
        this.selectorGroup = selectorGroup;
        this.declarations = declarations;
        this.line = line;
    }
    public SelectorGroup getSelectorGroup() {
        return selectorGroup;
    }
    public List<Declaration> getDeclarations() {
        return declarations;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.add(selectorGroup);
        children.addAll(declarations);
        return children;
    }
}
