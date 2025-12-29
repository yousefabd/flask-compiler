package python.models.atom_statement;

public class Float extends Number {
    private Double value;

    public Float(Double value, boolean negative) {
        super(negative);
        this.value = value;
    }

    public Double getValue()
    {
        return super.negative? -value: value;
    }
}
