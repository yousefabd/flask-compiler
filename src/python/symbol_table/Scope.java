package python.symbol_table;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final String name; 
    private final Scope parent; 
    private final Map<String, Symbol> symbols = new HashMap<>();

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

    @Override
    public String toString() {
        return "Scope(" + name + "): " + symbols.values();
    }
}