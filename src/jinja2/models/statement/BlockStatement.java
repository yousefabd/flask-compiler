package jinja2.models.statement;
//
//import jinja2.models.TemplateNode;
//import jinja2.models.Primary.ID;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockStatement extends Statement
//{
//    public ID id;
//    public Body body;
//
//    public BlockStatement(ID id, Body body, int lineNumber) {
//        super("BlockStatement", lineNumber);
//        this.id = id;
//        this.body = body;
//    }
//
//    @Override
//    public String toString() {
//        if(body != null) return "Id|Body: ";
//        return "Id: ";
//    }
//
//    @Override
//    public List<TemplateNode> getChildren() {
//        List<TemplateNode> children = new ArrayList<>();
//        if (id != null) children.add(id);
//        if (body != null) children.add(body);
//        return children;
//    }
//}
