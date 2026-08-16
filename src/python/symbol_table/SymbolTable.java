package python.symbol_table;

import python.models.funcdef.FunctionDef;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Static declarations arranged in Python lexical scopes. */
public final class SymbolTable {
    private final Scope moduleScope =
            new Scope("<module>", ScopeKind.MODULE, null);
    private final Deque<Scope> scopeStack = new ArrayDeque<>();
    private final List<Scope> allScopes = new ArrayList<>();
    private final Map<FunctionDef, Scope> functionScopes =
            new IdentityHashMap<>();

    public SymbolTable() {
        scopeStack.push(moduleScope);
        allScopes.add(moduleScope);
    }

    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    public Scope getModuleScope() {
        return moduleScope;
    }

    /** Compatibility alias for older callers. */
    public Scope getGlobalScope() {
        return moduleScope;
    }

    public Scope enterFunctionScope(FunctionDef function) {
        Objects.requireNonNull(function);
        if (functionScopes.containsKey(function)) {
            throw new IllegalStateException("Function scope was already created");
        }

        Scope parent = getCurrentScope();
        Scope scope = new Scope(
                "function " + function.id.name,
                ScopeKind.FUNCTION,
                parent
        );
        parent.addChild(scope);
        allScopes.add(scope);
        functionScopes.put(function, scope);
        scopeStack.push(scope);
        return scope;
    }

    public void exitScope() {
        if (scopeStack.size() == 1) {
            throw new IllegalStateException("Cannot exit the Python module scope");
        }
        scopeStack.pop();
    }

    public Scope getFunctionScope(FunctionDef function) {
        return functionScopes.get(function);
    }

    public boolean define(Symbol symbol) {
        return getCurrentScope().define(symbol);
    }

    public Symbol declareOrGet(Scope scope, Symbol candidate) {
        Objects.requireNonNull(scope);
        Objects.requireNonNull(candidate);
        Symbol existing = scope.resolveLocal(candidate.getName());
        if (existing != null) return existing;
        scope.define(candidate);
        return candidate;
    }

    public Scope assignmentScope(Scope scope, String name) {
        if (scope.getKind() == ScopeKind.FUNCTION && scope.isGlobal(name)) {
            return moduleScope;
        }
        return scope;
    }

    public Symbol resolve(Scope startingScope, String name) {
        Objects.requireNonNull(startingScope);
        Scope scope = startingScope;

        while (scope != null) {
            if (scope.getKind() == ScopeKind.FUNCTION && scope.isGlobal(name)) {
                return moduleScope.resolveLocal(name);
            }
            Symbol symbol = scope.resolveLocal(name);
            if (symbol != null) return symbol;
            scope = scope.getParent();
        }
        return null;
    }

    public Symbol resolve(String name) {
        return resolve(getCurrentScope(), name);
    }

    public Symbol findDeclaredAnywhere(String name) {
        for (Scope scope : allScopes) {
            Symbol symbol = scope.resolveLocal(name);
            if (symbol != null) return symbol;
        }
        return null;
    }

    public List<Scope> getAllScopes() {
        return List.copyOf(allScopes);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        appendScope(moduleScope, "", result);
        return result.toString();
    }

    private void appendScope(Scope scope, String indent, StringBuilder result) {
        result.append(indent).append(scope).append('\n');
        for (Scope child : scope.getChildren()) {
            appendScope(child, indent + "  ", result);
        }
    }
}
