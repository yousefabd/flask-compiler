package css.models.declarations.valueparts;

import css.models.Node;

import java.util.ArrayList;
import java.util.List;

public final class FunctionValue extends ValuePart {
    private final IdentifierValue name;
    private final List<ValuePart> arguments;

    public FunctionValue(IdentifierValue name, List<ValuePart> arguments,int line) {
        this.name = name;
        this.arguments = List.copyOf(arguments);
        this.line = line;
    }

    public IdentifierValue getName() {
        return name;
    }

    public List<ValuePart> getArguments() {
        return arguments;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>(1 + arguments.size());
        children.add(name);
        children.addAll(arguments);
        return children;
    }
}