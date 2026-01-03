package jinja2.models.Primary;

import jinja2.models.Root;
import java.util.ArrayList;
import java.util.List;

public class ID extends Primary {
	public String identifierName;

	public ID(String identifierName, int lineNumber) {
		super("ID", lineNumber);
		this.identifierName = identifierName;
	}

	@Override
	public List<Root> getChildren() { return new ArrayList<>(); }

	@Override
	public String toString() {
		return identifierName;
	}
}
