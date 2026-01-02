package css.models.declarations.valueparts;

import css.models.Node;

import java.util.List;

public final class NumberValue extends ValuePart {

    private final String raw;

    public NumberValue(String raw,int line) {
        this.raw = raw;
        this.line = line;
    }

    public String getRaw() {
        return raw;
    }

    public double asDouble() {
        return Double.parseDouble(raw);
    }

    @Override
    public String toString() {
        return raw;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}