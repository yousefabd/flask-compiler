package python.models.atom_statement;

public class String extends Atom {
    private String value;

    public String(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

}
