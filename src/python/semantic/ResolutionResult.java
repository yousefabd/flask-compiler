package python.semantic;

import python.models.ASTNode;
import python.models.funcdef.FunctionDef;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Everything {@link NameResolver} proved, handed to the type-checking stage.
 *
 * <p>This is the boundary in the intended dependency direction:
 * {@code Python AST -> name resolution and bindings -> type checking -> semantic errors}.
 * The type checker reads this and never re-derives scoping.</p>
 */
public final class ResolutionResult {

    private final PyScope moduleScope;
    private final List<PyScope> allScopes = new ArrayList<>();

    /** Identifier AST node -> the declaration it refers to. */
    private final Map<ASTNode, Binding> bindings = new IdentityHashMap<>();

    /**
     * A function's own {@link Binding} -> its declaration. Keyed by the
     * resolved binding rather than by the function's bare name: two
     * functions can share a name in different scopes (a nested {@code def}
     * shadowing a module-level one), and each must keep its own parameter
     * annotations. A name-keyed map let one function's annotations leak into
     * type-mismatch checks for the other.
     */
    private final Map<Binding, FunctionDef> functionDefinitions = new IdentityHashMap<>();

    public ResolutionResult(PyScope moduleScope) {
        this.moduleScope = moduleScope;
        this.allScopes.add(moduleScope);
    }

    public PyScope getModuleScope() { return moduleScope; }

    public List<PyScope> getAllScopes() { return allScopes; }

    public void addScope(PyScope scope) { allScopes.add(scope); }

    public void recordBinding(ASTNode node, Binding binding) {
        if (node != null && binding != null) bindings.put(node, binding);
    }

    public Binding getBinding(ASTNode node) { return bindings.get(node); }

    public Map<ASTNode, Binding> getBindings() { return bindings; }

    public void recordFunction(Binding binding, FunctionDef function) {
        if (binding != null && function != null) functionDefinitions.put(binding, function);
    }

    /** The declaration for a name resolved to {@code binding}, or null if it is not a function. */
    public FunctionDef getFunction(Binding binding) {
        return binding != null ? functionDefinitions.get(binding) : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PyScope scope : allScopes) sb.append(scope).append('\n');
        return sb.toString();
    }
}
