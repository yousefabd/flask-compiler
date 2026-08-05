package python.semantic;

import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.atom_statement.Dictionary;
import python.models.atom_statement.ID;
import python.models.atom_statement.ParenAtom;
import python.models.atom_statement.Set;
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
import python.models.expr_statement.Expression;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.expr_statement.RelationalComparison;
import python.models.expr_statement.UnaryExpression;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.CompoundStatement;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.GlobalStatement;
import python.models.small_statement.ReturnStatement;
import python.models.small_statement.SmallStatement;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.symbol_table.CompilerError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Resolves every identifier in the Python AST against the legal, visible
 * Python scope chain and reports the two name-level semantic errors.
 *
 * <p>Read-only with respect to the AST: nothing here mutates AST nodes or any
 * other compiler stage. It produces a {@link ResolutionResult} and appends to
 * a caller-owned error list, exactly like the Jinja2 SymbolTableBuilder does.</p>
 *
 * <h2>Scoping</h2>
 * Only {@code def} creates a scope. {@code if}, {@code for} and {@code while}
 * do not, so a name assigned inside them stays visible in the surrounding
 * function or module scope.
 *
 * <h2>Which error is reported</h2>
 * <ol>
 *   <li>declared in this scope, at any position → resolved, no error. A read
 *       that textually precedes its assignment in the same scope (a real
 *       Python {@code UnboundLocalError} for some inputs, not others,
 *       depending on which branch or iteration actually ran) is <em>not</em>
 *       distinguished from an ordinary read, because doing that soundly needs
 *       control-flow analysis — which branch of an {@code if} was taken,
 *       which iteration of a loop. An earlier version approximated this with
 *       execution-order tracking and got it wrong in both directions: it
 *       accepted {@code for x in xs: print(carry); carry = x} (a real
 *       {@code UnboundLocalError} on iteration one) and rejected nothing for
 *       {@code if flag: value = 1} followed by {@code print(value)} outside
 *       the branch (also a possible {@code UnboundLocalError}, just one the
 *       loop-body heuristic didn't happen to touch). Rather than ship a
 *       check that is confidently wrong in specific, findable cases, this
 *       distinction is not made at all: any name declared anywhere in a
 *       reachable scope resolves.</li>
 *   <li>visible through an enclosing scope → resolved, no error. Function
 *       bodies run later than they are written, so a function may reference a
 *       module global declared further down the file.</li>
 *   <li>a Python builtin → never reported</li>
 *   <li>declared somewhere in the program but not reachable from here →
 *       {@code SCOPE}</li>
 *   <li>nothing anywhere → {@code UNDEFINED_VARIABLE}</li>
 * </ol>
 */
public final class NameResolver {

    private final List<CompilerError> errors;

    /** Every name declared anywhere in the program, for the SCOPE check. */
    private final java.util.Set<String> allDeclaredNames = new HashSet<>();

    /** Where an inaccessible name was declared, for the SCOPE message. */
    private final Map<String, String> declarationSite = new HashMap<>();

    private ResolutionResult result;

    public NameResolver(List<CompilerError> errors) {
        this.errors = errors;
    }

    public ResolutionResult resolve(Program program) {
        PyScope moduleScope = new PyScope("module", PyScopeKind.MODULE, null);
        result = new ResolutionResult(moduleScope);

        // Pre-pass: know every declared name up front so the SCOPE check does
        // not depend on the order functions happen to appear in the file.
        collectAllDeclaredNames(program, null);

        List<Statement> statements = program.statements;
        if (statements == null) return result;

        declareGlobalStatements(statements, moduleScope);
        collectScopeBindings(statements, moduleScope);
        walkStatements(statements, moduleScope);

        return result;
    }

    // ─────────────────────────────────────────────────────────────
    // PRE-PASS — every declared name in the whole program
    // ─────────────────────────────────────────────────────────────

    private void collectAllDeclaredNames(ASTNode node, String enclosingFunction) {
        if (node == null) return;

        String functionForChildren = enclosingFunction;

        if (node instanceof FunctionDef function) {
            if (function.id != null) {
                remember(function.id.name, enclosingFunction);
                functionForChildren = function.id.name;
            }
            if (function.parameters != null)
                for (Parameter parameter : function.parameters)
                    if (parameter.id != null)
                        remember(parameter.id.name, functionForChildren);
        }
        else if (node instanceof ForStatement forStatement) {
            if (forStatement.iterators != null)
                for (ID iterator : forStatement.iterators)
                    remember(iterator.name, enclosingFunction);
        }
        else if (node instanceof ExpressionStatement statement
                && statement.haveEquals == Operation.EQUALS
                && statement.conditions != null) {
            for (Condition target : statement.conditions) {
                String name = plainTargetName(target);
                if (name != null) remember(name, enclosingFunction);
            }
        }
        else if (node instanceof AugAssignStatement augAssign) {
            if (augAssign.id != null) remember(augAssign.id.name, enclosingFunction);
        }
        else if (node instanceof FromImportStatement fromImport) {
            if (fromImport.targets != null)
                for (ID target : fromImport.targets) remember(target.name, enclosingFunction);
        }
        else if (node instanceof SimpleImportStatement simpleImport) {
            // `import os.path` binds `os`, not `path` — Python only binds the
            // first component of a dotted import.
            if (simpleImport.dottedName != null && !simpleImport.dottedName.isEmpty())
                remember(simpleImport.dottedName.getFirst().name, enclosingFunction);
        }

        for (ASTNode child : node.getChildren())
            collectAllDeclaredNames(child, functionForChildren);
    }

    private void remember(String name, String enclosingFunction) {
        allDeclaredNames.add(name);
        if (enclosingFunction != null)
            declarationSite.putIfAbsent(name, "function '" + enclosingFunction + "'");
    }

    // ─────────────────────────────────────────────────────────────
    // PHASE A — which names does this scope declare?
    // ─────────────────────────────────────────────────────────────

    /** {@code global x} must be seen before any assignment to x is collected. */
    private void declareGlobalStatements(List<Statement> statements, PyScope scope) {
        for (Statement statement : statements) {
            if (statement instanceof SimpleStatement simple && simple.smallStatementList != null) {
                for (SmallStatement small : simple.smallStatementList)
                    if (small instanceof GlobalStatement global && global.names != null)
                        for (ID name : global.names) scope.declareGlobal(name.name);
            }
            else if (statement instanceof WhileStatement whileStatement) {
                declareGlobalsInBody(whileStatement.body, scope);
                declareGlobalsInBody(whileStatement.last, scope);
            }
            else if (statement instanceof ForStatement forStatement) {
                declareGlobalsInBody(forStatement.body, scope);
                declareGlobalsInBody(forStatement.last, scope);
            }
            else if (statement instanceof IfStatement ifStatement) {
                if (ifStatement.bodies != null)
                    for (Body body : ifStatement.bodies) declareGlobalsInBody(body, scope);
                declareGlobalsInBody(ifStatement.last, scope);
            }
            // A nested def owns its own `global` declarations — not this scope's.
        }
    }

    private void declareGlobalsInBody(Body body, PyScope scope) {
        if (body != null && body.statements != null)
            declareGlobalStatements(body.statements, scope);
    }

    private void collectScopeBindings(List<Statement> statements, PyScope scope) {
        for (Statement statement : statements) collectStatementBindings(statement, scope);
    }

    private void collectStatementBindings(Statement statement, PyScope scope) {
        if (statement instanceof SimpleStatement simple) {
            if (simple.smallStatementList != null)
                for (SmallStatement small : simple.smallStatementList)
                    collectSmallStatementBindings(small, scope);
            return;
        }
        if (!(statement instanceof CompoundStatement)) return;

        if (statement instanceof WhileStatement whileStatement) {
            collectBodyBindings(whileStatement.body, scope);
            collectBodyBindings(whileStatement.last, scope);
        }
        else if (statement instanceof ForStatement forStatement) {
            if (forStatement.iterators != null)
                for (ID iterator : forStatement.iterators)
                    declareIn(scope, iterator.name, BindingKind.LOOP_VARIABLE, iterator.getLine());
            collectBodyBindings(forStatement.body, scope);
            collectBodyBindings(forStatement.last, scope);
        }
        else if (statement instanceof IfStatement ifStatement) {
            if (ifStatement.bodies != null)
                for (Body body : ifStatement.bodies) collectBodyBindings(body, scope);
            collectBodyBindings(ifStatement.last, scope);
        }
        else if (statement instanceof DecoratorStatement decorated) {
            FunctionDef function = decorated.function;
            if (function != null && function.id != null)
                declareIn(scope, function.id.name, BindingKind.FUNCTION, function.getLine());
            // The body belongs to the function's own scope — not collected here.
        }
    }

    private void collectBodyBindings(Body body, PyScope scope) {
        if (body != null && body.statements != null) collectScopeBindings(body.statements, scope);
    }

    private void collectSmallStatementBindings(SmallStatement small, PyScope scope) {
        if (small instanceof ExpressionStatement statement) {
            if (statement.haveEquals != Operation.EQUALS || statement.conditions == null) return;
            for (int i = 0; i < statement.conditions.size(); i++) {
                Condition target = statement.conditions.get(i);
                String name = plainTargetName(target);
                if (name == null) continue;   // e.g. app.secret_key = ... — not a new name

                Binding binding = declareIn(scope, name, BindingKind.VARIABLE, target.getLine());
                Condition value = valueAt(statement, i);
                if (binding != null) binding.addAssignedValue(value);
            }
        }
        else if (small instanceof AugAssignStatement augAssign) {
            if (augAssign.id != null)
                declareIn(scope, augAssign.id.name, BindingKind.VARIABLE, augAssign.getLine());
        }
        else if (small instanceof ImportStatement importStatement) {
            collectImportBindings(importStatement, scope);
        }
    }

    private void collectImportBindings(ImportStatement importStatement, PyScope scope) {
        if (importStatement instanceof FromImportStatement fromImport) {
            if (fromImport.targets != null)
                for (ID target : fromImport.targets)
                    declareIn(scope, target.name, BindingKind.IMPORT, target.getLine());
        }
        else if (importStatement instanceof SimpleImportStatement simpleImport) {
            // `import os.path` binds `os`, not `path`.
            if (simpleImport.dottedName != null && !simpleImport.dottedName.isEmpty()) {
                ID first = simpleImport.dottedName.getFirst();
                declareIn(scope, first.name, BindingKind.IMPORT, first.getLine());
            }
        }
    }

    /** Honours {@code global x} by declaring into the module scope instead. */
    private Binding declareIn(PyScope scope, String name, BindingKind kind, int line) {
        PyScope target = targetScopeFor(scope, name);
        return target.declare(new Binding(name, kind, line, target));
    }

    private PyScope targetScopeFor(PyScope scope, String name) {
        if (!scope.isModule() && scope.isGlobal(name)) return result.getModuleScope();
        return scope;
    }

    // ─────────────────────────────────────────────────────────────
    // PHASE B — walk in execution order and resolve every use
    // ─────────────────────────────────────────────────────────────

    private void walkStatements(List<Statement> statements, PyScope scope) {
        for (Statement statement : statements) walkStatement(statement, scope);
    }

    private void walkStatement(Statement statement, PyScope scope) {
        if (statement instanceof SimpleStatement simple) {
            if (simple.smallStatementList != null)
                for (SmallStatement small : simple.smallStatementList)
                    walkSmallStatement(small, scope);
        }
        else if (statement instanceof WhileStatement whileStatement) {
            walkCondition(whileStatement.condition, scope);
            walkBody(whileStatement.body, scope);
            walkBody(whileStatement.last, scope);
        }
        else if (statement instanceof ForStatement forStatement) {
            walkCondition(forStatement.iterable, scope);
            if (forStatement.iterators != null) {
                for (ID iterator : forStatement.iterators) {
                    PyScope owner = targetScopeFor(scope, iterator.name);
                    Binding binding = owner.resolveLocal(iterator.name);
                    if (binding != null) result.recordBinding(iterator, binding);
                }
            }
            walkBody(forStatement.body, scope);
            walkBody(forStatement.last, scope);
        }
        else if (statement instanceof IfStatement ifStatement) {
            // if/elif/else do not create scopes, and assignments made inside a
            // branch stay visible afterwards — so the same scope is used
            // throughout and assignments accumulate.
            if (ifStatement.conditions != null)
                for (Condition condition : ifStatement.conditions) walkCondition(condition, scope);
            if (ifStatement.bodies != null)
                for (Body body : ifStatement.bodies) walkBody(body, scope);
            walkBody(ifStatement.last, scope);
        }
        else if (statement instanceof DecoratorStatement decorated) {
            if (decorated.decorators != null)
                for (Decorator decorator : decorated.decorators) walkDecorator(decorator, scope);
            walkFunctionDef(decorated.function, scope);
        }
    }

    private void walkBody(Body body, PyScope scope) {
        if (body != null && body.statements != null) walkStatements(body.statements, scope);
    }

    private void walkSmallStatement(SmallStatement small, PyScope scope) {
        if (small instanceof ExpressionStatement statement) {
            walkExpressionStatement(statement, scope);
        }
        else if (small instanceof AugAssignStatement augAssign) {
            // x += 1 reads x before writing it.
            if (augAssign.id != null) resolveUse(augAssign.id, augAssign.id.name, augAssign.getLine(), scope);
            walkCondition(augAssign.expression, scope);
        }
        else if (small instanceof ReturnStatement returnStatement) {
            if (returnStatement.conditions != null)
                for (Condition condition : returnStatement.conditions) walkCondition(condition, scope);
        }
        // pass / break / continue / global / import — nothing left to resolve;
        // imports are already bound in the collect phase and have no
        // expression of their own to read.
    }

    private void walkExpressionStatement(ExpressionStatement statement, PyScope scope) {
        if (statement.haveEquals != Operation.EQUALS) {
            if (statement.conditions != null)
                for (Condition condition : statement.conditions) walkCondition(condition, scope);
            return;
        }

        // Python evaluates the right-hand side first, so `x = x + 1` still
        // reads the previous x.
        if (statement.assigns != null)
            for (Condition value : statement.assigns) walkCondition(value, scope);

        if (statement.conditions == null) return;

        for (Condition target : statement.conditions) {
            String name = plainTargetName(target);
            if (name == null) {
                // app.secret_key = ... / items[0] = ... — the base is a use.
                walkCondition(target, scope);
                continue;
            }
            PyScope owner = targetScopeFor(scope, name);
            Binding binding = owner.resolveLocal(name);
            if (binding != null) result.recordBinding(identifierNodeOf(target), binding);
        }
    }

    private void walkDecorator(Decorator decorator, PyScope scope) {
        if (decorator == null) return;
        // @app.route(...) — only the head of the dotted name is a variable.
        if (decorator.dottedName != null && !decorator.dottedName.isEmpty()) {
            ID head = decorator.dottedName.getFirst();
            resolveUse(head, head.name, head.getLine(), scope);
        }
        if (decorator.arguments != null)
            for (Argument argument : decorator.arguments) walkArgument(argument, scope);
    }

    private void walkFunctionDef(FunctionDef function, PyScope enclosing) {
        if (function == null) return;

        String functionName = function.id != null ? function.id.name : "<anonymous>";

        if (function.id != null) {
            PyScope owner = targetScopeFor(enclosing, functionName);
            Binding binding = owner.resolveLocal(functionName);
            if (binding != null) {
                result.recordBinding(function.id, binding);
                // Keyed by this specific Binding, not by the bare name — two
                // functions can share a name in different scopes, and each
                // keeps its own parameter annotations (see ResolutionResult).
                result.recordFunction(binding, function);
            }
        }

        // Annotations and defaults are evaluated in the enclosing scope.
        if (function.parameters != null) {
            for (Parameter parameter : function.parameters) {
                if (parameter.type != null) walkCondition(parameter.type, enclosing);
                if (parameter.defaultValue != null) walkCondition(parameter.defaultValue, enclosing);
            }
        }
        if (function.returnType != null) walkCondition(function.returnType, enclosing);

        PyScope functionScope =
                new PyScope("function " + functionName, PyScopeKind.FUNCTION, enclosing);
        result.addScope(functionScope);

        if (function.parameters != null) {
            for (Parameter parameter : function.parameters) {
                if (parameter.id == null) continue;
                Binding binding = functionScope.declare(new Binding(
                        parameter.id.name, BindingKind.PARAMETER, parameter.getLine(), functionScope));
                binding.setAnnotatedType(annotationType(parameter.type));
                result.recordBinding(parameter.id, binding);
            }
        }

        if (function.body != null && function.body.statements != null) {
            declareGlobalStatements(function.body.statements, functionScope);
            collectScopeBindings(function.body.statements, functionScope);
            walkStatements(function.body.statements, functionScope);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS
    // ─────────────────────────────────────────────────────────────

    private void walkCondition(Condition condition, PyScope scope) {
        if (condition == null) return;

        if (condition instanceof CompoundCondition compound) {
            walkCondition(compound.first, scope);
            walkCondition(compound.second, scope);
        }
        else if (condition instanceof RelationalComparison comparison) {
            walkCondition(comparison.left, scope);
            walkCondition(comparison.right, scope);
        }
        else if (condition instanceof BinaryExpression binary) {
            walkCondition(binary.left, scope);
            walkCondition(binary.right, scope);
        }
        else if (condition instanceof UnaryExpression unary) {
            walkCondition(unary.expression, scope);
        }
        else if (condition instanceof ParenAtom paren) {
            walkCondition(paren.inner, scope);
        }
        else if (condition instanceof python.models.atom_statement.List list) {
            if (list.content != null)
                for (Expression element : list.content) walkCondition(element, scope);
        }
        else if (condition instanceof Set set) {
            if (set.content != null)
                for (Expression element : set.content) walkCondition(element, scope);
        }
        else if (condition instanceof Dictionary dictionary) {
            if (dictionary.keys != null)
                for (Expression key : dictionary.keys) walkCondition(key, scope);
            if (dictionary.values != null)
                for (Expression value : dictionary.values) walkCondition(value, scope);
        }
        else if (condition instanceof IDTrailer idTrailer) {
            if (idTrailer.id != null)
                resolveUse(idTrailer.id, idTrailer.id.name, idTrailer.id.getLine(), scope);
            if (idTrailer.trailers != null)
                for (Trailer trailer : idTrailer.trailers) walkTrailer(trailer, scope);
        }
        else if (condition instanceof ID id) {
            resolveUse(id, id.name, id.getLine(), scope);
        }
        // literals (int/float/string/bool/None) resolve to nothing
    }

    private void walkTrailer(Trailer trailer, PyScope scope) {
        if (trailer == null) return;
        // `.name` is an attribute, not a variable — deliberately not resolved.
        if (trailer.arguments instanceof CallArguments call) {
            if (call.args != null)
                for (Argument argument : call.args) walkArgument(argument, scope);
        }
        else if (trailer.arguments instanceof SubscriptArguments subscript) {
            if (subscript.conditions != null)
                for (Condition condition : subscript.conditions) walkCondition(condition, scope);
        }
    }

    /**
     * For {@code f(page='home')} the keyword itself is a parameter name, not a
     * variable in the caller's scope, so only the value is resolved.
     */
    private void walkArgument(Argument argument, PyScope scope) {
        if (argument == null) return;
        if (argument.isAssigned()) walkCondition(argument.assign, scope);
        else walkCondition(argument.arg, scope);
    }

    // ─────────────────────────────────────────────────────────────
    // THE RESOLUTION DECISION
    // ─────────────────────────────────────────────────────────────

    private void resolveUse(ASTNode node, String name, int line, PyScope scope) {
        if (name == null) return;

        Binding local = scope.resolveLocal(name);
        if (local != null) {
            local.addUsage(line);
            result.recordBinding(node, local);
            return;
        }

        // Enclosing scopes: a function body runs after the module finished, so
        // no execution-order check applies here.
        Binding outer = scope.getParent() != null ? scope.getParent().resolve(name) : null;
        if (outer != null) {
            outer.addUsage(line);
            result.recordBinding(node, outer);
            return;
        }

        if (PythonBuiltins.contains(name)) return;

        if (allDeclaredNames.contains(name)) {
            String site = declarationSite.get(name);
            errors.add(new CompilerError(
                    CompilerError.Kind.SCOPE,
                    "Variable '" + name + "' is not visible in this scope"
                            + (site != null ? " (declared in " + site + ")" : ""),
                    line));
            return;
        }

        errors.add(new CompilerError(
                CompilerError.Kind.UNDEFINED_VARIABLE,
                "Undefined variable '" + name + "'",
                line));
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    /** Name of a plain assignment target, or null when it is not a bare name. */
    public static String plainTargetName(Condition target) {
        if (target instanceof IDTrailer idTrailer) {
            boolean bare = idTrailer.trailers == null || idTrailer.trailers.isEmpty();
            return bare && idTrailer.id != null ? idTrailer.id.name : null;
        }
        if (target instanceof ID id) return id.name;
        return null;
    }

    private static ASTNode identifierNodeOf(Condition target) {
        if (target instanceof IDTrailer idTrailer) return idTrailer.id;
        if (target instanceof ID id) return id;
        return null;
    }

    private static Condition valueAt(ExpressionStatement statement, int index) {
        if (statement.assigns == null) return null;
        // `a, b = 1, 2` pairs positionally; anything else is not provable.
        if (statement.assigns.size() == statement.conditions.size())
            return statement.assigns.get(index);
        return null;
    }

    /** Reads {@code age: int} into a {@link PythonType}, or null when absent. */
    public static PythonType annotationType(Condition annotation) {
        String name = plainTargetName(annotation);
        return name != null ? PythonType.fromTypeName(name) : null;
    }
}
