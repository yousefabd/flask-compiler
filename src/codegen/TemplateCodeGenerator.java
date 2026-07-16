package codegen;

import errors.CodeGenError;

import jinja2.models.TemplateNode;
import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeTextNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
import jinja2.models.content.html.HTMLNormalElementNode;
import jinja2.models.content.html.HTMLVoidElementNode;
import jinja2.models.expression.*;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Walks the Jinja2/HTML template AST and emits an HTML file that Flask's
 * real Jinja2 engine can render — HTML structure for the UI, and
 * {@code {{ ... }}} / {@code {% ... %}} constructs for the dynamic parts.
 *
 * <p>Original text nodes are preserved verbatim, so the generated template
 * keeps the source formatting wherever possible.</p>
 */
public class TemplateCodeGenerator {

    /** Void elements must not get a closing tag (per the HTML spec). */
    private static final List<String> VOID_TAGS = List.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr");

    private final String sourceFile;    // used only for error messages

    public TemplateCodeGenerator(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    // ─────────────────────────────────────────────────────────────
    // TEMPLATE
    // ─────────────────────────────────────────────────────────────

    public String generate(TemplateFile template) {
        StringBuilder out = new StringBuilder();
        for (ContentNode child : template.getContentChildren())
            out.append(content(child));
        return out.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT DISPATCH
    // ─────────────────────────────────────────────────────────────

    private String content(ContentNode node) {
        if (node instanceof HtmlTextNode text)
            return text.getText();
        if (node instanceof OutputNode out)
            return "{{ " + expr(out.getExpression()) + " }}";
        if (node instanceof HTMLNormalElementNode el)
            return normalElement(el);
        if (node instanceof HTMLVoidElementNode el)
            return voidElement(el);
        if (node instanceof ForStatementNode fs)
            return forStatement(fs);
        if (node instanceof IfStatementNode is)
            return ifStatement(is);
        if (node instanceof SetStatementNode ss)
            return setStatement(ss);
        if (node instanceof MacroStatementNode ms)
            return macroStatement(ms);
        if (node instanceof BlockStatementNode bs)
            return blockStatement(bs);
        if (node instanceof ExtendsStatementNode es)
            return "{% extends \"" + es.getPath() + "\" %}";
        if (node instanceof IncludeStatementNode in)
            return "{% include \"" + in.getPath() + "\" %}";
        throw unsupported(node);
    }

    private String body(List<ContentNode> nodes) {
        StringBuilder sb = new StringBuilder();
        if (nodes != null)
            for (ContentNode node : nodes)
                sb.append(content(node));
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // HTML ELEMENTS
    // ─────────────────────────────────────────────────────────────

    private String normalElement(HTMLNormalElementNode el) {
        return "<" + el.getTagName() + attributes(el.getAttributes()) + ">"
                + body(el.getChildren())
                + "</" + el.getTagName() + ">";
    }

    private String voidElement(HTMLVoidElementNode el) {
        String tag = el.getTagName().toLowerCase();
        String open = "<" + el.getTagName() + attributes(el.getAttributes());
        // real void elements are self-closing; anything else parsed as void
        // (e.g. an empty <textarea>) still needs its closing tag
        if (VOID_TAGS.contains(tag))
            return open + "/>";
        return open + "></" + el.getTagName() + ">";
    }

    private String attributes(List<HtmlAttributeNode> attributes) {
        StringBuilder sb = new StringBuilder();
        for (HtmlAttributeNode attr : attributes) {
            sb.append(' ').append(attr.getName());
            List<AttributeValuePartNode> parts = attr.getValueParts();
            if (parts == null || parts.isEmpty())
                continue; // boolean attribute, e.g. `required`
            sb.append("=\"");
            for (AttributeValuePartNode part : parts) {
                if (part instanceof AttributeTextNode text)
                    sb.append(text.getText());
                else if (part instanceof AttributeExpressionNode ex)
                    sb.append("{{ ").append(expr(ex.getExpression())).append(" }}");
                else
                    throw unsupported(part);
            }
            sb.append('"');
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // JINJA STATEMENTS
    // ─────────────────────────────────────────────────────────────

    private String forStatement(ForStatementNode fs) {
        return "{% for " + fs.getVariable().getName()
                + " in " + expr(fs.getIterable()) + " %}"
                + body(fs.getBody())
                + "{% endfor %}";
    }

    private String ifStatement(IfStatementNode is) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (IfBranchNode branch : is.getBranches()) {
            if (branch.isElseBranch()) {
                sb.append("{% else %}");
            } else {
                sb.append(first ? "{% if " : "{% elif ")
                  .append(expr(branch.getCondition())).append(" %}");
                first = false;
            }
            sb.append(body(branch.getBody()));
        }
        sb.append("{% endif %}");
        return sb.toString();
    }

    private String setStatement(SetStatementNode ss) {
        if (ss.isBlock())
            return "{% set " + ss.getVariableName() + " %}"
                    + body(ss.getBody())
                    + "{% endset %}";
        return "{% set " + ss.getVariableName() + " = " + expr(ss.getValue()) + " %}";
    }

    private String macroStatement(MacroStatementNode ms) {
        List<String> params = new ArrayList<>();
        for (ParameterNode p : ms.getParameters()) {
            String param = p.getName();
            if (p.hasDefault())
                param += "=" + expr(p.getDefaultValue());
            params.add(param);
        }
        return "{% macro " + ms.getMacroName() + "(" + String.join(", ", params) + ") %}"
                + body(ms.getBody())
                + "{% endmacro %}";
    }

    private String blockStatement(BlockStatementNode bs) {
        return "{% block " + bs.getBlockName() + " %}"
                + body(bs.getBody())
                + "{% endblock %}";
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS
    // ─────────────────────────────────────────────────────────────

    private String expr(ExpressionNode node) {
        if (node instanceof IdentifierNode id)
            return id.getName();
        if (node instanceof StringLiteralNode s)
            return s.getValue();                     // raw token, quotes included
        if (node instanceof NumberLiteralNode n)
            return n.getValue();
        if (node instanceof BooleanLiteralNode b)
            return b.getValue() ? "true" : "false";
        if (node instanceof NoneLiteralNode)
            return "none";
        if (node instanceof BinaryExpressionNode bin)
            return wrap(bin.getLeft()) + " " + operator(bin.getOperation(), bin)
                    + " " + wrap(bin.getRight());
        if (node instanceof UnaryExpressionNode un) {
            String inner = wrap(un.getExpression());
            return un.getOperation() == Operation.NOT ? "not " + inner : operator(un.getOperation(), un) + inner;
        }
        if (node instanceof PropertyAccessNode prop)
            return expr(prop.getTarget()) + "." + prop.getProperty().getName();
        if (node instanceof IndexAccessNode idx)
            return expr(idx.getTarget()) + "[" + expr(idx.getIndex()) + "]";
        if (node instanceof CallExpressionNode call)
            return expr(call.getCallee()) + "(" + arguments(call.getArguments()) + ")";
        if (node instanceof FilterExpressionNode filter)
            return filterExpression(filter);
        if (node instanceof ListExpressionNode list) {
            List<String> elements = new ArrayList<>();
            for (ExpressionNode el : list.getElements())
                elements.add(expr(el));
            return "[" + String.join(", ", elements) + "]";
        }
        if (node instanceof DictionaryExpressionNode dict)
            return dictionary(dict);
        if (node instanceof ArgumentNode arg)
            return argument(arg);
        throw unsupported(node);
    }

    /** Parenthesizes nested binary sub-expressions to keep the AST grouping. */
    private String wrap(ExpressionNode node) {
        String text = expr(node);
        return (node instanceof BinaryExpressionNode) ? "(" + text + ")" : text;
    }

    private String filterExpression(FilterExpressionNode filter) {
        String result = expr(filter.getTarget()) + "|" + filter.getFilterName();
        if (filter.getArguments() != null && !filter.getArguments().isEmpty())
            result += "(" + arguments(filter.getArguments()) + ")";
        return result;
    }

    private String arguments(List<ArgumentNode> args) {
        List<String> parts = new ArrayList<>();
        if (args != null)
            for (ArgumentNode arg : args)
                parts.add(argument(arg));
        return String.join(", ", parts);
    }

    private String argument(ArgumentNode arg) {
        if (arg.isKeyword())
            return arg.getKeyword() + "=" + expr(arg.getValue());
        return expr(arg.getValue());
    }

    private String dictionary(DictionaryExpressionNode dict) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < dict.getKeys().size(); i++)
            items.add(expr(dict.getKeys().get(i)) + ": " + expr(dict.getValues().get(i)));
        return "{" + String.join(", ", items) + "}";
    }

    private String operator(Operation op, TemplateNode node) {
        return switch (op) {
            case PLUS    -> "+";
            case MINUS   -> "-";
            case STAR    -> "*";
            case SLASH   -> "/";
            case PERCENT -> "%";
            case LT      -> "<";
            case GT      -> ">";
            case LTE     -> "<=";
            case GTE     -> ">=";
            case EQ      -> "==";
            case NEQ     -> "!=";
            case AND     -> "and";
            case OR      -> "or";
            case NOT     -> "not";
            case IN      -> "in";
            case IS      -> "is";
            default      -> throw new CodeGenError(sourceFile, node.getLineNumber(),
                    "Operator " + op + " has no Jinja2 mapping in " + node.describe());
        };
    }

    private CodeGenError unsupported(TemplateNode node) {
        return new CodeGenError(sourceFile, node.getLineNumber(),
                "No HTML/Jinja2 translation defined for AST node " + node.describe());
    }
}
