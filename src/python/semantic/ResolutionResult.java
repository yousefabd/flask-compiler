package python.semantic;

import python.models.ASTNode;
import python.models.funcdef.FunctionDef;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
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

    /** Module-level functions, used for annotated-parameter checking. */
    private final Map<String, FunctionDef> functions = new LinkedHashMap<>();

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

    public void recordFunction(FunctionDef function) {
        if (function != null && function.id != null)
            functions.putIfAbsent(function.id.name, function);
    }

    public FunctionDef getFunction(String name) { return functions.get(name); }

    public Map<String, FunctionDef> getFunctions() { return functions; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PyScope scope : allScopes) sb.append(scope).append('\n');
        return sb.toString();
    }
}
