package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;

import java.util.ArrayList;
import java.util.List;

public class SetStatementNode extends StatementNode {

    private final List<IdentifierNode> targets;
    private final ExpressionNode value;
    private final List<ContentNode> body;

    // {% set x, y = expression %}
    public static SetStatementNode inline(
            List<IdentifierNode> targets,
            ExpressionNode value,
            int lineNumber
    ) {
        return new SetStatementNode(
                targets,
                value,
                null,
                lineNumber
        );
    }

    // {% set x, y %} ... {% endset %}
    public static SetStatementNode block(
            List<IdentifierNode> targets,
            List<ContentNode> body,
            int lineNumber
    ) {
        return new SetStatementNode(
                targets,
                null,
                body,
                lineNumber
        );
    }

    private SetStatementNode(
            List<IdentifierNode> targets,
            ExpressionNode value,
            List<ContentNode> body,
            int lineNumber
    ) {
        super(lineNumber);

        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException(
                    "A set statement must contain at least one target"
            );
        }

        this.targets = List.copyOf(targets);
        this.value = value;
        this.body = body;
    }

    public boolean isBlock() {
        return body != null;
    }

    public List<IdentifierNode> getTargets() {
        return targets;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public List<ContentNode> getBody() {
        return body;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {

        List<TemplateNode> children = new ArrayList<>(targets);

        if (value != null) {
            children.add(value);
        }

        if (body != null) {
            children.addAll(body);
        }

        return children;
    }

    @Override
    public String describe() {
        String targetNames = String.join(
                ", ",
                targets.stream()
                        .map(IdentifierNode::getName)
                        .toList()
        );

        String kind = isBlock() ? "block" : "inline";

        return getNodeName()
                + " "
                + targetNames
                + " ("
                + kind
                + ") (line "
                + getLineNumber()
                + ")";
    }
}