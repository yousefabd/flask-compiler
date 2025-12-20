package html.models;

import java.util.Objects;
import java.util.Optional;

public class AttributeNode implements Node {
    private final String name;
    private final String value;
    private final int line;


    public AttributeNode(String name, String value,int line) {
        this.name = Objects.requireNonNull(name);
        this.value = value;
        this.line = line;
    }

    public String name() { return name; }
    public Optional<String> value() { return Optional.ofNullable(value); }
    public int getLine() { return line; }

    @Override
    public String toString() {
        return value == null ? name : name + " =" + value;
    }
}
