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
import jinja2.tests.JinjaTestDefinition;
import jinja2.tests.JinjaTestRegistry;

import java.util.*;

public class TypeCheckerRule implements ISemanticRule {

    // ─────────────────────────────────────────────────────────────
    // STATIC TABLES
    // ─────────────────────────────────────────────────────────────
    private final JinjaTestRegistry testRegistry;

    public TypeCheckerRule(
            JinjaTestRegistry testRegistry
    ) {
        this.testRegistry =
                Objects.requireNonNull(testRegistry);
    }
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
            if (ss.isBlock()
                    && ss.getTargets().size() != 1) {
                error(
                        ctx,
                        CompilerError.Kind.TYPE_ERROR,
                        "Block set requires exactly one target",
                        ss.getLineNumber()
                );
            }
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
        else if (expr instanceof TestExpressionNode test)
            checkTest(test, ctx);
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

    private void checkUnary(
            UnaryExpressionNode expression,
            SemanticContext context
    ) {
        checkExpr(
                expression.getExpression(),
                context
        );

        Operation operation =
                expression.getOperation();

        if (operation != Operation.PLUS
                && operation != Operation.MINUS) {
            return;
        }

        SymbolType operandType = resolveType(
                expression.getExpression(),
                context.symbolTable()
        );

        if (operandType != SymbolType.ANY
                && operandType != SymbolType.NUMBER) {
            error(
                    context,
                    CompilerError.Kind.TYPE_ERROR,
                    "Unary '"
                            + (operation == Operation.PLUS ? "+" : "-")
                            + "' cannot be applied to "
                            + operandType,
                    expression.getLineNumber()
            );
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
    private void checkTest(
            TestExpressionNode test,
            SemanticContext context
    ) {
        checkExpr(test.getValue(), context);

        for (ArgumentNode argument : test.getArguments()) {
            checkExpr(argument.getValue(), context);
        }

        JinjaTestDefinition definition =
                testRegistry.find(test.getTestName())
                        .orElse(null);

        if (definition == null) {
            error(
                    context,
                    CompilerError.Kind.TYPE_ERROR,
                    "Unknown Jinja test '"
                            + test.getTestName()
                            + "'",
                    test.getLineNumber()
            );

            return;
        }

        int argumentCount =
                test.getArguments().size();

        if (!definition.acceptsArgumentCount(argumentCount)) {
            error(
                    context,
                    CompilerError.Kind.TYPE_ERROR,
                    "Jinja test '"
                            + test.getTestName()
                            + "' received "
                            + argumentCount
                            + " argument(s), but expects between "
                            + definition.minimumArguments()
                            + " and "
                            + definition.maximumArguments(),
                    test.getLineNumber()
            );
        }

        for (ArgumentNode argument : test.getArguments()) {
            if (argument.isKeyword()) {
                error(
                        context,
                        CompilerError.Kind.TYPE_ERROR,
                        "Keyword arguments are not supported for Jinja tests yet",
                        argument.getLineNumber()
                );
            }
        }
    }
    private boolean isCompatible(SymbolType left, SymbolType right, Operation op) {
        return switch (op) {
            case MINUS, SLASH, PERCENT ->
                    left == SymbolType.NUMBER && right == SymbolType.NUMBER;
            case STAR ->
                    (left == SymbolType.NUMBER && right == SymbolType.NUMBER)
                            || (left == SymbolType.STRING && right == SymbolType.NUMBER);
            case PLUS, LT, GT, LTE, GTE ->
                    (left == SymbolType.NUMBER && right == SymbolType.NUMBER)
                            || (left == SymbolType.STRING && right == SymbolType.STRING);
            case EQ, NEQ, AND, OR -> true;
            case IN -> right == SymbolType.LIST
                    || right == SymbolType.DICT
                    || right == SymbolType.STRING;
            case NOT -> throw new IllegalStateException(
                    "Unary NOT was passed to binary compatibility checking"
            );
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
    public SymbolType resolveType(
            ExpressionNode expression,
            SymbolTable symbolTable
    ) {
        Set<Symbol> resolvingSymbols =
                Collections.newSetFromMap(
                        new IdentityHashMap<>()
                );

        return resolveType(
                expression,
                symbolTable,
                resolvingSymbols
        );
    }
    private SymbolType resolveType(
            ExpressionNode expression,
            SymbolTable symbolTable,
            Set<Symbol> resolvingSymbols
    ) {
        if (expression instanceof StringLiteralNode) {
            return SymbolType.STRING;
        }

        if (expression instanceof NumberLiteralNode) {
            return SymbolType.NUMBER;
        }

        if (expression instanceof BooleanLiteralNode) {
            return SymbolType.BOOLEAN;
        }

        if (expression instanceof NoneLiteralNode) {
            return SymbolType.NONE;
        }

        if (expression instanceof ListExpressionNode) {
            return SymbolType.LIST;
        }

        if (expression instanceof DictionaryExpressionNode) {
            return SymbolType.DICT;
        }
        if (expression instanceof TestExpressionNode)
            return SymbolType.BOOLEAN;
        if (expression instanceof IdentifierNode identifier) {
            /*
             * Prefer the declaration binding recorded while the symbol
             * table was built. This preserves which version of a
             * reassigned variable this specific identifier referred to.
             */
            Symbol symbol =
                    symbolTable.getBinding(identifier);

            /*
             * Fallback for identifiers that were not explicitly bound,
             * such as externally supplied context values.
             */
            if (symbol == null) {
                symbol = symbolTable.resolveGlobal(
                        identifier.getName()
                );
            }

            if (symbol == null) {
                return SymbolType.ANY;
            }

            if (symbol.getValue() == null) {
                return symbol.getType();
            }

            /*
             * Protect against malformed or genuinely recursive symbol
             * definitions. Returning ANY is safer than overflowing and
             * avoids producing false secondary errors.
             */
            if (!resolvingSymbols.add(symbol)) {
                return SymbolType.ANY;
            }

            try {
                return resolveType(
                        symbol.getValue(),
                        symbolTable,
                        resolvingSymbols
                );
            } finally {
                resolvingSymbols.remove(symbol);
            }
        }

        if (expression instanceof UnaryExpressionNode unary) {
            return switch (unary.getOperation()) {
                case NOT -> SymbolType.BOOLEAN;
                case PLUS, MINUS -> SymbolType.NUMBER;
                default -> SymbolType.ANY;
            };
        }

        if (expression instanceof BinaryExpressionNode binary) {
            SymbolType left = resolveType(
                    binary.getLeft(),
                    symbolTable,
                    resolvingSymbols
            );

            SymbolType right = resolveType(
                    binary.getRight(),
                    symbolTable,
                    resolvingSymbols
            );

            return inferBinaryResultType(
                    left,
                    right,
                    binary.getOperation()
            );
        }

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
            case LT, GT, LTE, GTE, EQ, NEQ, IN, NOT -> SymbolType.BOOLEAN;
            case AND, OR ->
                    left == right
                            ? left
                            : SymbolType.ANY;
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