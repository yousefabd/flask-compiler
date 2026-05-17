package jinja2.models.statement;


import jinja2.models.content.ContentNode;

public abstract class StatementNode extends ContentNode {

	protected StatementNode(int lineNumber) {
		super(lineNumber);
	}
}