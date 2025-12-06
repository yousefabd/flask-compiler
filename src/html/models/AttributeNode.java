package html.models;

import java.util.Objects;
import java.util.Optional;

public class AttributeNode {
    private final String name;
    private final String value;


    public AttributeNode(String name, String value) {
        this.name = Objects.requireNonNull(name);
        this.value = value;
    }

    public String name() { return name; }
    public Optional<String> value() { return Optional.ofNullable(value); }

    @Override
    public String toString() {
        return value == null ? name : name + "=\"" + value + "\"";
    }
}
