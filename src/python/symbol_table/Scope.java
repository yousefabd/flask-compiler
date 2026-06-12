package python.symbol_table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scope {
    private final String name;
    private final Scope parent;
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Set<String> globalNames = new HashSet<>();

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() { return name; }
    public Scope getParent() { return parent; }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false;
        }
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public Symbol resolve(String name) {
        Symbol sym = symbols.get(name);
        if (sym != null) return sym;
        if (parent != null) return parent.resolve(name);
        return null;
    }

    /** Marks {@code name} as referring to the module-level scope for the rest of this scope. */
    public void declareGlobal(String name) {
        globalNames.add(name);
    }

    public boolean isGlobal(String name) {
        return globalNames.contains(name);
    }

    @Override
    public String toString() {
        String base = "Scope(" + name + "): " + symbols.values();
        if (!globalNames.isEmpty()) base += " | global: " + globalNames;
        return base;
    }
}