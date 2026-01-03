package jinja2.models.statement;


import jinja2.models.root.Tag;

public abstract class Statement extends Tag {
	protected Statement(String nodeName, int lineNumber) { 
		super(nodeName, lineNumber); 
	}
}
