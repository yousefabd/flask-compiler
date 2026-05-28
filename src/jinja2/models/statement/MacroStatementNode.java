package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;

import java.util.ArrayList;
import java.util.List;

public class MacroStatementNode extends BodyStatementNode {

    private final String            macroName;
    private final List<ParameterNode> parameters;
    public MacroStatementNode(String macroName, List<ParameterNode> parameters,
                              List<ContentNode> body, int lineNumber) {
        super(body, lineNumber);
        this.macroName  = macroName;
        this.parameters = parameters;
    }

    public String getMacroName()                   { return macroName; }
    public List<ParameterNode> getParameters()     { return parameters; }

    @Override
    public List<? extends TemplateNode> getChildren() {
        List<TemplateNode> children = new ArrayList<>();
        children.addAll(parameters);
        children.addAll(super.getBody());
        return children;
    }

    @Override
    public String describe() {
        return getNodeName() + " " + macroName
                + "(" + parameters.size() + " params) (line " + getLineNumber() + ")";
    }
}