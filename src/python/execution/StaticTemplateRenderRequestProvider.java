package python.execution;

import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.template.TemplateCall;
import errors.CodeGenError;
import jinja2.runtime.FlashMessage;
import jinja2.runtime.RenderEnvironment;
import jinja2.runtime.RouteDefinition;
import python.models.ASTNode;
import python.models.atom_statement.*;
import python.models.compound_statement.Body;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.enums.Operation;
import python.models.expr_statement.*;
import python.models.funcdef.FunctionDef;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.SmallStatement;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.Trailer;
import utils.CompilerUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a render context by folding the {@code render_template(...)} arguments
 * straight from the Python AST, without executing CPython.
 *
 * <p>This is the compile-time counterpart of
 * {@link CPythonTemplateRenderRequestProvider}: where that one runs the route and
 * captures what it produced, this one proves what the route must produce. It
 * succeeds only for calls whose every context value is a compile-time constant —
 * a literal, a collection of literals, an arithmetic expression over them, or a
 * name bound to one earlier in the module or the function.</p>
 *
 * <p>When anything cannot be proven — a value that comes from a function call, an
 * attribute, a subscript, or a conditionally executed {@code flash()} — it throws
 * {@link CodeGenError} rather than guessing. A partially-correct render context
 * would silently produce wrong HTML, which is worse than falling back to CPython.
 * {@link FallbackTemplateRenderRequestProvider} is what performs that fallback.</p>
 *
 * <p>The Flask runtime information the templates need is also derived statically:
 * {@code url_for} routes come from the {@code @app.route} decorators, and
 * {@code get_flashed_messages()} from the literal {@code flash(...)} calls in the
 * route body.</p>
 */
public final class StaticTemplateRenderRequestProvider
        implements TemplateRenderRequestProvider {

    /** Flask serves this endpoint itself; no decorator declares it. */
    private static final RouteDefinition STATIC_ROUTE =
            new RouteDefinition(
                    "static",
                    "/static/<path:filename>",
                    List.of("filename")
            );

    /** Matches {@code <converter:name>} and {@code <name>} inside a route rule. */
    private static final Pattern ROUTE_ARGUMENT =
            Pattern.compile("<(?:[^:<>]+:)?([^:<>]+)>");

    /** Flask's default flash category. */
    private static final String DEFAULT_FLASH_CATEGORY = "message";

    private final Program program;
    private final Path appSource;
    private final List<RouteDefinition> routes;
    private final Map<String, Object> moduleConstants;

    public StaticTemplateRenderRequestProvider(Program program, Path appSource) {
        this.program = Objects.requireNonNull(program);
        this.appSource = Objects.requireNonNull(appSource);
        this.routes = collectRoutes(program);
        this.moduleConstants = collectConstants(program.statements, Map.of());
    }

    @Override
    public TemplateRenderRequest provide(TemplateCall call) {
        Objects.requireNonNull(call);

        FunctionDef owner = findFunction(program, call.ownerFunctionName());

        Map<String, Object> locals =
                owner == null
                        ? Map.of()
                        : collectConstants(owner.body.statements, moduleConstants);

        Map<String, Object> context = new LinkedHashMap<>();

        for (Map.Entry<String, Condition> argument
                : call.contextArguments().entrySet()) {

            context.put(
                    argument.getKey(),
                    fold(argument.getValue(), locals, argument.getKey(), call));
        }

        List<FlashMessage> flashed =
                owner == null
                        ? List.of()
                        : collectFlashMessages(owner, locals, call);

        return new TemplateRenderRequest(
                call.templateName(),
                context,
                new RenderEnvironment(flashed, routes));
    }

    // ─────────────────────────────────────────────────────────────
    // ROUTES  —  @app.route('/product/<int:product_id>')
    // ─────────────────────────────────────────────────────────────

    private static List<RouteDefinition> collectRoutes(Program program) {
        List<RouteDefinition> routes = new ArrayList<>();
        routes.add(STATIC_ROUTE);

        collectRoutes(program, routes);

        return routes;
    }

    private static void collectRoutes(ASTNode node, List<RouteDefinition> routes) {
        if (node == null) return;

        if (node instanceof DecoratorStatement decorated
                && decorated.function != null
                && decorated.function.id != null
                && decorated.decorators != null) {

            String endpoint = decorated.function.id.name;

            for (Decorator decorator : decorated.decorators) {
                String rule = routeRule(decorator);
                if (rule == null) continue;

                routes.add(new RouteDefinition(
                        endpoint, rule, routeArguments(rule)));
            }
        }

        for (ASTNode child : node.getChildren())
            collectRoutes(child, routes);
    }

    /** The URL rule of an {@code @app.route("/path")} decorator, or null. */
    private static String routeRule(Decorator decorator) {
        if (decorator.dottedName == null || decorator.dottedName.isEmpty()) return null;

        ID last = decorator.dottedName.get(decorator.dottedName.size() - 1);

        if (!"route".equals(last.name)) return null;
        if (decorator.arguments == null || decorator.arguments.isEmpty()) return null;

        Argument first = decorator.arguments.get(0);

        if (first.isAssigned() || !(first.arg instanceof StringAtom rule)) return null;

        return CompilerUtils.stripStringQuotes(rule.value);
    }

    private static List<String> routeArguments(String rule) {
        List<String> arguments = new ArrayList<>();

        Matcher matcher = ROUTE_ARGUMENT.matcher(rule);

        while (matcher.find())
            arguments.add(matcher.group(1));

        return arguments;
    }

    // ─────────────────────────────────────────────────────────────
    // FLASH MESSAGES  —  flash("saved"), flash("careful", "warning")
    // ─────────────────────────────────────────────────────────────

    /**
     * The messages this route flashes before rendering.
     *
     * <p>Only {@code flash(...)} calls that run unconditionally — statements
     * directly in the function body — can be accounted for. A {@code flash()} inside
     * an {@code if} or a loop may or may not run, and a render context that guesses
     * would produce HTML the real app never shows, so the whole call falls back to
     * CPython instead.</p>
     */
    private List<FlashMessage> collectFlashMessages(
            FunctionDef owner, Map<String, Object> locals, TemplateCall call) {

        List<FlashMessage> messages = new ArrayList<>();

        for (Statement statement : owner.body.statements) {
            if (!(statement instanceof SimpleStatement simple)) {
                // A nested block: if it flashes anything, we cannot prove the result.
                if (containsFlashCall(statement))
                    throw notConstant(call,
                            "function '" + call.ownerFunctionName()
                                    + "' calls flash() inside a conditional or loop at line "
                                    + statement.getLine());
                continue;
            }

            for (SmallStatement small : simple.smallStatementList) {
                if (!(small instanceof ExpressionStatement expression)) continue;
                if (expression.haveEquals == Operation.EQUALS) continue;

                for (Condition condition : expression.conditions) {
                    CallArguments arguments = flashCallArguments(condition);
                    if (arguments == null) continue;

                    messages.add(readFlashMessage(arguments, locals, call));
                }
            }
        }

        return messages;
    }

    /** The argument list of a top-level {@code flash(...)} call, or null. */
    private static CallArguments flashCallArguments(Condition condition) {
        if (!(condition instanceof IDTrailer idTrailer)) return null;
        if (!"flash".equals(idTrailer.id.name)) return null;
        if (idTrailer.trailers == null || idTrailer.trailers.size() != 1) return null;

        Trailer trailer = idTrailer.trailers.get(0);

        if (trailer.isDotIdTrailer()) return null;
        if (!(trailer.arguments instanceof CallArguments arguments)) return null;

        return arguments;
    }

    private FlashMessage readFlashMessage(
            CallArguments arguments, Map<String, Object> locals, TemplateCall call) {

        List<Argument> given =
                arguments.args == null ? List.of() : arguments.args;

        if (given.isEmpty())
            throw notConstant(call, "flash() was called without a message");

        Object message = fold(given.get(0).arg, locals, "flash message", call);

        String category = DEFAULT_FLASH_CATEGORY;

        if (given.size() > 1) {
            Object folded = fold(given.get(1).arg, locals, "flash category", call);

            if (!(folded instanceof String text))
                throw notConstant(call, "the flash category is not a string literal");

            category = text;
        }

        return new FlashMessage(category, message);
    }

    private static boolean containsFlashCall(ASTNode node) {
        if (node == null) return false;

        if (node instanceof Condition condition
                && flashCallArguments(condition) != null) return true;

        for (ASTNode child : node.getChildren())
            if (containsFlashCall(child)) return true;

        return false;
    }

    // ─────────────────────────────────────────────────────────────
    // CONSTANTS  —  names bound to foldable values
    // ─────────────────────────────────────────────────────────────

    /**
     * Names assigned a compile-time constant by the statements directly in a block.
     *
     * <p>Assignments are folded in source order, so a later constant may be defined
     * in terms of an earlier one. Anything that does not fold is simply left out —
     * it only becomes an error if a render context actually asks for it.</p>
     */
    private Map<String, Object> collectConstants(
            List<Statement> statements, Map<String, Object> inherited) {

        Map<String, Object> constants = new LinkedHashMap<>(inherited);

        for (Statement statement : statements) {
            if (!(statement instanceof SimpleStatement simple)) continue;

            for (SmallStatement small : simple.smallStatementList) {
                if (!(small instanceof ExpressionStatement expression)) continue;
                if (expression.haveEquals != Operation.EQUALS) continue;
                if (expression.assigns == null) continue;
                if (expression.assigns.size() != expression.conditions.size()) continue;

                for (int i = 0; i < expression.conditions.size(); i++) {
                    String name = plainName(expression.conditions.get(i));
                    if (name == null) continue;

                    try {
                        constants.put(name,
                                foldOrFail(expression.assigns.get(i), constants));
                    } catch (NotConstantException notConstant) {
                        // Not a constant — later reads of this name will report it.
                        constants.remove(name);
                    }
                }
            }
        }

        return constants;
    }

    private static String plainName(Condition target) {
        if (target instanceof IDTrailer idTrailer
                && (idTrailer.trailers == null || idTrailer.trailers.isEmpty()))
            return idTrailer.id.name;
        if (target instanceof ID id) return id.name;
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // FOLDING
    // ─────────────────────────────────────────────────────────────

    /** Signals that an expression is not a compile-time constant. */
    private static final class NotConstantException extends RuntimeException {
        /** The name that could not be folded, when the failure came from one. */
        private final String symbol;

        NotConstantException(String message) { this(message, null); }

        NotConstantException(String message, String symbol) {
            super(message);
            this.symbol = symbol;
        }

        String getSymbol() { return symbol; }
    }

    private Object fold(Condition expression, Map<String, Object> constants,
                        String what, TemplateCall call) {
        try {
            return foldOrFail(expression, constants);

        } catch (NotConstantException notConstant) {
            String symbol = notConstant.getSymbol();

            // Naming the same thing twice ("'product' refers to 'product'") reads
            // badly; only mention the inner name when it differs from the label.
            String subject =
                    symbol != null && !symbol.equals(what)
                            ? "'" + what + "' depends on '" + symbol + "', which "
                            : "'" + what + "' ";

            throw notConstant(call, subject + notConstant.getMessage());
        }
    }

    /**
     * Folds an expression to a Java value using the same representation the CPython
     * provider produces through Gson: {@code Long} for integers, {@code Double} for
     * floats, {@code String}, {@code Boolean}, {@code null}, {@code List} and
     * {@code Map}. Matching that contract is what lets the two providers be swapped.
     */
    private Object foldOrFail(Condition expression, Map<String, Object> constants) {
        if (expression == null)
            throw new NotConstantException("is missing");

        if (expression instanceof IntegerAtom integer)
            return (long) integer.value;

        if (expression instanceof FloatAtom floating)
            // via toString: (double) 999.99f would be 999.989990234375
            return Double.parseDouble(Float.toString(floating.value));

        if (expression instanceof StringAtom text)
            return CompilerUtils.stripStringQuotes(text.value);

        if (expression instanceof BoolAtom truth)
            return truth.value;

        if (expression instanceof None)
            return null;

        if (expression instanceof ParenAtom paren)
            return foldOrFail(paren.inner, constants);

        if (expression instanceof python.models.atom_statement.List list)
            return foldAll(list.content, constants);

        if (expression instanceof python.models.atom_statement.Set set)
            return foldAll(set.content, constants);

        if (expression instanceof Dictionary dictionary)
            return foldDictionary(dictionary, constants);

        if (expression instanceof BinaryExpression binary)
            return foldBinary(binary, constants);

        if (expression instanceof IDTrailer idTrailer) {
            if (idTrailer.trailers != null && !idTrailer.trailers.isEmpty())
                throw new NotConstantException(
                        "is a call, attribute or index expression, "
                                + "whose value only exists at run time");

            return lookUp(idTrailer.id.name, constants);
        }

        if (expression instanceof ID id)
            return lookUp(id.name, constants);

        throw new NotConstantException(
                "is a " + expression.getSimpleName() + ", which cannot be folded");
    }

    private Object lookUp(String name, Map<String, Object> constants) {
        if (constants.containsKey(name))
            return constants.get(name);

        throw new NotConstantException("is not a compile-time constant", name);
    }

    private List<Object> foldAll(List<Expression> items, Map<String, Object> constants) {
        List<Object> folded = new ArrayList<>();

        for (Expression item : items)
            folded.add(foldOrFail(item, constants));

        return folded;
    }

    private Map<String, Object> foldDictionary(
            Dictionary dictionary, Map<String, Object> constants) {

        Map<String, Object> folded = new LinkedHashMap<>();

        for (int i = 0; i < dictionary.keys.size(); i++) {
            Object key = foldOrFail(dictionary.keys.get(i), constants);

            if (key == null)
                throw new NotConstantException("uses none as a dictionary key");

            folded.put(key.toString(),
                    foldOrFail(dictionary.values.get(i), constants));
        }

        return folded;
    }

    /** Arithmetic and concatenation over already-folded operands. */
    private Object foldBinary(BinaryExpression binary, Map<String, Object> constants) {
        Object left = foldOrFail(binary.left, constants);
        Object right = foldOrFail(binary.right, constants);

        if (left instanceof String text && binary.operation == Operation.ADD) {
            if (!(right instanceof String suffix))
                throw new NotConstantException("concatenates a string with a non-string");
            return text + suffix;
        }

        if (left instanceof String text
                && binary.operation == Operation.MULT
                && right instanceof Long count)
            return text.repeat(Math.max(0, count.intValue()));

        if (!(left instanceof Number leftNumber) || !(right instanceof Number rightNumber))
            throw new NotConstantException(
                    "applies " + binary.operation + " to values that are not numbers");

        boolean integral = left instanceof Long && right instanceof Long;

        double a = leftNumber.doubleValue();
        double b = rightNumber.doubleValue();

        return switch (binary.operation) {
            case ADD  -> integral ? (Object) (long) (a + b) : (Object) (a + b);
            case SUB  -> integral ? (Object) (long) (a - b) : (Object) (a - b);
            case MULT -> integral ? (Object) (long) (a * b) : (Object) (a * b);
            case DIV  -> {
                if (b == 0) throw new NotConstantException("divides by zero");
                yield a / b;
            }
            case MOD  -> {
                if (b == 0) throw new NotConstantException("takes a modulus of zero");
                yield integral ? (Object) (long) (a % b) : (Object) (a % b);
            }
            case POWER -> {
                double result = Math.pow(a, b);
                yield integral ? (Object) (long) result : (Object) result;
            }
            default -> throw new NotConstantException(
                    "uses the operator " + binary.operation
                            + ", which is not folded at compile time");
        };
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    private static FunctionDef findFunction(ASTNode node, String name) {
        if (node == null) return null;

        if (node instanceof FunctionDef function
                && function.id != null
                && function.id.name.equals(name))
            return function;

        for (ASTNode child : node.getChildren()) {
            FunctionDef found = findFunction(child, name);
            if (found != null) return found;
        }

        return null;
    }

    private CodeGenError notConstant(TemplateCall call, String detail) {
        return new CodeGenError(
                appSource.toString(),
                call.line(),
                "Cannot build the render context of '" + call.templateName()
                        + "' at compile time: " + detail);
    }
}
