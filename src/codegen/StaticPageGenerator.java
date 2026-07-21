package codegen;

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

import resolver.ConstantValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fully evaluates a Jinja2/HTML template AST against known compile-time data
 * and emits <b>pure, final HTML</b> — every {@code {% if/for/set %}} and
 * {@code {{ expr }}} is resolved and substituted, so nothing Jinja-shaped is
 * left in the output. This is deliberately a different class from
 * {@link TemplateCodeGenerator} (which regenerates *live* Flask templates —
 * a template whose data isn't known until a real request arrives, so its
 * control flow must be preserved, not evaluated): the two classes serve
 * opposite goals against the same AST types, which is exactly why they are
 * kept separate rather than merged behind a mode flag.
 *
 * <p><b>What "fully evaluates" means in practice:</b> anything the resolver
 * can prove — a module-level literal (`products`), a literal keyword
 * argument passed to `render_template`, a `{% set %}` of a literal, a
 * `url_for(...)` call whose endpoint and arguments are all known — is
 * substituted. Anything that depends on a real HTTP request (`request`,
 * `session`, `get_flashed_messages()`) is treated as empty, since a static
 * build has no request to serve. Anything else that genuinely can't be
 * determined (a runtime-looked-up value like `product` on the details page)
 * is rendered as a visible, logged HTML comment instead of guessed at —
 * an educational compiler should show its limitations, not hide them behind
 * a wrong answer.</p>
 */
public class StaticPageGenerator {

    private final String pageName;          // used only for log messages
    private final RouteTable routes;
    private final List<String> log;

    public StaticPageGenerator(String pageName, RouteTable routes, List<String> log) {
        this.pageName = pageName;
        this.routes = routes;
        this.log = log;
    }

    public String generate(TemplateFile template, Map<String, ConstantValue> context) {
        return body(template.getContentChildren(), new Scope(context, null));
    }

    // ─────────────────────────────────────────────────────────────
    // SCOPE  (set/for bindings, chained to the outer context)
    // ─────────────────────────────────────────────────────────────

    private static final class Scope {
        final Map<String, ConstantValue> locals;
        final Scope parent;

        Scope(Map<String, ConstantValue> locals, Scope parent) {
            this.locals = locals;
            this.parent = parent;
        }

        ConstantValue resolve(String name) {
            if (locals.containsKey(name)) return locals.get(name);
            return parent != null ? parent.resolve(name) : null;
        }

        Scope child() { return new Scope(new LinkedHashMap<>(), this); }
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT DISPATCH
    // ─────────────────────────────────────────────────────────────

    private String body(List<ContentNode> nodes, Scope scope) {
        StringBuilder sb = new StringBuilder();
        if (nodes != null)
            for (ContentNode node : nodes)
                sb.append(content(node, scope));
        return sb.toString();
    }

    private String content(ContentNode node, Scope scope) {
        if (node instanceof HtmlTextNode text)
            return text.getText();
        if (node instanceof OutputNode out)
            return escapeHtml(displayValue(eval(out.getExpression(), scope)));
        if (node instanceof HTMLNormalElementNode el)
            return normalElement(el, scope);
        if (node instanceof HTMLVoidElementNode el)
            return voidElement(el, scope);
        if (node instanceof ForStatementNode fs)
            return forStatement(fs, scope);
        if (node instanceof IfStatementNode is)
            return ifStatement(is, scope);
        if (node instanceof SetStatementNode ss)
            return setStatement(ss, scope);
        if (node instanceof MacroStatementNode ms) {
            macros.put(ms.getMacroName(), ms);
            return ""; // a macro *definition* produces no output by itself
        }
        if (node instanceof BlockStatementNode bs)
            // no template inheritance in this generator — a block simply renders its own body
            return body(bs.getBody(), scope);
        if (node instanceof ExtendsStatementNode es) {
            log.add(pageName + ": {% extends \"" + es.getPath() + "\" %} is not supported by the "
                    + "static generator (no multi-template inheritance pass) — ignored.");
            return "<!-- extends \"" + es.getPath() + "\" not evaluated -->";
        }
        if (node instanceof IncludeStatementNode in) {
            log.add(pageName + ": {% include \"" + in.getPath() + "\" %} is not supported by the "
                    + "static generator — ignored.");
            return "<!-- include \"" + in.getPath() + "\" not evaluated -->";
        }
        return "<!-- unsupported node: " + node.describe() + " -->";
    }

    // ─────────────────────────────────────────────────────────────
    // HTML ELEMENTS
    // ─────────────────────────────────────────────────────────────

    private static final List<String> VOID_TAGS = List.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr");

    private String normalElement(HTMLNormalElementNode el, Scope scope) {
        return "<" + el.getTagName() + attributes(el.getAttributes(), scope) + ">"
                + body(el.getChildren(), scope)
                + "</" + el.getTagName() + ">";
    }

    private String voidElement(HTMLVoidElementNode el, Scope scope) {
        String open = "<" + el.getTagName() + attributes(el.getAttributes(), scope);
        if (VOID_TAGS.contains(el.getTagName().toLowerCase()))
            return open + "/>";
        return open + "></" + el.getTagName() + ">";
    }

    private String attributes(List<HtmlAttributeNode> attributes, Scope scope) {
        StringBuilder sb = new StringBuilder();
        for (HtmlAttributeNode attr : attributes) {
            sb.append(' ').append(attr.getName());
            List<AttributeValuePartNode> parts = attr.getValueParts();
            if (parts == null || parts.isEmpty()) continue; // boolean attribute
            sb.append("=\"");
            for (AttributeValuePartNode part : parts) {
                if (part instanceof AttributeTextNode text)
                    sb.append(text.getText());
                else if (part instanceof AttributeExpressionNode ex)
                    sb.append(escapeAttribute(displayValue(eval(ex.getExpression(), scope))));
            }
            sb.append('"');
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // STATEMENTS
    // ─────────────────────────────────────────────────────────────

    private String forStatement(ForStatementNode fs, Scope scope) {
        ConstantValue iterable = eval(fs.getIterable(), scope);
        if (iterable.getKind() != ConstantValue.Kind.LIST) {
            log.add(pageName + " line " + fs.getLineNumber() + ": {% for " + fs.getVariable().getName()
                    + " in ... %} — iterable is not a statically known list, loop left unresolved.");
            return "<!-- for loop over unresolved data not evaluated (line "
                    + fs.getLineNumber() + ") -->";
        }

        List<ConstantValue> items = iterable.asList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            Scope loopScope = scope.child();
            loopScope.locals.put(fs.getVariable().getName(), items.get(i));
            loopScope.locals.put("loop", loopMeta(i, items.size()));
            sb.append(body(fs.getBody(), loopScope));
        }
        return sb.toString();
    }

    private static ConstantValue loopMeta(int index0, int size) {
        Map<String, ConstantValue> meta = new LinkedHashMap<>();
        meta.put("index", ConstantValue.ofInt(index0 + 1));
        meta.put("index0", ConstantValue.ofInt(index0));
        meta.put("first", ConstantValue.ofBool(index0 == 0));
        meta.put("last", ConstantValue.ofBool(index0 == size - 1));
        meta.put("length", ConstantValue.ofInt(size));
        return ConstantValue.ofDict(meta);
    }

    private String ifStatement(IfStatementNode is, Scope scope) {
        for (IfBranchNode branch : is.getBranches()) {
            if (branch.isElseBranch())
                return body(branch.getBody(), scope);
            ConstantValue cond = eval(branch.getCondition(), scope);
            if (!cond.isKnown()) {
                log.add(pageName + " line " + branch.getLineNumber()
                        + ": {% if/elif %} condition depends on runtime data — branch not evaluated.");
                return "<!-- if/elif condition not statically resolvable (line "
                        + branch.getLineNumber() + ") -->";
            }
            if (isTruthy(cond))
                return body(branch.getBody(), scope);
        }
        return ""; // no branch taken and there was no else — Jinja would render nothing
    }

    private String setStatement(SetStatementNode ss, Scope scope) {
        ConstantValue value = ss.isBlock()
                ? ConstantValue.ofString(body(ss.getBody(), scope)) // block-set captures rendered text
                : eval(ss.getValue(), scope);
        scope.locals.put(ss.getVariableName(), value);
        return ""; // {% set %} never itself produces output
    }

    private final Map<String, MacroStatementNode> macros = new LinkedHashMap<>();

    // ─────────────────────────────────────────────────────────────
    // EXPRESSION EVALUATION
    // ─────────────────────────────────────────────────────────────

    private ConstantValue eval(ExpressionNode expr, Scope scope) {
        if (expr instanceof StringLiteralNode s)  return ConstantValue.ofString(unquote(s.getValue()));
        if (expr instanceof NumberLiteralNode n)
            return n.getValue().contains(".")
                    ? ConstantValue.ofFloat(Double.parseDouble(n.getValue()))
                    : ConstantValue.ofInt(Integer.parseInt(n.getValue()));
        if (expr instanceof BooleanLiteralNode b)  return ConstantValue.ofBool(b.getValue());
        if (expr instanceof NoneLiteralNode)       return ConstantValue.none();

        if (expr instanceof IdentifierNode id)     return evalIdentifier(id, scope);
        if (expr instanceof PropertyAccessNode p)   return evalProperty(p, scope);
        if (expr instanceof IndexAccessNode idx)    return evalIndex(idx, scope);
        if (expr instanceof UnaryExpressionNode un) return evalUnary(un, scope);
        if (expr instanceof BinaryExpressionNode bn) return evalBinary(bn, scope);
        if (expr instanceof CallExpressionNode call) return evalCall(call, scope);
        if (expr instanceof FilterExpressionNode f)  return evalFilter(f, scope);
        if (expr instanceof ListExpressionNode list) {
            List<ConstantValue> items = new ArrayList<>();
            for (ExpressionNode el : list.getElements()) {
                ConstantValue v = eval(el, scope);
                if (!v.isKnown()) return unresolved(list.getLineNumber(), "list element");
                items.add(v);
            }
            return ConstantValue.ofList(items);
        }
        if (expr instanceof DictionaryExpressionNode dict) {
            Map<String, ConstantValue> map = new LinkedHashMap<>();
            for (int i = 0; i < dict.getKeys().size(); i++) {
                ConstantValue k = eval(dict.getKeys().get(i), scope);
                ConstantValue v = eval(dict.getValues().get(i), scope);
                if (k.getKind() != ConstantValue.Kind.STRING || !v.isKnown())
                    return unresolved(dict.getLineNumber(), "dict entry");
                map.put(k.asString(), v);
            }
            return ConstantValue.ofDict(map);
        }
        return unresolved(expr.getLineNumber(), expr.describe());
    }

    private ConstantValue unresolved(int line, String what) {
        log.add(pageName + " line " + line + ": could not statically resolve " + what + ".");
        return ConstantValue.unknown();
    }

    /** Request-scoped names have no meaning in a static build — treated as absent/empty. */
    private static final List<String> REQUEST_SCOPED = List.of("request", "session", "config", "g");

    private ConstantValue evalIdentifier(IdentifierNode id, Scope scope) {
        ConstantValue local = scope.resolve(id.getName());
        if (local != null) return local;
        if (REQUEST_SCOPED.contains(id.getName())) return ConstantValue.ofDict(Map.of());
        return unresolved(id.getLineNumber(), "variable '" + id.getName() + "'");
    }

    private ConstantValue evalProperty(PropertyAccessNode p, Scope scope) {
        ConstantValue target = eval(p.getTarget(), scope);
        if (target.getKind() != ConstantValue.Kind.DICT)
            return unresolved(p.getLineNumber(), "property '" + p.getProperty().getName() + "'");
        ConstantValue value = target.asDict().get(p.getProperty().getName());
        return value != null ? value : unresolved(p.getLineNumber(),
                "property '" + p.getProperty().getName() + "'");
    }

    private ConstantValue evalIndex(IndexAccessNode idx, Scope scope) {
        ConstantValue target = eval(idx.getTarget(), scope);
        ConstantValue index = eval(idx.getIndex(), scope);
        if (target.getKind() == ConstantValue.Kind.LIST && index.getKind() == ConstantValue.Kind.INT) {
            List<ConstantValue> list = target.asList();
            int i = index.asInt();
            return (i >= 0 && i < list.size()) ? list.get(i) : unresolved(idx.getLineNumber(), "index " + i);
        }
        if (target.getKind() == ConstantValue.Kind.DICT && index.getKind() == ConstantValue.Kind.STRING) {
            ConstantValue value = target.asDict().get(index.asString());
            return value != null ? value : unresolved(idx.getLineNumber(), "key '" + index.asString() + "'");
        }
        return unresolved(idx.getLineNumber(), "index access");
    }

    private ConstantValue evalUnary(UnaryExpressionNode un, Scope scope) {
        ConstantValue operand = eval(un.getExpression(), scope);
        if (un.getOperation() == Operation.NOT)
            return operand.isKnown() ? ConstantValue.ofBool(!isTruthy(operand)) : ConstantValue.unknown();
        if (un.getOperation() == Operation.MINUS && operand.isKnown()) {
            if (operand.getKind() == ConstantValue.Kind.INT) return ConstantValue.ofInt(-operand.asInt());
            if (operand.getKind() == ConstantValue.Kind.FLOAT) return ConstantValue.ofFloat(-operand.asFloat());
        }
        return unresolved(un.getLineNumber(), "unary operator");
    }

    private ConstantValue evalBinary(BinaryExpressionNode bin, Scope scope) {
        Operation op = bin.getOperation();
        if (op == Operation.AND || op == Operation.OR) {
            ConstantValue left = eval(bin.getLeft(), scope);
            if (!left.isKnown()) return unresolved(bin.getLineNumber(), "operand");
            boolean leftTrue = isTruthy(left);
            if (op == Operation.AND && !leftTrue) return left;
            if (op == Operation.OR && leftTrue) return left;
            return eval(bin.getRight(), scope);
        }

        ConstantValue left = eval(bin.getLeft(), scope);
        ConstantValue right = eval(bin.getRight(), scope);
        if (!left.isKnown() || !right.isKnown()) return unresolved(bin.getLineNumber(), "operand");

        if (op == Operation.EQ)  return ConstantValue.ofBool(constantsEqual(left, right));
        if (op == Operation.NEQ) return ConstantValue.ofBool(!constantsEqual(left, right));

        if (op == Operation.PLUS && left.getKind() == ConstantValue.Kind.STRING
                && right.getKind() == ConstantValue.Kind.STRING)
            return ConstantValue.ofString(left.asString() + right.asString());

        if (isNumeric(left) && isNumeric(right)) {
            double a = numeric(left), b = numeric(right);
            boolean bothInt = left.getKind() == ConstantValue.Kind.INT && right.getKind() == ConstantValue.Kind.INT;
            return switch (op) {
                case PLUS    -> bothInt ? ConstantValue.ofInt((int) (a + b)) : ConstantValue.ofFloat(a + b);
                case MINUS   -> bothInt ? ConstantValue.ofInt((int) (a - b)) : ConstantValue.ofFloat(a - b);
                case STAR    -> bothInt ? ConstantValue.ofInt((int) (a * b)) : ConstantValue.ofFloat(a * b);
                case SLASH   -> ConstantValue.ofFloat(a / b);
                case PERCENT -> bothInt ? ConstantValue.ofInt((int) a % (int) b) : ConstantValue.ofFloat(a % b);
                case LT      -> ConstantValue.ofBool(a < b);
                case GT      -> ConstantValue.ofBool(a > b);
                case LTE     -> ConstantValue.ofBool(a <= b);
                case GTE     -> ConstantValue.ofBool(a >= b);
                default      -> unresolved(bin.getLineNumber(), "operator " + op);
            };
        }
        return unresolved(bin.getLineNumber(), "operator " + op + " on " + left.getKind() + "/" + right.getKind());
    }

    private static boolean isNumeric(ConstantValue v) {
        return v.getKind() == ConstantValue.Kind.INT || v.getKind() == ConstantValue.Kind.FLOAT;
    }

    private static double numeric(ConstantValue v) {
        return v.getKind() == ConstantValue.Kind.INT ? v.asInt() : v.asFloat();
    }

    private static boolean constantsEqual(ConstantValue a, ConstantValue b) {
        if (a.getKind() != b.getKind()) return isNumeric(a) && isNumeric(b) && numeric(a) == numeric(b);
        return switch (a.getKind()) {
            case STRING -> a.asString().equals(b.asString());
            case INT    -> a.asInt() == b.asInt();
            case FLOAT  -> a.asFloat() == b.asFloat();
            case BOOL   -> a.asBool() == b.asBool();
            case NONE   -> true;
            default     -> false;
        };
    }

    /** Python/Jinja truthiness: None/False/0/0.0/""/[]/{} are falsy, everything else truthy. */
    private static boolean isTruthy(ConstantValue v) {
        return switch (v.getKind()) {
            case NONE, UNKNOWN -> false;
            case BOOL   -> v.asBool();
            case INT    -> v.asInt() != 0;
            case FLOAT  -> v.asFloat() != 0.0;
            case STRING -> !v.asString().isEmpty();
            case LIST   -> !v.asList().isEmpty();
            case DICT   -> !v.asDict().isEmpty();
        };
    }

    // ─────────────────────────────────────────────────────────────
    // CALLS  (url_for, get_flashed_messages, macros)
    // ─────────────────────────────────────────────────────────────

    private ConstantValue evalCall(CallExpressionNode call, Scope scope) {
        if (!(call.getCallee() instanceof IdentifierNode callee))
            return unresolved(call.getLineNumber(), "call target");

        return switch (callee.getName()) {
            case "url_for" -> evalUrlFor(call, scope);
            case "get_flashed_messages" -> ConstantValue.ofList(List.of()); // no request in a static build
            default -> macros.containsKey(callee.getName())
                    ? evalMacroCall(macros.get(callee.getName()), call, scope)
                    : unresolved(call.getLineNumber(), "call to '" + callee.getName() + "'");
        };
    }

    private ConstantValue evalUrlFor(CallExpressionNode call, Scope scope) {
        List<ArgumentNode> args = call.getArguments();
        if (args.isEmpty() || args.get(0).isKeyword())
            return unresolved(call.getLineNumber(), "url_for endpoint");

        ConstantValue endpointValue = eval(args.get(0).getValue(), scope);
        if (endpointValue.getKind() != ConstantValue.Kind.STRING)
            return unresolved(call.getLineNumber(), "url_for endpoint");

        Map<String, String> kwargs = new LinkedHashMap<>();
        for (int i = 1; i < args.size(); i++) {
            ArgumentNode arg = args.get(i);
            if (!arg.isKeyword()) continue;
            ConstantValue value = eval(arg.getValue(), scope);
            if (!value.isKnown()) return unresolved(call.getLineNumber(), "url_for argument");
            kwargs.put(arg.getKeyword(), displayValue(value));
        }

        String resolved = routes.resolve(endpointValue.asString(), kwargs);
        return resolved != null
                ? ConstantValue.ofString(resolved)
                : unresolved(call.getLineNumber(), "url_for('" + endpointValue.asString() + "')");
    }

    private ConstantValue evalMacroCall(MacroStatementNode macro, CallExpressionNode call, Scope scope) {
        Scope macroScope = new Scope(new LinkedHashMap<>(), null); // macros don't close over the call site
        List<ParameterNode> params = macro.getParameters();
        List<ArgumentNode> args = call.getArguments();

        for (int i = 0; i < params.size(); i++) {
            ParameterNode param = params.get(i);
            ConstantValue value = null;
            for (ArgumentNode arg : args)
                if (arg.isKeyword() && arg.getKeyword().equals(param.getName()))
                    value = eval(arg.getValue(), scope);
            if (value == null && i < args.size() && !args.get(i).isKeyword())
                value = eval(args.get(i).getValue(), scope);
            if (value == null && param.hasDefault())
                value = eval(param.getDefaultValue(), macroScope);
            macroScope.locals.put(param.getName(), value != null ? value : ConstantValue.unknown());
        }
        return ConstantValue.ofString(body(macro.getBody(), macroScope));
    }

    // ─────────────────────────────────────────────────────────────
    // FILTERS
    // ─────────────────────────────────────────────────────────────

    private ConstantValue evalFilter(FilterExpressionNode filter, Scope scope) {
        ConstantValue target = eval(filter.getTarget(), scope);
        if (!target.isKnown())
            return unresolved(filter.getLineNumber(), "filter '" + filter.getFilterName() + "' target");

        List<ConstantValue> args = new ArrayList<>();
        for (ArgumentNode arg : filter.getArguments()) {
            ConstantValue v = eval(arg.getValue(), scope);
            if (!v.isKnown()) return unresolved(filter.getLineNumber(), "filter argument");
            args.add(v);
        }

        return switch (filter.getFilterName()) {
            case "length", "count" -> ConstantValue.ofInt(lengthOf(target));
            case "upper"      -> ConstantValue.ofString(target.asString().toUpperCase());
            case "lower"      -> ConstantValue.ofString(target.asString().toLowerCase());
            case "capitalize" -> ConstantValue.ofString(capitalize(target.asString()));
            case "title"      -> ConstantValue.ofString(titleCase(target.asString()));
            case "trim"       -> ConstantValue.ofString(target.asString().strip());
            case "default"    -> isTruthy(target) ? target : (args.isEmpty() ? target : args.get(0));
            case "format"     -> pythonPercentFormat(target.asString(), args, filter.getLineNumber());
            default -> unresolved(filter.getLineNumber(), "filter '" + filter.getFilterName() + "'");
        };
    }

    private int lengthOf(ConstantValue v) {
        return switch (v.getKind()) {
            case LIST   -> v.asList().size();
            case DICT   -> v.asDict().size();
            case STRING -> v.asString().length();
            default     -> 0;
        };
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    private static String titleCase(String s) {
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++)
            if (!words[i].isEmpty())
                words[i] = Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1).toLowerCase();
        return String.join(" ", words);
    }

    /** Supports the common Python %-format conversions used in Jinja templates: %s, %d, %.<N>f. */
    private ConstantValue pythonPercentFormat(String pyFormat, List<ConstantValue> args, int line) {
        try {
            Object[] javaArgs = new Object[args.size()];
            for (int i = 0; i < args.size(); i++) {
                ConstantValue v = args.get(i);
                javaArgs[i] = switch (v.getKind()) {
                    case INT    -> v.asInt();
                    case FLOAT  -> v.asFloat();
                    default     -> v.getKind() == ConstantValue.Kind.STRING ? v.asString() : v.display();
                };
            }
            return ConstantValue.ofString(String.format(pyFormat, javaArgs));
        } catch (RuntimeException e) {
            return unresolved(line, "format spec '" + pyFormat + "'");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DISPLAY / ESCAPING
    // ─────────────────────────────────────────────────────────────

    /** Renders a resolved value the way Jinja's {{ }} would print it as text. */
    private String displayValue(ConstantValue v) {
        return switch (v.getKind()) {
            case STRING -> v.asString();
            case INT    -> String.valueOf(v.asInt());
            case FLOAT  -> String.valueOf(v.asFloat());
            case BOOL   -> v.asBool() ? "True" : "False";
            case NONE   -> "None";
            case UNKNOWN -> ""; // already logged by whoever produced UNKNOWN
            default     -> v.display();
        };
    }

    /** Flask/Jinja auto-escape HTML text content by default for .html templates. */
    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String escapeAttribute(String s) {
        return escapeHtml(s).replace("\"", "&quot;");
    }

    private static String unquote(String raw) {
        if (raw.length() >= 2 && (raw.startsWith("'") || raw.startsWith("\""))
                && raw.endsWith(raw.substring(0, 1)))
            return raw.substring(1, raw.length() - 1);
        return raw;
    }
}
