package python.models.atom_statement;

public class Integer extends Number {
    private Long value;

    public Integer(Long value, boolean negative) {
        super(negative);
        this.value = value;
    }

    public Long getValue()
    {
        return super.negative? -value: value;
    }
}
