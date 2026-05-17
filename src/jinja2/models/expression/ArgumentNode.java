package jinja2.models.expression;

import jinja2.models.TemplateNode;

import java.util.List;

public class ArgumentNode extends ExpressionNode {

    private final String keyword;   // null for positional args
    private final ExpressionNode value;

    public ArgumentNode(String keyword, ExpressionNode value, int lineNumber) {
        super(lineNumber);
        this.keyword = keyword;
        this.value   = value;
    }

    public boolean isKeyword()  { return keyword != null; }
    public String  getKeyword() { return keyword; }
    public ExpressionNode getValue() { return value; }

    @Override
    public List<? extends TemplateNode> getChildren() { return List.of(value); }
}