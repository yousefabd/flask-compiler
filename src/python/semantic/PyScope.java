package python.semantic;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A real Python scope — module or function only.
 *
 * <p>Mirrors {@code jinja2.symbol_table.Scope}, with one Python-specific
 * addition: {@code globalNames} records {@code global x}, which redirects
 * assignments to the module scope.</p>
 *
 * <p>This deliberately does <em>not</em> track execution order within a
 * scope. An earlier version did, to separate "used before it was assigned"
 * from "never declared at all" — but doing that correctly needs control-flow
 * analysis (which branch of an {@code if}, which iteration of a loop), and
 * the version without it produced both false positives and false negatives.
 * See {@link NameResolver}'s class comment.</p>
 */
public final class PyScope {

    private final String name;
    private final PyScopeKind kind;
    private final PyScope parent;

    private final Map<String, Binding> bindings = new LinkedHashMap<>();
    private final Set<String> globalNames = new HashSet<>();

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

    /**
     * Declares {@code binding} here unless the name is already declared, in
     * which case the existing declaration is kept (and accumulates further
     * assigned values / usages instead of being replaced) — this is what
     * makes ordinary Python reassignment ({@code x = 1} then {@code x = 2})
     * resolve to one {@link Binding} rather than two.
     *
     * <p>One exception: if the existing and the new declaration are a
     * function/variable pair for the same name — {@code def convert(): ...}
     * followed by {@code convert = 3}, or the reverse — the name is marked
     * {@link Binding#markRebound() rebound}. Python allows this (the name
     * simply refers to whichever declaration ran last), but nothing here
     * models execution order, so which one is live at a given read is not
     * knowable; see {@link Binding#isRebound()}.</p>
     */
    public Binding declare(Binding binding) {
        Binding existing = bindings.get(binding.getName());
        if (existing != null) {
            if (isFunctionVariableClash(existing.getKind(), binding.getKind()))
                existing.markRebound();
            return existing;
        }
        bindings.put(binding.getName(), binding);
        return binding;
    }

    private static boolean isFunctionVariableClash(BindingKind a, BindingKind b) {
        return (a == BindingKind.FUNCTION && b == BindingKind.VARIABLE)
                || (a == BindingKind.VARIABLE && b == BindingKind.FUNCTION);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(" \"").append(name).append("\": ").append(bindings.values());
        if (!globalNames.isEmpty()) sb.append(" | global: ").append(globalNames);
        return sb.toString();
    }
}
