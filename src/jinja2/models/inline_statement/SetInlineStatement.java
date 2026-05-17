//package jinja2.models.inline_statement;
//
//import jinja2.models.Primary.ID;
//import jinja2.models.expression.ExpressionNode;
//import jinja2.models.TemplateNode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SetInlineStatement extends InlineStatement {
//
//    public List<ID> targets;
//    //public Expression value;
//
//
//    public SetInlineStatement(List<ID> targets, ExpressionNode value, int lineNumber)
//    {
//        super("SetInlineStatement", lineNumber);
//        this.targets = targets;
//    }
//
//    protected SetInlineStatement(int line) {
//        super("SetInlineStatement",line);
//    }
//
//    @Override
//    public String toString()
//    {
//        return "Targets|Value: ";
//    }
//
////    @Override
////    public List<TemplateNode> getChildren()
////    {
////        List<TemplateNode> children = new ArrayList<>();
////        if (targets != null) children.addAll(targets);
////        if (value != null) children.add(value);
////        return children;
////    }
//}
//
