package css.models.declarations.valueparts;

import css.models.Node;

public class PercentValue extends ValuePart {
    private final NumberValue number;

    public PercentValue(NumberValue number,int line) {
        this.number = number;
        this.line = line;
    }

    public NumberValue getNumber() {
        return number;
    }

    public double asFraction() {
        return number.asDouble() / 100.0;
    }

    @Override
    public String toString() {
        return number + "%";
    }
}
