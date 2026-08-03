package python.symbol_table.semantic_rules;

import python.models.ASTNode;
import python.models.atom_statement.*;
import python.models.enums.Operation;
import python.models.expr_statement.*;
import python.models.funcdef.Parameter;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.symbol_table.CompilerError;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;
import python.symbol_table.SymbolTable;
import python.symbol_table.SymbolTableBuilder;
import python.symbol_table.SymbolType;

import java.util.List;
import java.util.Set;

/**
 * Static type checking for the Python front end — the counterpart of
 * {@code jinja2.symbol_table.semantic_rules.TypeCheckerRule}.
 *
 * <p>Reports:</p>
 * <ul>
 *   <li><b>TypeError</b> — an operation a value's type does not support:
 *       {@code 5 + "hello"}, calling a non-callable, indexing an int;</li>
 *   <li><b>TypeMismatchError</b> — an argument whose type contradicts the
 *       parameter's annotation: {@code def f(a: int)} called as {@code f("x")};</li>
 *   <li><b>ArgumentCountError</b> — a call with too few or too many arguments.</li>
 * </ul>
 *
 * <p>Every check is skipped when a type resolves to {@link SymbolType#ANY}: the rule
 * only reports what it can prove, so an incomplete inference never becomes a false
 * error in the report.</p>
 */
public class TypeCheckerRule implements ISemanticRule {

    private static final Set<SymbolType> INDEXABLE = Set.of(
            SymbolType.LIST, SymbolType.DICT, SymbolType.STRING, SymbolType.ANY
    );

    @Override
    public void validate(SemanticContext context) {
        walk(context.root(), context);
    }

    // ─────────────────────────────────────────────────────────────
    // AST TRAVERSAL
    // ─────────────────────────────────────────────────────────────

    private void walk(ASTNode node, SemanticContext context) {
        if (node == null) return;

        if (node instanceof BinaryExpression binary)
            checkBinary(binary, context);
        else if (node instanceof IDTrailer idTrailer)
            checkIdTrailer(idTrailer, context);

        for (ASTNode child : node.getChildren())
            walk(child, context);
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_ERROR — binary operations
    // ─────────────────────────────────────────────────────────────

    private void checkBinary(BinaryExpression binary, SemanticContext context) {
        SymbolType left  = resolveType(binary.left,  context.symbolTable());
        SymbolType right = resolveType(binary.right, context.symbolTable());

        if (!left.isKnown() || !right.isKnown()) return;
        if (isCompatible(left, right, binary.operation)) return;

        context.error(
                CompilerError.Kind.TYPE_ERROR,
                "Unsupported operand types for " + symbolOf(binary.operation)
                        + ": " + left + " and " + right,
                binary.getLine(),
                null,
                null);
    }

    private static boolean isCompatible(SymbolType left, SymbolType right, Operation op) {
        return switch (op) {
            case ADD ->
                    (left.isNumeric() && right.isNumeric())
                            || (left == SymbolType.STRING && right == SymbolType.STRING)
                            || (left == SymbolType.LIST && right == SymbolType.LIST);
            case SUB, POWER, IDIV, DIV ->
                    left.isNumeric() && right.isNumeric();
            case MOD ->
                    // "%s items" % count is Python's old-style string formatting
                    (left.isNumeric() && right.isNumeric())
                            || left == SymbolType.STRING;
            case MULT ->
                    (left.isNumeric() && right.isNumeric())
                            || (left == SymbolType.STRING && right.isNumeric())
                            || (left.isNumeric() && right == SymbolType.STRING)
                            || (left == SymbolType.LIST && right.isNumeric())
                            || (left.isNumeric() && right == SymbolType.LIST);
            case AND, OR, XOR, LSHIFT, RSHIFT ->
                    left.isNumeric() && right.isNumeric();
            // comparisons, `in`, `is`: Python accepts these across types
            default -> true;
        };
    }

    private static String symbolOf(Operation operation) {
        return switch (operation) {
            case ADD    -> "+";
            case SUB    -> "-";
            case MULT   -> "*";
            case DIV    -> "/";
            case IDIV   -> "//";
            case MOD    -> "%";
            case POWER  -> "**";
            case AND    -> "&";
            case OR     -> "|";
            case XOR    -> "^";
            case LSHIFT -> "<<";
            case RSHIFT -> ">>";
            default     -> operation.name();
        };
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_ERROR / TYPE_MISMATCH / ARGUMENT_COUNT — calls and subscripts
    // ─────────────────────────────────────────────────────────────

    private void checkIdTrailer(IDTrailer idTrailer, SemanticContext context) {
        if (idTrailer.trailers == null || idTrailer.trailers.isEmpty()) return;

        Trailer first = idTrailer.trailers.get(0);

        /*
         * Only the FIRST trailer applies to the name itself. In `products.append(x)`
         * the call applies to the attribute, not to `products`, so there is nothing
         * this rule can prove about it.
         */
        if (first.isDotIdTrailer()) return;

        Symbol callee = context.symbolTable().getBinding(idTrailer.id);

        if (first.arguments instanceof SubscriptArguments) {
            checkSubscript(idTrailer, callee, context);
            return;
        }

        if (first.arguments instanceof CallArguments callArguments)
            checkCall(idTrailer, callee, callArguments, context);
    }

    private void checkSubscript(IDTrailer idTrailer, Symbol target, SemanticContext context) {
        SymbolType type = target == null ? SymbolType.ANY : target.getType();

        if (INDEXABLE.contains(type)) return;

        context.error(
                CompilerError.Kind.TYPE_ERROR,
                "Type " + type + " does not support index access for '"
                        + idTrailer.id.name + "'",
                idTrailer.getLine(),
                null,
                idTrailer.id.name);
    }

    private void checkCall(IDTrailer idTrailer,
                           Symbol callee,
                           CallArguments callArguments,
                           SemanticContext context) {

        if (callee == null) return;   // unresolved — already reported as UndefinedError

        SymbolType calleeType = callee.getType();

        if (calleeType.isKnown() && calleeType != SymbolType.CALLABLE) {
            context.error(
                    CompilerError.Kind.TYPE_ERROR,
                    "'" + idTrailer.id.name + "' of type " + calleeType + " is not callable",
                    idTrailer.getLine(),
                    null,
                    idTrailer.id.name);
            return;
        }

        // Only user-defined functions carry a declaration we can check against.
        if (callee.getKind() != SymbolKind.FUNCTION || callee.getDeclaration() == null) return;

        List<Parameter> parameters = callee.getParameters();
        List<Argument> arguments =
                callArguments.args == null ? List.of() : callArguments.args;

        checkArgumentCount(idTrailer, callee, parameters, arguments, context);
        checkArgumentTypes(idTrailer, callee, parameters, arguments, context);
    }

    private void checkArgumentCount(IDTrailer idTrailer,
                                    Symbol callee,
                                    List<Parameter> parameters,
                                    List<Argument> arguments,
                                    SemanticContext context) {
        int required = 0;
        for (Parameter parameter : parameters)
            if (!parameter.hasDefaultValue()) required++;

        int given = arguments.size();

        if (given >= required && given <= parameters.size()) return;

        String expected = required == parameters.size()
                ? String.valueOf(required)
                : "between " + required + " and " + parameters.size();

        context.error(
                CompilerError.Kind.ARGUMENT_COUNT,
                "Function '" + callee.getName() + "' expects " + expected
                        + " argument(s), but " + given + " were given",
                idTrailer.getLine(),
                null,
                callee.getName());
    }

    private void checkArgumentTypes(IDTrailer idTrailer,
                                    Symbol callee,
                                    List<Parameter> parameters,
                                    List<Argument> arguments,
                                    SemanticContext context) {
        for (int i = 0; i < arguments.size(); i++) {
            Argument argument = arguments.get(i);

            Parameter parameter;
            Condition value;

            if (argument.isAssigned()) {
                // f(name=value) — match the parameter by name
                String keyword = keywordName(argument.arg);
                parameter = keyword == null ? null : findParameter(parameters, keyword);
                value = argument.assign;
            } else {
                parameter = i < parameters.size() ? parameters.get(i) : null;
                value = argument.arg;
            }

            if (parameter == null) continue;

            SymbolType expected = SymbolTableBuilder.annotatedType(parameter);
            SymbolType actual = resolveType(value, context.symbolTable());

            if (!expected.isKnown() || !actual.isKnown()) continue;
            if (expected == actual) continue;
            if (expected.isNumeric() && actual.isNumeric()) continue;
            // None is accepted anywhere — Python's optional arguments are pervasive
            if (actual == SymbolType.NONE) continue;

            context.error(
                    CompilerError.Kind.TYPE_MISMATCH,
                    "Expected " + expected + ", got " + actual + " for parameter '"
                            + parameter.id.name + "' of '" + callee.getName() + "'",
                    argument.getLine(),
                    null,
                    parameter.id.name);
        }
    }

    private static String keywordName(Condition keyword) {
        if (keyword instanceof IDTrailer idt
                && (idt.trailers == null || idt.trailers.isEmpty()))
            return idt.id.name;
        if (keyword instanceof ID id) return id.name;
        return null;
    }

    private static Parameter findParameter(List<Parameter> parameters, String name) {
        for (Parameter parameter : parameters)
            if (parameter.id.name.equals(name)) return parameter;
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE RESOLUTION
    // ─────────────────────────────────────────────────────────────

    /**
     * Statically known type of an expression, or ANY when it is only known at run
     * time. Identifiers are answered from the bindings the symbol table builder
     * recorded, so a name resolves to the declaration it actually refers to.
     */
    public SymbolType resolveType(Condition expression, SymbolTable symbolTable) {
        if (expression == null)                         return SymbolType.ANY;
        if (expression instanceof IntegerAtom)          return SymbolType.INT;
        if (expression instanceof FloatAtom)            return SymbolType.FLOAT;
        if (expression instanceof StringAtom)           return SymbolType.STRING;
        if (expression instanceof BoolAtom)             return SymbolType.BOOLEAN;
        if (expression instanceof None)                 return SymbolType.NONE;
        if (expression instanceof python.models.atom_statement.List) return SymbolType.LIST;
        if (expression instanceof Dictionary)           return SymbolType.DICT;
        if (expression instanceof python.models.atom_statement.Set)  return SymbolType.SET;
        if (expression instanceof RelationalComparison) return SymbolType.BOOLEAN;
        if (expression instanceof CompoundCondition)    return SymbolType.BOOLEAN;

        if (expression instanceof ParenAtom paren)
            return resolveType(paren.inner, symbolTable);

        if (expression instanceof UnaryExpression unary) {
            SymbolType operand = resolveType(unary.expression, symbolTable);
            return operand.isNumeric() ? operand : SymbolType.ANY;
        }

        if (expression instanceof BinaryExpression binary) {
            return SymbolTableBuilder.binaryResultType(
                    resolveType(binary.left, symbolTable),
                    resolveType(binary.right, symbolTable),
                    binary.operation);
        }

        if (expression instanceof IDTrailer idTrailer) {
            // A call result or an attribute value is not statically known.
            if (idTrailer.trailers != null && !idTrailer.trailers.isEmpty())
                return SymbolType.ANY;

            Symbol symbol = symbolTable.getBinding(idTrailer.id);
            return symbol == null ? SymbolType.ANY : symbol.getType();
        }

        if (expression instanceof ID id) {
            Symbol symbol = symbolTable.getBinding(id);
            return symbol == null ? SymbolType.ANY : symbol.getType();
        }

        return SymbolType.ANY;
    }
}
