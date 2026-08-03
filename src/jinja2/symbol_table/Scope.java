package jinja2.symbol_table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Scope {

    private final String    name;
    private final ScopeKind kind;
    private final Scope     parent;
    private final Map<String, Symbol> symbols = new HashMap<>();

    public Scope(String name, ScopeKind kind, Scope parent) {
        this.name   = name;
        this.kind   = kind;
        this.parent = parent;
    }

    public String    getName()   { return name; }
    public ScopeKind getKind()   { return kind; }
    public Scope     getParent() { return parent; }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) return false;
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    /** Unconditionally set a symbol — used for Jinja2 variable reassignment. */
    public void overwrite(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public Symbol resolve(String name) {
        Symbol sym = symbols.get(name);
        if (sym != null)    return sym;
        if (parent != null) return parent.resolve(name);
        return null;
    }

    public Collection<Symbol> getSymbols() { return symbols.values(); }

    // added: the scope path an error was found in, e.g. `template > for > macro card`.
    // Mirrors python.symbol_table.Scope.getQualifiedName() so both front ends can
    // report the same "(in ...)" context.
    /** Fully qualified scope path, from the template scope down to this one. */
    public String getQualifiedName() {
        if (parent == null) return name;
        return parent.getQualifiedName() + " > " + name;
    }

    @Override
    public String toString() {
        return "Scope[" + kind + " \"" + name + "\"]: " + symbols.values();
    }
}