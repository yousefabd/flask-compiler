package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;

import java.util.List;

public class BlockStatementNode extends StatementNode {

    private final String blockName;
    private final List<ContentNode> body;

    public BlockStatementNode(String blockName, List<ContentNode> body, int lineNumber) {
        super(lineNumber);
        this.blockName = blockName;
        this.body      = body;
    }

    public String getBlockName() { return blockName; }
    public List<ContentNode> getBody()    { return body; }

    @Override
    public List<? extends TemplateNode> getChildren() { return body; }

    @Override
    public String describe() {
        return getNodeName() + " \"" + blockName + "\" (line " + getLineNumber() + ")";
    }
}