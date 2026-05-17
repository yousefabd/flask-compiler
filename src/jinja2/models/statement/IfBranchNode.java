package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;

import java.util.ArrayList;
import java.util.List;

public class IfBranchNode extends TemplateNode {

    private final ExpressionNode condition; // null for the else branch
    private final List<ContentNode> body;

    public IfBranchNode(ExpressionNode condition, List<ContentNode> body, int lineNumber) {
        super(lineNumber);
        this.condition = condition;
        this.body      = body;
    }

    public boolean isElseBranch() { return condition == null; }
    public ExpressionNode getCondition() { return condition; }
    public List<ContentNode> getBody()   { return body; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        if (condition != null) children.add(condition);
        children.addAll(body);
        return children;
    }

    @Override
    public String describe() {
        String label = condition == null ? "else" : "if/elif";
        return getNodeName() + " (" + label + ") (line " + getLineNumber() + ")";
    }
}