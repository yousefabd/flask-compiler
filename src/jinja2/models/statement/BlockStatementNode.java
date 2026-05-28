package jinja2.models.statement;

import jinja2.models.TemplateNode;
import jinja2.models.content.ContentNode;

import java.util.List;

public class BlockStatementNode extends BodyStatementNode {

    private final String blockName;

    public BlockStatementNode(String blockName, List<ContentNode> body,
                              int lineNumber) {
        super(body, lineNumber);
        this.blockName = blockName;
    }

    public String getBlockName() { return blockName; }
    @Override
    public List<? extends TemplateNode> getChildren() { return super.getBody(); }

    @Override
    public String describe() {
        return getNodeName() + " \"" + blockName + "\" (line " + getLineNumber() + ")";
    }
}