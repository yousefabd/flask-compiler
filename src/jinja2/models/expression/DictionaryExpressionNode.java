package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.ArrayList;
import java.util.List;

public class DictionaryExpressionNode extends ExpressionNode {

    // grammar: STRING COLON expr  — keys are always string literals
    private final List<ExpressionNode> keys;
    private final List<ExpressionNode> values;

    public DictionaryExpressionNode(
            List<ExpressionNode> keys,
            List<ExpressionNode> values,
            int lineNumber
    ) {
        super(lineNumber);

        if (keys.size() != values.size()) {
            throw new IllegalArgumentException(
                    "Dictionary keys and values must have equal sizes"
            );
        }

        this.keys = List.copyOf(keys);
        this.values = List.copyOf(values);
    }

    public List<ExpressionNode> getKeys()   { return keys; }
    public List<ExpressionNode> getValues() { return values; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<ExpressionNode> all = new ArrayList<>();
        // interleave so the printer shows key→value pairs in order
        for (int i = 0; i < keys.size(); i++) {
            all.add(keys.get(i));
            all.add(values.get(i));
        }
        return all;
    }
}