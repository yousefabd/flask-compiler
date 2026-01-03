package css.models.declarations;

import css.models.Node;

import java.util.List;

public class Declaration extends Node {
    private final Property property;
    private final Value value;
    private final boolean important;

    public Declaration(Property property, Value value, boolean important,int line) {
        this.property = property;
        this.value = value;
        this.important = important;
        this.line = line;
    }
    public Property getProperty() {
        return property;
    }
    public Value getValue() {
        return value;
    }
    public boolean isImportant() {
        return important;
    }

    @Override
    public List<Node> getChildren() {
        return List.of(property,value);
    }
    public boolean isVariable() {
        return property.getName().startsWith("--");
    }
}
