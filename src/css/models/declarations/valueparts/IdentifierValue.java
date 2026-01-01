package css.models.declarations.valueparts;

import css.models.Node;

public class IdentifierValue extends ValuePart {
    private final String name;

    public IdentifierValue(String name,int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
