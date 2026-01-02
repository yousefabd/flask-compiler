package css.models.selector.filter;

import css.models.Node;
import css.models.enums.AttributeOperator;

import java.util.List;

public class AttributeFilter extends SelectorFilter{
    private final String attributeName;
    private final AttributeOperator operator;
    private final String value; // null for BOOLEAN

    public AttributeFilter(
            String attributeName,
            AttributeOperator operator,
            String value,
            int line
    ) {
        this.attributeName = attributeName;
        this.operator = operator;
        this.value = value;
        this.line = line;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public AttributeOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
