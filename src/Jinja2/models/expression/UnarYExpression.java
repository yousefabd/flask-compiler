package Jinja2.models.expression;

import Jinja2.models.Root;
import java.util.ArrayList;
import java.util.List;

public class UnarYExpression extends Expression 
{

	public Operation operator;
	public Expression operand;

	public UnarYExpression(Operation operator, Expression operand, int lineNumber) {
		super("UnaryOperationExpression", lineNumber);
		this.operator = operator;
		this.operand = operand;
	}

	@Override
	public String toString() {
		return "Operator: " + operator.name();
	}

	@Override
	public List<Root> getChildren() {
		List<Root> children = new ArrayList<>();
		if (operand != null) children.add(operand);
		return children;
	}
}
