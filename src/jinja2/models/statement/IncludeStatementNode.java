package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.expression.ExpressionNode;

import java.util.List;
import java.util.Objects;

public class IncludeStatementNode extends StatementNode {

    private final ExpressionNode templateExpression;

    public IncludeStatementNode(
            ExpressionNode templateExpression,
            int lineNumber
    ) {
        super(lineNumber);

        this.templateExpression =
                Objects.requireNonNull(templateExpression);
    }

    public ExpressionNode getTemplateExpression() {
        return templateExpression;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return List.of(templateExpression);
    }

    @Override
    public String describe() {
        return getNodeName()
                + " (line "
                + getLineNumber()
                + ")";
    }
}