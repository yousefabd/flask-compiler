package python.symbol_table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Scope {
    private final String name;
    private final ScopeKind kind;
    private final Scope parent;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private final Set<String> globalNames = new LinkedHashSet<>();
    private final List<Scope> children = new ArrayList<>();

    public Scope(String name, ScopeKind kind, Scope parent) {
        this.name = name;
        this.kind = kind;
        this.parent = parent;
    }

    public String getName() { return name; }
    public ScopeKind getKind() { return kind; }
    public Scope getParent() { return parent; }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) return false;
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public Symbol resolve(String name) {
        if (kind == ScopeKind.FUNCTION && globalNames.contains(name)) {
            Scope root = this;
            while (root.parent != null) root = root.parent;
            return root.symbols.get(name);
        }
        Symbol symbol = symbols.get(name);
        if (symbol != null) return symbol;
        return parent == null ? null : parent.resolve(name);
    }

    public void declareGlobal(String name) {
        globalNames.add(name);
    }

    public boolean isGlobal(String name) {
        return globalNames.contains(name);
    }

    public Collection<Symbol> getSymbols() {
        return List.copyOf(symbols.values());
    }

    public Map<String, Symbol> getSymbolsByName() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(symbols));
    }

    public Set<String> getGlobalNames() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(globalNames));
    }

    void addChild(Scope child) {
        children.add(child);
    }

    public List<Scope> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public String toString() {
        String result = "Scope[" + kind + " " + name + "]: " + symbols.values();
        if (!globalNames.isEmpty()) result += " | global: " + globalNames;
        return result;
    }
}
