package python.symbol_table;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scope {
    private final String name;
    private final Scope parent;
    private final Map<String, Symbol> symbols = new HashMap<>();
    // added: names declared with `global` inside this scope (see declareGlobal/isGlobal)
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

    // added: record that `name` was declared `global` in this scope - SymbolTable.define()
    // checks this to redirect later assignments to the module/global scope instead
    /** Marks {@code name} as referring to the module-level scope for the rest of this scope. */
    public void declareGlobal(String name) {
        globalNames.add(name);
    }

    // added: used by SymbolTable.define() to decide whether to define `name` here or in globalScope
    public boolean isGlobal(String name) {
        return globalNames.contains(name);
    }

    // added: lets PythonResolver.report() enumerate every symbol declared directly
    // in this scope (mirrors jinja2.symbol_table.Scope.getSymbols()).
    public Collection<Symbol> getSymbols() {
        return symbols.values();
    }

    @Override
    public String toString() {
        String base = "Scope(" + name + "): " + symbols.values();
        // added: surface global declarations in printed output, e.g. "| global: [products]"
        if (!globalNames.isEmpty()) base += " | global: " + globalNames;
        return base;
    }
}