package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class BinaryExpressionNode extends ExpressionNode {

	private final ExpressionNode left;

	private final Operation operation;

	private final ExpressionNode right;

	public BinaryExpressionNode(
			ExpressionNode left,
			Operation operation,
			ExpressionNode right,
			int lineNumber) {

		super(lineNumber);

		this.left = left;
		this.operation = operation;
		this.right = right;
	}

	public ExpressionNode getLeft() {
		return left;
	}

	public Operation getOperation() {
		return operation;
	}

	public ExpressionNode getRight() {
		return right;
	}

	@Override
	public List<? extends TemplateNode> getChildren() {
		return List.of(left, right);
	}

}