package python.semantic;

import errors.CompilerProblem;
import errors.CompilerStage;
import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.atom_statement.BoolAtom;
import python.models.atom_statement.Dictionary;
import python.models.atom_statement.FloatAtom;
import python.models.atom_statement.ID;
import python.models.atom_statement.IntegerAtom;
import python.models.atom_statement.None;
import python.models.atom_statement.ParenAtom;
import python.models.atom_statement.StringAtom;
import python.models.compound_statement.Body;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.compound_statement.ForStatement;
import python.models.compound_statement.IfStatement;
import python.models.compound_statement.WhileStatement;
import python.models.enums.Operation;
import python.models.expr_statement.BinaryExpression;
import python.models.expr_statement.CompoundCondition;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.expr_statement.RelationalComparison;
import python.models.expr_statement.UnaryExpression;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.ReturnStatement;
import python.models.small_statement.SmallStatement;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;
import python.symbol_table.SymbolTable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Third semantic pass: shallow, flow-insensitive, conservative type checks. */
public final class PythonTypeChecker {
    private static final Set<String> LIST_ATTRIBUTES = Set.of(
            "append", "clear", "copy", "count", "extend", "index",
            "insert", "pop", "remove", "reverse", "sort");
    private static final Set<String> DICT_ATTRIBUTES = Set.of(
            "clear", "copy", "fromkeys", "get", "items", "keys", "pop",
            "popitem", "setdefault", "update", "values");
    private static final Set<String> SET_ATTRIBUTES = Set.of(
            "add", "clear", "copy", "difference", "difference_update", "discard",
            "intersection", "intersection_update", "isdisjoint", "issubset",
            "issuperset", "pop", "remove", "symmetric_difference",
            "symmetric_difference_update", "union", "update");
    private static final Set<String> STRING_ATTRIBUTES = Set.of(
            "capitalize", "casefold", "center", "count", "encode", "endswith",
            "expandtabs", "find", "format", "format_map", "index", "isalnum",
            "isalpha", "isascii", "isdecimal", "isdigit", "isidentifier",
            "islower", "isnumeric", "isprintable", "isspace", "istitle",
            "isupper", "join", "ljust", "lower", "lstrip", "maketrans",
            "partition", "removeprefix", "removesuffix", "replace", "rfind",
            "rindex", "rjust", "rpartition", "rsplit", "rstrip", "split",
            "splitlines", "startswith", "strip", "swapcase", "title",
            "translate", "upper", "zfill");
    private static final Set<String> NUMBER_ATTRIBUTES = Set.of(
            "as_integer_ratio", "bit_count", "bit_length", "conjugate",
            "denominator", "from_bytes", "fromhex", "hex", "imag", "is_integer",
            "numerator", "real", "to_bytes");
    private static final Set<String> NUMBER_VALUE_ATTRIBUTES = Set.of(
            "denominator", "imag", "numerator", "real");

    private final String sourceFile;
    private final SymbolTable symbolTable;
    private final Map<ID, Symbol> bindings;
    private final Map<String, Symbol> builtins;
    private final List<CompilerProblem> diagnostics;
    private final Map<Symbol, PythonType> symbolTypes = new IdentityHashMap<>();
    private final Map<ASTNode, PythonType> inferredTypes = new IdentityHashMap<>();
    private final Set<Symbol> assignedSymbols =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<ASTNode> diagnosedNodes =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private final Map<AugAssignStatement, BinaryOperands> augmentedOperands =
            new IdentityHashMap<>();

    public PythonTypeChecker(
            String sourceFile,
            SymbolTable symbolTable,
            Map<ID, Symbol> bindings,
            Map<String, Symbol> builtins,
            List<CompilerProblem> diagnostics
    ) {
        this.sourceFile = sourceFile;
        this.symbolTable = symbolTable;
        this.bindings = bindings;
        this.builtins = builtins;
        this.diagnostics = diagnostics;
    }

    public Map<ASTNode, PythonType> check(Program program) {
        seedDeclarationTypes();
        collectFacts(program.statements);
        checkStatements(program.statements);
        return inferredTypes;
    }

    private void seedDeclarationTypes() {
        for (var scope : symbolTable.getAllScopes()) {
            for (Symbol symbol : scope.getSymbols()) {
                if (symbol.getKind() == SymbolKind.FUNCTION) {
                    seed(symbol, PythonType.CALLABLE);
                } else if (symbol.getKind() == SymbolKind.PARAMETER
                        || symbol.getKind() == SymbolKind.IMPORT) {
                    seed(symbol, PythonType.UNKNOWN);
                }
            }
        }
        for (Map.Entry<String, Symbol> entry : builtins.entrySet()) {
            seed(entry.getValue(), PythonBuiltinCatalog.typeOf(entry.getKey()));
        }
    }

    private void seed(Symbol symbol, PythonType type) {
        symbolTypes.put(symbol, type);
        assignedSymbols.add(symbol);
    }

    private void collectFacts(List<Statement> statements) {
        for (Statement statement : statements) {
            if (statement instanceof SimpleStatement simple) {
                for (SmallStatement small : simple.smallStatementList) collectSmallFacts(small);
            } else if (statement instanceof DecoratorStatement decorated) {
                assign(bindings.get(decorated.function.id), PythonType.CALLABLE);
                collectFacts(decorated.function.body.statements);
            } else if (statement instanceof IfStatement conditional) {
                for (Body body : conditional.bodies) collectFacts(body.statements);
                if (conditional.last != null) collectFacts(conditional.last.statements);
            } else if (statement instanceof ForStatement loop) {
                for (ID iterator : loop.iterators) assign(bindings.get(iterator), PythonType.UNKNOWN);
                collectFacts(loop.body.statements);
                if (loop.last != null) collectFacts(loop.last.statements);
            } else if (statement instanceof WhileStatement loop) {
                collectFacts(loop.body.statements);
                if (loop.last != null) collectFacts(loop.last.statements);
            }
        }
    }

    private void collectSmallFacts(SmallStatement statement) {
        if (statement instanceof ExpressionStatement expression && expression.isAssignment()) {
            List<Condition> values = expression.getValues();
            for (int index = 0; index < expression.getTargets().size(); index++) {
                PythonType valueType = values.size() == expression.getTargets().size()
                        ? inferStatic(values.get(index))
                        : PythonType.UNKNOWN;
                assignTarget(expression.getTargets().get(index), valueType);
            }
        } else if (statement instanceof AugAssignStatement augmented) {
            Symbol symbol = bindings.get(augmented.id);
            PythonType left = typeOfSymbol(symbol);
            PythonType right = inferStatic(augmented.expression);
            augmentedOperands.put(augmented, new BinaryOperands(left, right));
            assign(symbol, binaryResult(augmented.operation, left, right));
        } else if (statement instanceof ImportStatement imported) {
            collectImportFact(imported);
        }
    }

    private void collectImportFact(ImportStatement statement) {
        if (statement instanceof SimpleImportStatement simple
                && simple.dottedName != null && !simple.dottedName.isEmpty()) {
            assign(bindings.get(simple.getBoundName()), PythonType.UNKNOWN);
        } else if (statement instanceof FromImportStatement from && from.targets != null) {
            for (ID target : from.getBoundNames()) {
                assign(bindings.get(target), PythonType.UNKNOWN);
            }
        }
    }

    private void assignTarget(Condition target, PythonType type) {
        if (target instanceof IDTrailer identifier
                && (identifier.trailers == null || identifier.trailers.isEmpty())) {
            assign(bindings.get(identifier.id), type);
        } else if (target instanceof ID identifier) {
            assign(bindings.get(identifier), type);
        } else if (target instanceof ParenAtom parenthesized) {
            assignTarget(parenthesized.inner, type);
        } else if (target instanceof python.models.atom_statement.List list) {
            for (var item : list.content) assignTarget(item, PythonType.UNKNOWN);
        } else if (target instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) assignTarget(item, PythonType.UNKNOWN);
        }
    }

    private void assign(Symbol symbol, PythonType type) {
        if (symbol == null) return;
        if (!assignedSymbols.add(symbol)) {
            symbolTypes.put(symbol, merge(symbolTypes.get(symbol), type));
        } else {
            symbolTypes.put(symbol, type);
        }
    }

    private PythonType merge(PythonType first, PythonType second) {
        if (first == second) return first;
        if (first != null && first.isNumeric() && second.isNumeric()) return PythonType.NUMBER;
        return PythonType.UNKNOWN;
    }

    private void checkStatements(List<Statement> statements) {
        for (Statement statement : statements) checkStatement(statement);
    }

    private void checkStatement(Statement statement) {
        if (statement instanceof SimpleStatement simple) {
            for (SmallStatement small : simple.smallStatementList) checkSmallStatement(small);
        } else if (statement instanceof DecoratorStatement decorated) {
            checkDecoratedFunction(decorated);
        } else if (statement instanceof IfStatement conditional) {
            for (int index = 0; index < conditional.conditions.size(); index++) {
                typeOf(conditional.conditions.get(index));
                checkStatements(conditional.bodies.get(index).statements);
            }
            if (conditional.last != null) checkStatements(conditional.last.statements);
        } else if (statement instanceof ForStatement loop) {
            PythonType iterable = typeOf(loop.iterable);
            if (iterable != PythonType.UNKNOWN && !iterable.isIterable()) {
                typeError(loop.iterable, "Value of type " + iterable + " is not iterable");
            }
            checkStatements(loop.body.statements);
            if (loop.last != null) checkStatements(loop.last.statements);
        } else if (statement instanceof WhileStatement loop) {
            typeOf(loop.condition);
            checkStatements(loop.body.statements);
            if (loop.last != null) checkStatements(loop.last.statements);
        }
    }

    private void checkSmallStatement(SmallStatement statement) {
        if (statement instanceof ExpressionStatement expression) {
            if (expression.isAssignment()) {
                for (Condition value : expression.getValues()) typeOf(value);
                for (Condition target : expression.getTargets()) checkAssignmentTarget(target);
            } else {
                for (Condition value : expression.getExpressions()) typeOf(value);
            }
        } else if (statement instanceof AugAssignStatement augmented) {
            BinaryOperands operands = augmentedOperands.get(augmented);
            PythonType currentLeft = typeOfIdentifier(augmented.id);
            PythonType currentRight = typeOf(augmented.expression);
            PythonType left = operands == null
                    ? currentLeft
                    : operands.left();
            PythonType right = operands == null
                    ? currentRight
                    : operands.right();
            checkBinary(augmented.operation, left, right, augmented, augmented.getLine());
        } else if (statement instanceof ReturnStatement returned) {
            for (Condition value : returned.conditions) typeOf(value);
        } else if (statement instanceof ImportStatement) {
            // Import bindings remain UNKNOWN; no runtime import is performed here.
        }
    }

    private void checkDecoratedFunction(DecoratorStatement decorated) {
        if (decorated.decorators != null) {
            for (Decorator decorator : decorated.decorators) checkDecorator(decorator);
        }
        FunctionDef function = decorated.function;
        for (Parameter parameter : function.parameters) {
            if (parameter.type != null) typeOf(parameter.type);
            if (parameter.defaultValue != null) typeOf(parameter.defaultValue);
        }
        if (function.returnType != null) typeOf(function.returnType);
        checkStatements(function.body.statements);
    }

    private void checkDecorator(Decorator decorator) {
        if (decorator.dottedName == null || decorator.dottedName.isEmpty()) return;
        PythonType current = typeOfIdentifier(decorator.dottedName.getFirst());
        for (int index = 1; index < decorator.dottedName.size(); index++) {
            current = attributeType(current, decorator.dottedName.get(index).name, decorator);
        }
        if (decorator.arguments != null) {
            for (Argument argument : decorator.arguments) checkArgument(argument);
        }
        if (current != PythonType.UNKNOWN && current != PythonType.CALLABLE) {
            typeError(decorator, "Decorator target of type " + current + " is not callable");
        }
    }

    private void checkAssignmentTarget(Condition target) {
        if (target instanceof IDTrailer identifier
                && identifier.trailers != null && !identifier.trailers.isEmpty()) {
            typeOf(identifier);
        } else if (target instanceof ParenAtom parenthesized) {
            checkAssignmentTarget(parenthesized.inner);
        } else if (target instanceof python.models.atom_statement.List list) {
            for (var item : list.content) checkAssignmentTarget(item);
        } else if (target instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) checkAssignmentTarget(item);
        }
    }

    private PythonType typeOf(Condition condition) {
        if (condition == null) return PythonType.UNKNOWN;
        PythonType cached = inferredTypes.get(condition);
        if (cached != null) return cached;

        PythonType result;
        if (condition instanceof IDTrailer identifier) {
            result = typeOfIdentifierExpression(identifier);
        } else if (condition instanceof ID identifier) {
            result = typeOfIdentifier(identifier);
        } else if (condition instanceof BoolAtom) {
            result = PythonType.BOOLEAN;
        } else if (condition instanceof IntegerAtom) {
            result = PythonType.INTEGER;
        } else if (condition instanceof FloatAtom) {
            result = PythonType.FLOAT;
        } else if (condition instanceof StringAtom) {
            result = PythonType.STRING;
        } else if (condition instanceof None) {
            result = PythonType.NONE;
        } else if (condition instanceof python.models.atom_statement.List list) {
            for (var item : list.content) typeOf(item);
            result = PythonType.LIST;
        } else if (condition instanceof Dictionary dictionary) {
            for (var key : dictionary.keys) typeOf(key);
            for (var value : dictionary.values) typeOf(value);
            result = PythonType.DICT;
        } else if (condition instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) typeOf(item);
            result = PythonType.SET;
        } else if (condition instanceof ParenAtom parenthesized) {
            result = typeOf(parenthesized.inner);
        } else if (condition instanceof UnaryExpression unary) {
            result = checkUnary(unary);
        } else if (condition instanceof BinaryExpression binary) {
            PythonType left = typeOf(binary.left);
            PythonType right = typeOf(binary.right);
            checkBinary(binary.operation, left, right, binary, binary.getLine());
            result = binaryResult(binary.operation, left, right);
        } else if (condition instanceof RelationalComparison comparison) {
            result = checkComparison(comparison);
        } else if (condition instanceof CompoundCondition compound) {
            PythonType first = typeOf(compound.first);
            PythonType second = typeOf(compound.second);
            result = compound.operation == Operation.NOT
                    ? PythonType.BOOLEAN
                    : merge(first, second);
        } else {
            result = PythonType.UNKNOWN;
        }

        inferredTypes.put(condition, result);
        return result;
    }

    private PythonType typeOfIdentifier(ID identifier) {
        PythonType type = typeOfSymbol(bindings.get(identifier));
        inferredTypes.put(identifier, type);
        return type;
    }

    private PythonType typeOfSymbol(Symbol symbol) {
        return symbol == null ? PythonType.UNKNOWN
                : symbolTypes.getOrDefault(symbol, PythonType.UNKNOWN);
    }

    private PythonType typeOfIdentifierExpression(IDTrailer expression) {
        PythonType current = typeOfIdentifier(expression.id);
        if (expression.trailers == null) return current;

        for (int index = 0; index < expression.trailers.size(); index++) {
            Trailer trailer = expression.trailers.get(index);
            if (trailer.isDotIdTrailer() && trailer.id != null) {
                current = attributeType(current, trailer.id.name, trailer);
            }
            if (trailer.arguments instanceof CallArguments calls) {
                for (Argument argument : calls.args) checkArgument(argument);
                if (current != PythonType.UNKNOWN && current != PythonType.CALLABLE) {
                    typeError(trailer, "Value of type " + current + " is not callable");
                    current = PythonType.UNKNOWN;
                } else if (current == PythonType.CALLABLE
                        && index == 0 && "float".equals(expression.id.name)
                        && !trailer.isDotIdTrailer()) {
                    current = PythonType.FLOAT;
                } else {
                    current = PythonType.UNKNOWN;
                }
            } else if (trailer.arguments instanceof SubscriptArguments subscripts) {
                for (Condition subscript : subscripts.conditions) typeOf(subscript);
                if (current != PythonType.UNKNOWN && !current.isIndexable()) {
                    typeError(trailer,
                            "Value of type " + current + " does not support index access");
                    current = PythonType.UNKNOWN;
                } else if (current == PythonType.STRING) {
                    current = PythonType.STRING;
                } else {
                    current = PythonType.UNKNOWN;
                }
            }
            inferredTypes.put(trailer, current);
        }
        return current;
    }

    private void checkArgument(Argument argument) {
        typeOf(argument.isAssigned() ? argument.assign : argument.arg);
    }

    private PythonType attributeType(PythonType owner, String attribute, ASTNode node) {
        if (owner == PythonType.UNKNOWN || owner == PythonType.CALLABLE) return PythonType.UNKNOWN;
        if (attribute.startsWith("__")) return PythonType.UNKNOWN;
        Set<String> allowed = switch (owner) {
            case LIST -> LIST_ATTRIBUTES;
            case DICT -> DICT_ATTRIBUTES;
            case SET -> SET_ATTRIBUTES;
            case STRING -> STRING_ATTRIBUTES;
            case INTEGER, FLOAT, NUMBER, BOOLEAN -> NUMBER_ATTRIBUTES;
            default -> Set.of();
        };
        if (allowed.contains(attribute)) {
            if ((owner == PythonType.INTEGER || owner == PythonType.FLOAT
                    || owner == PythonType.NUMBER || owner == PythonType.BOOLEAN)
                    && NUMBER_VALUE_ATTRIBUTES.contains(attribute)) {
                return PythonType.NUMBER;
            }
            return PythonType.CALLABLE;
        }
        typeError(node, "Type " + owner + " has no attribute '" + attribute + "'");
        return PythonType.UNKNOWN;
    }

    private PythonType checkUnary(UnaryExpression unary) {
        PythonType operand = typeOf(unary.expression);
        if (operand == PythonType.UNKNOWN) return PythonType.UNKNOWN;
        if (unary.operation == Operation.ADD || unary.operation == Operation.SUB) {
            if (!operand.isNumeric()) {
                typeError(unary, "Unary operator cannot be applied to " + operand);
                return PythonType.UNKNOWN;
            }
            return operand == PythonType.BOOLEAN ? PythonType.INTEGER : operand;
        }
        if (unary.operation == Operation.NOT_OP) {
            if (operand == PythonType.NUMBER) return PythonType.UNKNOWN;
            if (!operand.isIntegral()) {
                typeError(unary, "Bitwise not cannot be applied to " + operand);
                return PythonType.UNKNOWN;
            }
            return PythonType.INTEGER;
        }
        return PythonType.UNKNOWN;
    }

    private PythonType checkComparison(RelationalComparison comparison) {
        PythonType left = typeOf(comparison.left);
        PythonType right = typeOf(comparison.right);
        if (comparison.operation == Operation.IN || comparison.operation == Operation.NOTIN) {
            if (right != PythonType.UNKNOWN && !right.isIterable()) {
                typeError(comparison.right,
                        "Value of type " + right + " is not usable with membership testing");
            }
        } else if (comparison.operation != Operation.EQUALS
                && comparison.operation != Operation.NOT_EQ
                && comparison.operation != Operation.IS
                && comparison.operation != Operation.ISNOT
                && left != PythonType.UNKNOWN && right != PythonType.UNKNOWN
                && !compatibleOrdering(left, right)) {
            mismatch(comparison, comparison.getLine(), comparison.operation, left, right);
        }
        return PythonType.BOOLEAN;
    }

    private void checkBinary(
            Operation operation, PythonType left, PythonType right, ASTNode node, int line) {
        if (left == PythonType.UNKNOWN || right == PythonType.UNKNOWN) return;
        if (binaryResult(operation, left, right) == PythonType.UNKNOWN
                && !hasPossibleBinaryResult(operation, left, right)) {
            mismatch(node, line, operation, left, right);
        }
    }

    private boolean hasPossibleBinaryResult(
            Operation operation, PythonType left, PythonType right) {
        for (PythonType possibleLeft : concretePossibilities(left)) {
            for (PythonType possibleRight : concretePossibilities(right)) {
                if (binaryResult(operation, possibleLeft, possibleRight)
                        != PythonType.UNKNOWN) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<PythonType> concretePossibilities(PythonType type) {
        return type == PythonType.NUMBER
                ? List.of(PythonType.INTEGER, PythonType.FLOAT)
                : List.of(type);
    }

    private PythonType binaryResult(Operation operation, PythonType left, PythonType right) {
        if (left == PythonType.UNKNOWN || right == PythonType.UNKNOWN) return PythonType.UNKNOWN;
        return switch (operation) {
            case ADD -> {
                if (left.isNumeric() && right.isNumeric()) yield numericResult(left, right);
                if (left == right && (left == PythonType.STRING || left == PythonType.LIST)) yield left;
                yield PythonType.UNKNOWN;
            }
            case SUB -> {
                if (left.isNumeric() && right.isNumeric()) yield numericResult(left, right);
                if (left == PythonType.SET && right == PythonType.SET) yield PythonType.SET;
                yield PythonType.UNKNOWN;
            }
            case DIV -> left.isNumeric() && right.isNumeric()
                    ? PythonType.FLOAT : PythonType.UNKNOWN;
            case IDIV, POWER -> left.isNumeric() && right.isNumeric()
                    ? numericResult(left, right) : PythonType.UNKNOWN;
            case MOD -> {
                if (left.isNumeric() && right.isNumeric()) yield numericResult(left, right);
                if (left == PythonType.STRING) yield PythonType.STRING;
                yield PythonType.UNKNOWN;
            }
            case MULT -> {
                if (left.isNumeric() && right.isNumeric()) yield numericResult(left, right);
                if ((left == PythonType.STRING || left == PythonType.LIST)
                        && right.isIntegral()) yield left;
                if (left.isIntegral()
                        && (right == PythonType.STRING || right == PythonType.LIST)) yield right;
                yield PythonType.UNKNOWN;
            }
            case AND, OR, XOR -> {
                if (left.isIntegral() && right.isIntegral()) yield integralResult(left, right);
                if (left == PythonType.SET && right == PythonType.SET) yield PythonType.SET;
                yield PythonType.UNKNOWN;
            }
            case LSHIFT, RSHIFT -> left.isIntegral() && right.isIntegral()
                    ? PythonType.INTEGER : PythonType.UNKNOWN;
            default -> PythonType.UNKNOWN;
        };
    }

    private PythonType numericResult(PythonType left, PythonType right) {
        if (left == PythonType.FLOAT || right == PythonType.FLOAT) return PythonType.FLOAT;
        if (left.isIntegral() && right.isIntegral()) return PythonType.INTEGER;
        return PythonType.NUMBER;
    }

    private PythonType integralResult(PythonType left, PythonType right) {
        return left == PythonType.BOOLEAN && right == PythonType.BOOLEAN
                ? PythonType.BOOLEAN : PythonType.INTEGER;
    }

    private boolean compatibleOrdering(PythonType left, PythonType right) {
        return left.isNumeric() && right.isNumeric()
                || left == PythonType.STRING && right == PythonType.STRING
                || left == PythonType.LIST && right == PythonType.LIST
                || left == PythonType.SET && right == PythonType.SET;
    }

    private PythonType inferStatic(Condition condition) {
        if (condition == null) return PythonType.UNKNOWN;
        if (condition instanceof BoolAtom) return PythonType.BOOLEAN;
        if (condition instanceof IntegerAtom) return PythonType.INTEGER;
        if (condition instanceof FloatAtom) return PythonType.FLOAT;
        if (condition instanceof StringAtom) return PythonType.STRING;
        if (condition instanceof None) return PythonType.NONE;
        if (condition instanceof python.models.atom_statement.List) return PythonType.LIST;
        if (condition instanceof Dictionary) return PythonType.DICT;
        if (condition instanceof python.models.atom_statement.Set) return PythonType.SET;
        if (condition instanceof ParenAtom parenthesized) return inferStatic(parenthesized.inner);
        if (condition instanceof ID identifier) return typeOfSymbol(bindings.get(identifier));
        if (condition instanceof IDTrailer identifier
                && (identifier.trailers == null || identifier.trailers.isEmpty())) {
            return typeOfSymbol(bindings.get(identifier.id));
        }
        if (condition instanceof UnaryExpression unary) {
            PythonType operand = inferStatic(unary.expression);
            return unary.operation == Operation.NOT ? PythonType.BOOLEAN : operand;
        }
        if (condition instanceof BinaryExpression binary) {
            return binaryResult(binary.operation,
                    inferStatic(binary.left), inferStatic(binary.right));
        }
        if (condition instanceof RelationalComparison || condition instanceof CompoundCondition) {
            return PythonType.BOOLEAN;
        }
        return PythonType.UNKNOWN;
    }

    private void typeError(ASTNode node, String message) {
        if (diagnosedNodes.add(node)) {
            diagnostics.add(new CompilerProblem(CompilerStage.SEMANTIC_ANALYSIS,
                    "TYPE_ERROR", sourceFile, node.getLine(), message));
        }
    }

    private void mismatch(
            ASTNode node, int line, Operation operation, PythonType left, PythonType right) {
        if (diagnosedNodes.add(node)) {
            diagnostics.add(new CompilerProblem(CompilerStage.SEMANTIC_ANALYSIS,
                    "TYPE_MISMATCH", sourceFile, line,
                    "Operator " + operation + " cannot be applied to " + left + " and " + right));
        }
    }

    private record BinaryOperands(PythonType left, PythonType right) { }
}
