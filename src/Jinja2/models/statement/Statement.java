package Jinja2.models.statement;


import Jinja2.models.root.Tag;

public abstract class Statement extends Tag {
	protected Statement(String nodeName, int lineNumber) { 
		super(nodeName, lineNumber); 
	}
}
