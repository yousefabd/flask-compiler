package python.semantic;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A real Python scope — module or function only.
 *
 * <p>Mirrors {@code jinja2.symbol_table.Scope}, with two Python-specific
 * additions:</p>
 * <ul>
 *   <li>{@code assignedSoFar} tracks execution order inside the scope, which
 *       is what separates "used before it was assigned" (a NameError /
 *       UnboundLocalError) from "never declared at all".</li>
 *   <li>{@code globalNames} records {@code global x}, which redirects
 *       assignments to the module scope.</li>
 * </ul>
 */
public final class PyScope {

    private final String name;
    private final PyScopeKind kind;
    private final PyScope parent;

    private final Map<String, Binding> bindings = new LinkedHashMap<>();
    private final Set<String> globalNames = new HashSet<>();
    private final Set<String> assignedSoFar = new HashSet<>();

    public PyScope(String name, PyScopeKind kind, PyScope parent) {
        this.name = name;
        this.kind = kind;
        this.parent = parent;
    }

    public String getName() { return name; }
    public PyScopeKind getKind() { return kind; }
    public PyScope getParent() { return parent; }

    public boolean isModule() { return kind == PyScopeKind.MODULE; }

    public Collection<Binding> getBindings() { return bindings.values(); }

    /** Declares {@code binding} here unless the name is already declared. */
    public Binding declare(Binding binding) {
        Binding existing = bindings.get(binding.getName());
        if (existing != null) return existing;
        bindings.put(binding.getName(), binding);
        return binding;
    }

    public Binding resolveLocal(String name) {
        return bindings.get(name);
    }

    /** Walks the visible scope chain — the legal Python lookup path. */
    public Binding resolve(String name) {
        Binding local = bindings.get(name);
        if (local != null) return local;
        return parent != null ? parent.resolve(name) : null;
    }

    public void declareGlobal(String name) { globalNames.add(name); }

    public boolean isGlobal(String name) { return globalNames.contains(name); }

    public void markAssigned(String name) { assignedSoFar.add(name); }

    public boolean isAssigned(String name) { return assignedSoFar.contains(name); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(" \"").append(name).append("\": ").append(bindings.values());
        if (!globalNames.isEmpty()) sb.append(" | global: ").append(globalNames);
        return sb.toString();
    }
}
