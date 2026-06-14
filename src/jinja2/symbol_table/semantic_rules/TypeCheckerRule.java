package jinja2.symbol_table.semantic_rules;

import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
import jinja2.models.content.*;
import jinja2.models.content.html.*;
import jinja2.models.expression.*;
import jinja2.models.expression.literal.*;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.*;
import jinja2.symbol_table.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeCheckerRule implements ISemanticRule {

    // ─────────────────────────────────────────────────────────────
    // STATIC TABLES
    // ─────────────────────────────────────────────────────────────

    private static final Set<SymbolType> INDEXABLE = Set.of(
            SymbolType.LIST, SymbolType.DICT, SymbolType.STRING, SymbolType.ANY
    );

    private static final Map<SymbolType, Set<String>> KNOWN_PROPERTIES = Map.of(
            SymbolType.STRING, Set.of(
                    "upper", "lower", "title", "capitalize",
                    "strip", "lstrip", "rstrip",
                    "replace", "split", "join",
                    "startswith", "endswith",
                    "format", "count", "find", "center", "zfill"
            ),
            SymbolType.LIST, Set.of(
                    "append", "extend", "insert", "remove",
                    "pop", "sort", "reverse", "count", "index", "copy"
            ),
            SymbolType.DICT, Set.of(
                    "keys", "values", "items", "get",
                    "update", "pop", "setdefault", "copy", "clear"
            )
    );

    // ─────────────────────────────────────────────────────────────
    // ISemanticRule
    // ─────────────────────────────────────────────────────────────

    @Override
    public void validate(SemanticContext ctx) {
        walkTemplateFile(ctx.root(), ctx);
    }

    // ─────────────────────────────────────────────────────────────
    // AST TRAVERSAL
    // ─────────────────────────────────────────────────────────────

    private void walkTemplateFile(TemplateFile template, SemanticContext ctx) {
        for (ContentNode child : template.getContentChildren())
            walkContent(child, ctx);
    }

    private void walkContent(ContentNode node, SemanticContext ctx) {
        if (node instanceof ForStatementNode fs) {
            checkExpr(fs.getIterable(), ctx);
            for (ContentNode child : fs.getBody()) walkContent(child, ctx);
        }
        else if (node instanceof IfStatementNode is) {
            for (IfBranchNode branch : is.getBranches()) {
                if (branch.getCondition() != null) checkExpr(branch.getCondition(), ctx);
                for (ContentNode child : branch.getBody()) walkContent(child, ctx);
            }
        }
        else if (node instanceof SetStatementNode ss) {
            if (ss.isBlock()) for (ContentNode child : ss.getBody()) walkContent(child, ctx);
            else checkExpr(ss.getValue(), ctx);
        }
        else if (node instanceof MacroStatementNode ms) {
            for (ParameterNode p : ms.getParameters())
                if (p.hasDefault()) checkExpr(p.getDefaultValue(), ctx);
            for (ContentNode child : ms.getBody()) walkContent(child, ctx);
        }
        else if (node instanceof BlockStatementNode bs) {
            for (ContentNode child : bs.getBody()) walkContent(child, ctx);
        }
        else if (node instanceof OutputNode out) {
            checkExpr(out.getExpression(), ctx);
        }
        else if (node instanceof HTMLNormalElementNode el) {
            walkAttributes(el.getAttributes(), ctx);
            for (ContentNode child : el.getChildren()) walkContent(child, ctx);
        }
        else if (node instanceof HTMLVoidElementNode el) {
            walkAttributes(el.getAttributes(), ctx);
        }
    }

    private void walkAttributes(List<HtmlAttributeNode> attrs, SemanticContext ctx) {
        for (HtmlAttributeNode attr : attrs)
            for (AttributeValuePartNode part : attr.getValueParts())
                if (part instanceof AttributeExpressionNode ep)
                    checkExpr(ep.getExpression(), ctx);
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSION DISPATCH  (side-effects only — finds and reports errors)
    // ─────────────────────────────────────────────────────────────

    private void checkExpr(ExpressionNode expr, SemanticContext ctx) {
        if (expr instanceof CallExpressionNode   call) checkCall(call, ctx);
        else if (expr instanceof IndexAccessNode      idx)  checkIndex(idx, ctx);
        else if (expr instanceof BinaryExpressionNode bin)  checkBinary(bin, ctx);
        else if (expr instanceof UnaryExpressionNode  un)   checkUnary(un, ctx);
        else if (expr instanceof PropertyAccessNode   prop) checkExpr(prop.getTarget(), ctx);
        else if (expr instanceof FilterExpressionNode f) {
            checkExpr(f.getTarget(), ctx);
            for (ArgumentNode arg : f.getArguments()) checkExpr(arg.getValue(), ctx);
        }
        else if (expr instanceof ListExpressionNode list) {
            for (ExpressionNode el : list.getElements()) checkExpr(el, ctx);
        }
        else if (expr instanceof DictionaryExpressionNode dict) {
            for (ExpressionNode k : dict.getKeys())   checkExpr(k, ctx);
            for (ExpressionNode v : dict.getValues()) checkExpr(v, ctx);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_ERROR — call, index, unary
    // ─────────────────────────────────────────────────────────────

    private void checkCall(CallExpressionNode call, SemanticContext ctx) {
        for (ArgumentNode arg : call.getArguments())
            checkExpr(arg.getValue(), ctx);

        ExpressionNode callee = call.getCallee();

        if (callee instanceof PropertyAccessNode prop) {
            checkExpr(prop.getTarget(), ctx);
            SymbolType targetType   = resolveType(prop.getTarget(), ctx.symbolTable());
            String     propertyName = prop.getProperty().getName();
            checkPropertyCall(targetType, propertyName, call.getLineNumber(), ctx);
        } else {
            checkExpr(callee, ctx);
            SymbolType calleeType = resolveType(callee, ctx.symbolTable());
            if (calleeType != SymbolType.ANY && calleeType != SymbolType.CALLABLE) {
                error(ctx, CompilerError.Kind.TYPE_ERROR,
                        "Value of type " + calleeType + " is not callable",
                        call.getLineNumber());
            }
        }
    }

    private void checkPropertyCall(
            SymbolType targetType, String property, int line, SemanticContext ctx) {
        if (targetType == SymbolType.ANY || targetType == SymbolType.CALLABLE) return;

        Set<String> knownProps = KNOWN_PROPERTIES.get(targetType);
        if (knownProps == null || !knownProps.contains(property)) {
            error(ctx, CompilerError.Kind.TYPE_ERROR,
                    "Type " + targetType + " has no property '" + property + "'", line);
        }
    }

    private void checkIndex(IndexAccessNode idx, SemanticContext ctx) {
        checkExpr(idx.getTarget(), ctx);
        checkExpr(idx.getIndex(),  ctx);

        SymbolType targetType = resolveType(idx.getTarget(), ctx.symbolTable());
        if (!INDEXABLE.contains(targetType)) {
            error(ctx, CompilerError.Kind.TYPE_ERROR,
                    "Type " + targetType + " does not support index access",
                    idx.getLineNumber());
        }
    }

    private void checkUnary(UnaryExpressionNode un, SemanticContext ctx) {
        checkExpr(un.getExpression(), ctx);
        if (un.getOperation() == Operation.MINUS) {
            SymbolType operand = resolveType(un.getExpression(), ctx.symbolTable());
            if (operand != SymbolType.ANY && operand != SymbolType.NUMBER)
                error(ctx, CompilerError.Kind.TYPE_ERROR,
                        "Unary '-' cannot be applied to " + operand, un.getLineNumber());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_MISMATCH — binary operations
    // ─────────────────────────────────────────────────────────────

    private void checkBinary(BinaryExpressionNode bin, SemanticContext ctx) {
        checkExpr(bin.getLeft(),  ctx);
        checkExpr(bin.getRight(), ctx);

        SymbolType left  = resolveType(bin.getLeft(),  ctx.symbolTable());
        SymbolType right = resolveType(bin.getRight(), ctx.symbolTable());

        if (left == SymbolType.ANY || right == SymbolType.ANY) return;

        if (!isCompatible(left, right, bin.getOperation())) {
            error(ctx, CompilerError.Kind.TYPE_MISMATCH,
                    "Operator " + bin.getOperation()
                            + " cannot be applied to " + left + " and " + right,
                    bin.getLineNumber());
        }
    }

    private boolean isCompatible(SymbolType left, SymbolType right, Operation op) {
        return switch (op) {
            case MINUS, SLASH, PERCENT ->
                    left == SymbolType.NUMBER && right == SymbolType.NUMBER;
            case PLUS ->
                    (left == SymbolType.NUMBER && right == SymbolType.NUMBER)
                            || (left == SymbolType.STRING && right == SymbolType.STRING);
            case STAR ->
                    (left == SymbolType.NUMBER && right == SymbolType.NUMBER)
                            || (left == SymbolType.STRING && right == SymbolType.NUMBER);
            case LT, GT, LTE, GTE ->
                    (left == SymbolType.NUMBER && right == SymbolType.NUMBER)
                            || (left == SymbolType.STRING && right == SymbolType.STRING);
            case EQ, NEQ, AND, OR, IS, NOT -> true;
            case IN -> right == SymbolType.LIST
                    || right == SymbolType.DICT
                    || right == SymbolType.STRING;
        };
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE RESOLUTION  (recursive — the single source of truth)
    // ─────────────────────────────────────────────────────────────

    /**
     * Resolves the statically-known SymbolType of an expression.
     * Returns ANY when the type can only be determined at runtime.
     * This is recursive: a BinaryExpressionNode's result type is inferred
     * from its operands' resolved types, so chains like (5 + 3) + "x"
     * correctly propagate NUMBER upward and catch the outer mismatch.
     * For identifiers, the stored ExpressionNode in Symbol is re-resolved
     * here rather than trusting sym.getType(), because sym.getType() was
     * computed shallowly at construction time before the full symbol table
     * existed. At check time we have everything, so we can go deeper.
     */
    public SymbolType resolveType(ExpressionNode expr, SymbolTable symbolTable) {
        if (expr instanceof StringLiteralNode)        return SymbolType.STRING;
        if (expr instanceof NumberLiteralNode)        return SymbolType.NUMBER;
        if (expr instanceof BooleanLiteralNode)       return SymbolType.BOOLEAN;
        if (expr instanceof NoneLiteralNode)          return SymbolType.NONE;
        if (expr instanceof ListExpressionNode)       return SymbolType.LIST;
        if (expr instanceof DictionaryExpressionNode) return SymbolType.DICT;

        if (expr instanceof IdentifierNode id) {
            Symbol sym = symbolTable.resolveGlobal(id.getName());
            if (sym == null) return SymbolType.ANY;
            // Re-resolve the stored expression with the now-complete symbol table.
            // sym.getType() was computed shallowly at construction time and may
            // be ANY for non-literal assignments like {% set x = 5 + 3 %}.
            if (sym.getValue() != null)
                return resolveType(sym.getValue(), symbolTable);
            return sym.getType(); // macros (CALLABLE), loop vars, params (ANY)
        }

        if (expr instanceof UnaryExpressionNode un) {
            return switch (un.getOperation()) {
                case NOT   -> SymbolType.BOOLEAN;
                case MINUS -> SymbolType.NUMBER;
                default    -> SymbolType.ANY;
            };
        }

        if (expr instanceof BinaryExpressionNode bin) {
            SymbolType left  = resolveType(bin.getLeft(),  symbolTable);
            SymbolType right = resolveType(bin.getRight(), symbolTable);
            return inferBinaryResultType(left, right, bin.getOperation());
        }

        // Calls, filters, property/index access — return type not statically known
        return SymbolType.ANY;
    }

    /**
     * Infers the result type of a binary operation from its operand types.
     * Returns ANY if either operand is ANY (skip, can't know) or if the
     * combination is invalid (error already reported by checkBinary, and
     * returning ANY prevents cascading false positives in the parent expression).
     */
    private SymbolType inferBinaryResultType(SymbolType left, SymbolType right, Operation op) {
        if (left == SymbolType.ANY || right == SymbolType.ANY) return SymbolType.ANY;

        return switch (op) {
            case PLUS -> {
                if (left == SymbolType.NUMBER && right == SymbolType.NUMBER) yield SymbolType.NUMBER;
                if (left == SymbolType.STRING && right == SymbolType.STRING) yield SymbolType.STRING;
                yield SymbolType.ANY; // invalid — error already reported
            }
            case MINUS, SLASH, PERCENT -> {
                if (left == SymbolType.NUMBER && right == SymbolType.NUMBER) yield SymbolType.NUMBER;
                yield SymbolType.ANY;
            }
            case STAR -> {
                if (left == SymbolType.NUMBER && right == SymbolType.NUMBER) yield SymbolType.NUMBER;
                if (left == SymbolType.STRING && right == SymbolType.NUMBER) yield SymbolType.STRING;
                yield SymbolType.ANY;
            }
            case LT, GT, LTE, GTE, EQ, NEQ, AND, OR, IN, IS, NOT -> SymbolType.BOOLEAN;
            default -> SymbolType.ANY;
        };
    }

    // ─────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────

    private void error(SemanticContext ctx, CompilerError.Kind kind, String msg, int line) {
        ctx.errors().add(new CompilerError(kind, msg, line));
    }
}