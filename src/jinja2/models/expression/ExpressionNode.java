package jinja2.models.expression;
import jinja2.models.TemplateNode;

public abstract class ExpressionNode extends TemplateNode {

    protected ExpressionNode(int lineNumber) {
        super(lineNumber);
    }
}