package css.models.declarations.valueparts;

import css.models.Node;

public class DimensionValue extends ValuePart {
    private final NumberValue number;
    private final String unit;

    public DimensionValue(NumberValue number, String unit,int line) {
        this.number = number;
        this.unit = unit;
        this.line = line;
    }

    public NumberValue getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    public double asDouble() {
        return number.asDouble();
    }

    @Override
    public String toString() {
        return number + unit;
    }
}
