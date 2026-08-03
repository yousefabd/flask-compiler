package python.semantic.semantic_rules;

import python.models.ASTNode;
import python.models.atom_statement.BoolAtom;
import python.models.atom_statement.Dictionary;
import python.models.atom_statement.FloatAtom;
import python.models.atom_statement.ID;
import python.models.atom_statement.IntegerAtom;
import python.models.atom_statement.None;
import python.models.atom_statement.ParenAtom;
import python.models.atom_statement.Set;
import python.models.atom_statement.StringAtom;
import python.models.enums.Operation;
import python.models.expr_statement.BinaryExpression;
import python.models.expr_statement.CompoundCondition;
import python.models.expr_statement.Condition;
import python.models.expr_statement.IDTrailer;
import python.models.expr_statement.RelationalComparison;
import python.models.expr_statement.UnaryExpression;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.semantic.Binding;
import python.semantic.BindingKind;
import python.semantic.PythonBuiltins;
import python.semantic.PythonType;
import python.semantic.ResolutionResult;
import python.symbol_table.CompilerError;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Reports the two type-level Python errors, mirroring
 * {@code jinja2.symbol_table.semantic_rules.TypeCheckerRule}.
 *
 * <ul>
 *   <li>{@code TYPE_ERROR} — the operand types are statically known and the
 *       operation is provably invalid: {@code 5 + "hello"}, indexing an
 *       {@code int}, calling something that is not callable.</li>
 *   <li>{@code TYPE_MISMATCH} — a value contradicts an <em>explicit</em> type
 *       expectation. The only such expectation in this grammar is a function
 *       parameter annotation: {@code def set_age(age: int)} called with
 *       {@code "twenty"}.</li>
 * </ul>
 *
 * <p>A type that cannot be proven is {@link PythonType#ANY} and every check
 * involving it is skipped. Ordinary Python rebinding
 * ({@code x = 1} then {@code x = "text"}) is legal and never reported: a name
 * with more than one assignment is simply typed ANY, so the first assignment
 * never fixes the variable's type.</p>
 */
public final class TypeCheckerRule implements ISemanticRule {

    private static final java.util.Set<PythonType> SUBSCRIPTABLE = java.util.Set.of(
            PythonType.LIST, PythonType.DICT, PythonType.STRING, PythonType.ANY
    );

    private ResolutionResult resolution;
    private List<CompilerError> errors;

    /** Guards against a self-referential binding while inferring its type. */
    private final java.util.Set<Binding> inferring =
            Collections.newSetFromMap(new IdentityHashMap<>());

    @Override
    public void validate(SemanticContext context) {
        this.resolution = context.resolution();
        this.errors = context.errors();
        walk(context.root());
    }

    // ─────────────────────────────────────────────────────────────
    // TRAVERSAL
    // ─────────────────────────────────────────────────────────────

    /**
     * Expressions know how to check their own subtree, so the generic walk
     * stops as soon as it reaches one.
     */
    private void walk(ASTNode node) {
        if (node == null) return;
        if (node instanceof Condition condition) {
            checkExpression(condition);
            return;
        }
        for (ASTNode child : node.getChildren()) walk(child);
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSION CHECKING — returns the resulting type as a side benefit
    // ─────────────────────────────────────────────────────────────

    private PythonType checkExpression(Condition expression) {
        if (expression == null) return PythonType.ANY;

        if (expression instanceof CompoundCondition compound) {
            checkExpression(compound.first);
            checkExpression(compound.second);
            return PythonType.BOOL;
        }
        if (expression instanceof RelationalComparison comparison)
            return checkComparison(comparison);
        if (expression instanceof BinaryExpression binary)
            return checkBinary(binary);
        if (expression instanceof UnaryExpression unary)
            return checkUnary(unary);
        if (expression instanceof ParenAtom paren)
            return checkExpression(paren.inner);
        if (expression instanceof IDTrailer idTrailer)
            return checkIdTrailer(idTrailer);

        if (expression instanceof python.models.atom_statement.List list) {
            if (list.content != null) list.content.forEach(this::checkExpression);
            return PythonType.LIST;
        }
        if (expression instanceof Set set) {
            if (set.content != null) set.content.forEach(this::checkExpression);
            return PythonType.SET;
        }
        if (expression instanceof Dictionary dictionary) {
            if (dictionary.keys != null) dictionary.keys.forEach(this::checkExpression);
            if (dictionary.values != null) dictionary.values.forEach(this::checkExpression);
            return PythonType.DICT;
        }

        return literalType(expression);
    }

    private static PythonType literalType(Condition expression) {
        if (expression instanceof IntegerAtom) return PythonType.INT;
        if (expression instanceof FloatAtom) return PythonType.FLOAT;
        if (expression instanceof StringAtom) return PythonType.STRING;
        if (expression instanceof BoolAtom) return PythonType.BOOL;
        if (expression instanceof None) return PythonType.NONE;
        return PythonType.ANY;
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_ERROR — binary, unary, comparison, index, call
    // ─────────────────────────────────────────────────────────────

    private PythonType checkBinary(BinaryExpression binary) {
        PythonType left = checkExpression(binary.left);
        PythonType right = checkExpression(binary.right);

        if (!left.isKnown() || !right.isKnown()) return PythonType.ANY;

        if (!isBinaryValid(binary.operation, left, right)) {
            error(CompilerError.Kind.TYPE_ERROR,
                    "Unsupported operand types for " + symbolOf(binary.operation)
                            + ": '" + left.display() + "' and '" + right.display() + "'",
                    binary.getLine());
            return PythonType.ANY;
        }

        return binaryResultType(binary.operation, left, right);
    }

    private static boolean isBinaryValid(Operation operation, PythonType left, PythonType right) {
        return switch (operation) {
            case ADD -> (left.isNumeric() && right.isNumeric())
                    || (left == PythonType.STRING && right == PythonType.STRING)
                    || (left == PythonType.LIST && right == PythonType.LIST);
            case SUB -> (left.isNumeric() && right.isNumeric())
                    || (left == PythonType.SET && right == PythonType.SET);
            case MULT -> (left.isNumeric() && right.isNumeric())
                    || isRepetition(left, right)
                    || isRepetition(right, left);
            case DIV, IDIV, POWER -> left.isNumeric() && right.isNumeric();
            // '%s' % value is string formatting, not arithmetic.
            case MOD -> (left.isNumeric() && right.isNumeric()) || left == PythonType.STRING;
            case LSHIFT, RSHIFT -> isIntegral(left) && isIntegral(right);
            case AND, OR, XOR -> (isIntegral(left) && isIntegral(right))
                    || (left == PythonType.SET && right == PythonType.SET);
            default -> true;
        };
    }

    /** {@code "ab" * 3} and {@code [1] * 3} repeat a sequence an integral number of times. */
    private static boolean isRepetition(PythonType sequence, PythonType count) {
        return (sequence == PythonType.STRING || sequence == PythonType.LIST)
                && isIntegral(count);
    }

    private static boolean isIntegral(PythonType type) {
        return type == PythonType.INT || type == PythonType.BOOL;
    }

    private static PythonType binaryResultType(Operation operation, PythonType left, PythonType right) {
        return switch (operation) {
            case ADD -> {
                if (left == PythonType.STRING && right == PythonType.STRING) yield PythonType.STRING;
                if (left == PythonType.LIST && right == PythonType.LIST) yield PythonType.LIST;
                yield numericResult(left, right);
            }
            case SUB -> left == PythonType.SET ? PythonType.SET : numericResult(left, right);
            case MULT -> {
                if (left == PythonType.STRING || right == PythonType.STRING) yield PythonType.STRING;
                if (left == PythonType.LIST || right == PythonType.LIST) yield PythonType.LIST;
                yield numericResult(left, right);
            }
            case DIV -> PythonType.FLOAT;
            case IDIV, POWER -> numericResult(left, right);
            case MOD -> left == PythonType.STRING ? PythonType.STRING : numericResult(left, right);
            default -> PythonType.ANY;
        };
    }

    private static PythonType numericResult(PythonType left, PythonType right) {
        if (left == PythonType.FLOAT || right == PythonType.FLOAT) return PythonType.FLOAT;
        if (left.isNumeric() && right.isNumeric()) return PythonType.INT;
        return PythonType.ANY;
    }

    private PythonType checkUnary(UnaryExpression unary) {
        PythonType operand = checkExpression(unary.expression);
        if (!operand.isKnown()) return PythonType.ANY;

        if (unary.operation == Operation.ADD || unary.operation == Operation.SUB) {
            if (!operand.isNumeric()) {
                error(CompilerError.Kind.TYPE_ERROR,
                        "Unary '" + (unary.operation == Operation.ADD ? "+" : "-")
                                + "' is not supported for '" + operand.display() + "'",
                        unary.getLine());
                return PythonType.ANY;
            }
            return operand == PythonType.FLOAT ? PythonType.FLOAT : PythonType.INT;
        }

        if (unary.operation == Operation.NOT_OP) {
            if (!isIntegral(operand)) {
                error(CompilerError.Kind.TYPE_ERROR,
                        "Unary '~' is not supported for '" + operand.display() + "'",
                        unary.getLine());
                return PythonType.ANY;
            }
            return PythonType.INT;
        }

        return PythonType.ANY;
    }

    private PythonType checkComparison(RelationalComparison comparison) {
        PythonType left = checkExpression(comparison.left);
        PythonType right = checkExpression(comparison.right);

        if (!left.isKnown() || !right.isKnown()) return PythonType.BOOL;

        switch (comparison.operation) {
            case LESS_THAN, GREATER_THAN, LT_EQ, GT_EQ -> {
                boolean ordered = (left.isNumeric() && right.isNumeric())
                        || (left == right && (left == PythonType.STRING
                                || left == PythonType.LIST
                                || left == PythonType.SET));
                if (!ordered)
                    error(CompilerError.Kind.TYPE_ERROR,
                            "'" + symbolOf(comparison.operation) + "' is not supported between '"
                                    + left.display() + "' and '" + right.display() + "'",
                            comparison.getLine());
            }
            case IN, NOTIN -> {
                boolean container = right == PythonType.LIST || right == PythonType.DICT
                        || right == PythonType.SET || right == PythonType.STRING;
                if (!container)
                    error(CompilerError.Kind.TYPE_ERROR,
                            "'" + right.display() + "' is not iterable, so 'in' cannot be used on it",
                            comparison.getLine());
            }
            default -> { /* ==, !=, is, is not are valid for any pair of types */ }
        }

        return PythonType.BOOL;
    }

    /** Walks {@code name.attr[i](args)} left to right, checking each step. */
    private PythonType checkIdTrailer(IDTrailer node) {
        PythonType current = baseType(node);

        if (node.trailers == null || node.trailers.isEmpty()) return current;

        for (int i = 0; i < node.trailers.size(); i++) {
            Trailer trailer = node.trailers.get(i);
            if (trailer == null) continue;

            if (trailer.arguments instanceof CallArguments call) {
                checkArguments(call);

                if (trailer.isDotIdTrailer()) {
                    current = PythonType.ANY;      // obj.method(...) — return type unknown
                    continue;
                }

                if (current.isKnown() && current != PythonType.CALLABLE) {
                    error(CompilerError.Kind.TYPE_ERROR,
                            "'" + current.display() + "' object is not callable",
                            trailer.getLine());
                    current = PythonType.ANY;
                    continue;
                }

                if (i == 0 && node.id != null) {
                    checkAnnotatedCall(node.id.name, call);
                    current = PythonBuiltins.contains(node.id.name)
                            ? PythonBuiltins.callResult(node.id.name)
                            : PythonType.ANY;
                } else {
                    current = PythonType.ANY;
                }
            }
            else if (trailer.arguments instanceof SubscriptArguments subscript) {
                if (subscript.conditions != null) subscript.conditions.forEach(this::checkExpression);

                if (current.isKnown() && !SUBSCRIPTABLE.contains(current)) {
                    error(CompilerError.Kind.TYPE_ERROR,
                            "'" + current.display() + "' object is not subscriptable",
                            trailer.getLine());
                }
                current = PythonType.ANY;          // element type is not tracked
            }
            else {
                current = PythonType.ANY;          // plain attribute access
            }
        }

        return current;
    }

    private PythonType baseType(IDTrailer node) {
        if (node.id == null) return PythonType.ANY;

        Binding binding = resolution.getBinding(node.id);
        if (binding != null) return typeOf(binding);

        // Builtins are not bindings, but they are callable.
        if (PythonBuiltins.contains(node.id.name)) {
            PythonType constant = constantType(node.id.name);
            return constant != null ? constant : PythonType.CALLABLE;
        }
        return PythonType.ANY;
    }

    private static PythonType constantType(String name) {
        return switch (name) {
            case "True", "False" -> PythonType.BOOL;
            case "None" -> PythonType.NONE;
            default -> null;
        };
    }

    private void checkArguments(CallArguments call) {
        if (call.args == null) return;
        for (Argument argument : call.args) {
            if (argument == null) continue;
            // For name=value the name is a parameter, not an expression here.
            checkExpression(argument.isAssigned() ? argument.assign : argument.arg);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE_MISMATCH — annotated parameters only
    // ─────────────────────────────────────────────────────────────

    private void checkAnnotatedCall(String functionName, CallArguments call) {
        FunctionDef function = resolution.getFunction(functionName);
        if (function == null || function.parameters == null || call.args == null) return;

        List<Parameter> parameters = function.parameters;
        int positional = 0;

        for (Argument argument : call.args) {
            if (argument == null) continue;

            if (!argument.isAssigned()) {
                if (positional < parameters.size())
                    checkArgumentAgainst(parameters.get(positional), argument.arg, functionName);
                positional++;               // argument-count errors are out of scope
                continue;
            }

            String keyword = python.semantic.NameResolver.plainTargetName(argument.arg);
            if (keyword == null) continue;
            for (Parameter parameter : parameters) {
                if (parameter.id != null && keyword.equals(parameter.id.name)) {
                    checkArgumentAgainst(parameter, argument.assign, functionName);
                    break;
                }
            }
        }
    }

    private void checkArgumentAgainst(Parameter parameter, Condition value, String functionName) {
        if (parameter == null || parameter.id == null || value == null) return;

        PythonType expected = python.semantic.NameResolver.annotationType(parameter.type);
        if (expected == null) return;                 // no explicit expectation — nothing to check

        PythonType actual = typeOfWithoutChecking(value);
        if (!actual.isKnown()) return;                // unknown at compile time — skip

        if (!isAssignable(actual, expected)) {
            error(CompilerError.Kind.TYPE_MISMATCH,
                    "Parameter '" + parameter.id.name + "' of '" + functionName
                            + "' expects '" + expected.display()
                            + "' but received '" + actual.display() + "'",
                    value.getLine());
        }
    }

    /** Python's numeric tower: bool is an int, and an int is accepted as a float. */
    private static boolean isAssignable(PythonType actual, PythonType expected) {
        if (actual == expected) return true;
        if (expected == PythonType.FLOAT && (actual == PythonType.INT || actual == PythonType.BOOL))
            return true;
        return expected == PythonType.INT && actual == PythonType.BOOL;
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE INFERENCE
    // ─────────────────────────────────────────────────────────────

    private PythonType typeOf(Binding binding) {
        if (binding.getAnnotatedType() != null) return binding.getAnnotatedType();

        return switch (binding.getKind()) {
            case FUNCTION -> PythonType.CALLABLE;
            case PARAMETER, IMPORT, LOOP_VARIABLE -> PythonType.ANY;
            case VARIABLE -> inferVariableType(binding);
        };
    }

    /**
     * Only a name assigned exactly once has a provable type. Python allows
     * rebinding to a different type, so anything reassigned stays ANY rather
     * than letting the first assignment fix the type.
     */
    private PythonType inferVariableType(Binding binding) {
        if (binding.getAssignedValues().size() != 1) return PythonType.ANY;
        if (!inferring.add(binding)) return PythonType.ANY;
        try {
            return typeOfWithoutChecking(binding.getAssignedValues().getFirst());
        } finally {
            inferring.remove(binding);
        }
    }

    /**
     * Types an expression without reporting anything — used while inferring a
     * variable's type, where the expression's own errors are reported at the
     * place it actually appears.
     */
    private PythonType typeOfWithoutChecking(Condition expression) {
        if (expression == null) return PythonType.ANY;

        if (expression instanceof ParenAtom paren) return typeOfWithoutChecking(paren.inner);
        if (expression instanceof CompoundCondition) return PythonType.BOOL;
        if (expression instanceof RelationalComparison) return PythonType.BOOL;
        if (expression instanceof python.models.atom_statement.List) return PythonType.LIST;
        if (expression instanceof Set) return PythonType.SET;
        if (expression instanceof Dictionary) return PythonType.DICT;

        if (expression instanceof BinaryExpression binary) {
            PythonType left = typeOfWithoutChecking(binary.left);
            PythonType right = typeOfWithoutChecking(binary.right);
            if (!left.isKnown() || !right.isKnown()) return PythonType.ANY;
            if (!isBinaryValid(binary.operation, left, right)) return PythonType.ANY;
            return binaryResultType(binary.operation, left, right);
        }

        if (expression instanceof UnaryExpression unary) {
            PythonType operand = typeOfWithoutChecking(unary.expression);
            if (unary.operation == Operation.ADD || unary.operation == Operation.SUB)
                return operand.isNumeric() ? operand : PythonType.ANY;
            return PythonType.ANY;
        }

        if (expression instanceof IDTrailer idTrailer) return typeOfIdTrailerQuietly(idTrailer);
        if (expression instanceof ID id) {
            Binding binding = resolution.getBinding(id);
            return binding != null ? typeOf(binding) : PythonType.ANY;
        }

        return literalType(expression);
    }

    private PythonType typeOfIdTrailerQuietly(IDTrailer node) {
        PythonType current = baseType(node);
        if (node.trailers == null || node.trailers.isEmpty()) return current;

        for (int i = 0; i < node.trailers.size(); i++) {
            Trailer trailer = node.trailers.get(i);
            if (trailer == null) continue;

            boolean plainCall = trailer.arguments instanceof CallArguments
                    && !trailer.isDotIdTrailer();

            current = (plainCall && i == 0 && node.id != null
                    && PythonBuiltins.contains(node.id.name))
                    ? PythonBuiltins.callResult(node.id.name)
                    : PythonType.ANY;
        }
        return current;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    private static String symbolOf(Operation operation) {
        return switch (operation) {
            case ADD -> "+";
            case SUB -> "-";
            case MULT -> "*";
            case DIV -> "/";
            case IDIV -> "//";
            case MOD -> "%";
            case POWER -> "**";
            case LSHIFT -> "<<";
            case RSHIFT -> ">>";
            case AND -> "&";
            case OR -> "|";
            case XOR -> "^";
            case LESS_THAN -> "<";
            case GREATER_THAN -> ">";
            case LT_EQ -> "<=";
            case GT_EQ -> ">=";
            default -> operation.name();
        };
    }

    private void error(CompilerError.Kind kind, String message, int line) {
        errors.add(new CompilerError(kind, message, line));
    }
}
