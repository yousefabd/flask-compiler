package jinja2.models.statement;

import jinja2.models.TemplateNode;

import java.util.List;

public class IfStatementNode extends StatementNode {

    // branches[0] = if,  branches[1..n-1] = elif,  last may be else (condition==null)
    private final List<IfBranchNode> branches;

    public IfStatementNode(List<IfBranchNode> branches, int lineNumber) {
        super(lineNumber);
        this.branches = branches;
    }

    public List<IfBranchNode> getBranches() { return branches; }

    @Override
    public List<? extends TemplateNode> getChildren() { return branches; }

    @Override
    public String describe() {
        long elifCount = branches.stream()
                .filter(b -> !b.isElseBranch())
                .count() - 1; // subtract the if itself
        return getNodeName() + " (" + elifCount + " elif) (line " + getLineNumber() + ")";
    }
}