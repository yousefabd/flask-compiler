package Jinja2.models.expression;

import Jinja2.models.Root;
import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression 
{

	public Expression left;
	public Expression right;
	public Operation operator;

	public BinaryExpression(Expression left, Operation operator, Expression right, int line) {
		super("BinaryOperationExpression", line);
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	public String toString()
	{
		return "Operator: " + operator.name();
	}

	@Override
	public List<Root> getChildren() {
		List<Root> children = new ArrayList<>();
		if (left != null) children.add(left);
		if (right != null) children.add(right);
		return children;
	}
}
