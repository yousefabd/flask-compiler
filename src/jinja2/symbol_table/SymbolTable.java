package jinja2.symbol_table;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SymbolTable {

    private final Scope        templateScope;
    private final Deque<Scope> scopeStack = new ArrayDeque<>();
    private final List<Scope> allScopes  = new ArrayList<>();

    public SymbolTable() {
        templateScope = new Scope("template", ScopeKind.TEMPLATE, null);
        scopeStack.push(templateScope);
        allScopes.add(templateScope);
    }

    public void enterScope(String name, ScopeKind kind) {
        Scope scope = new Scope(name, kind, getCurrentScope());
        scopeStack.push(scope);
        allScopes.add(scope);
    }

    public void exitScope() {
        if (scopeStack.size() > 1) scopeStack.pop();
    }

    public Scope getCurrentScope()  { return scopeStack.peek(); }
    public Scope getTemplateScope() { return templateScope; }

    public boolean define(Symbol symbol) {
        return getCurrentScope().define(symbol);
    }

    public Symbol resolve(String name) {
        return getCurrentScope().resolve(name);
    }

    public List<Scope> getAllScopes() { return allScopes; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Scope scope : allScopes)
            sb.append(scope).append("\n");
        return sb.toString();
    }
}