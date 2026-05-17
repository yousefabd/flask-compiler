package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class UnaryExpressionNode extends ExpressionNode
{

	private final Operation operation;

	private final ExpressionNode expression;

	public UnaryExpressionNode(
			Operation operation,
			ExpressionNode expression,
			int lineNumber) {

		super(lineNumber);

		this.operation = operation;
		this.expression = expression;
	}

	public Operation getOperation() {
		return operation;
	}

	public ExpressionNode getExpression() {
		return expression;
	}

	@Override
	public List<? extends TemplateNode> getChildren() {
		return List.of(expression);
	}

	@Override
	public String toString() {
		return operation + " " + expression;
	}
}
