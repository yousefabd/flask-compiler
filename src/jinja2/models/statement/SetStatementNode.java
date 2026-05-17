package jinja2.models.statement;


import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;

import java.util.ArrayList;
import java.util.List;

public class SetStatementNode extends StatementNode {

    private final String variableName;
    private final ExpressionNode value;      // null for block-set
    private final List<ContentNode> body;    // null for inline-set

    // inline:  {% set x = expr %}
    public static SetStatementNode inline(
            String variableName, ExpressionNode value, int lineNumber) {
        return new SetStatementNode(variableName, value, null, lineNumber);
    }

    // block:  {% set x %}...{% endset %}
    public static SetStatementNode block(
            String variableName, List<ContentNode> body, int lineNumber) {
        return new SetStatementNode(variableName, null, body, lineNumber);
    }

    private SetStatementNode(
            String variableName,
            ExpressionNode value,
            List<ContentNode> body,
            int lineNumber) {
        super(lineNumber);
        this.variableName = variableName;
        this.value        = value;
        this.body         = body;
    }

    public boolean isBlock() { return body != null; }
    public String  getVariableName()  { return variableName; }
    public ExpressionNode getValue()  { return value; }
    public List<ContentNode> getBody(){ return body; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        if (value != null) children.add(value);
        if (body  != null) children.addAll(body);
        return children;
    }

    @Override
    public String describe() {
        String kind = isBlock() ? "block" : "inline";
        return getNodeName() + " " + variableName + " (" + kind + ") (line " + getLineNumber() + ")";
    }
}