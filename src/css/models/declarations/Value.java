package css.models.declarations;

import css.models.Node;
import css.models.declarations.valueparts.ValuePart;

import java.util.List;

public class Value extends Node {
    private final List<ValuePart> parts;

    public Value(List<ValuePart> parts, int line) {
        this.parts = parts;
        this.line = line;
    }
    public List<ValuePart> getValueParts(){
        return parts;
    }
}
