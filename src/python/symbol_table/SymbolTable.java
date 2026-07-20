package python.symbol_table;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SymbolTable 
{
    private final Scope globalScope;
    private final Deque<Scope> scopeStack = new ArrayDeque<>();
    List<Scope> allScopes = new ArrayList<>();

    public SymbolTable() {
        globalScope = new Scope("global", null);
        scopeStack.push(globalScope);
        allScopes.add(getCurrentScope());
    }

    public Scope getCurrentScope() {
        return scopeStack.peek();
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public void enterScope(String name) {
        Scope newScope = new Scope(name, getCurrentScope());
        scopeStack.push(newScope);
        
    }

    public void exitScope() {
        allScopes.add(getCurrentScope());
        if (scopeStack.size() > 1) {
            scopeStack.pop();
        }
    }

    // تعريف رمز جديد في الـ scope الحالي
    // (or in the global scope, if this name was declared with `global` in the current scope)
    // added: when `name` was declared `global` in this scope, define it in globalScope
    // instead of the current (function) scope, so an assignment after `global x` updates
    // the module-level `x` rather than creating a local shadow.
    public boolean define(Symbol symbol) {
        Scope current = getCurrentScope();
        if (current != globalScope && current.isGlobal(symbol.getName()))
            return globalScope.define(symbol);
        return current.define(symbol);
    }

    // البحث عن اسم في الـ scope الحالي وما فوق
    public Symbol resolve(String name) {
        return getCurrentScope().resolve(name);
    }

    // added: used by PythonResolver, where re-assigning an existing name (completely
    // normal in Python — `x = 1` then `x = 2`) must return the SAME Symbol so usage
    // lines/values accumulate on it, instead of define()'s "reject duplicates" behavior
    // (which exists for the builder's *declaration* checks, not for plain reassignment).
    // Respects the same `global`-redirection rule as define().
    public Symbol defineOrGet(Symbol candidate) {
        Scope current = getCurrentScope();
        Scope target = (current != globalScope && current.isGlobal(candidate.getName()))
                ? globalScope : current;
        Symbol existing = target.resolveLocal(candidate.getName());
        if (existing != null) return existing;
        target.define(candidate);
        return candidate;
    }

    // added: entry point used by SymbolTableBuilder when it visits a GlobalStatement
    /** Records that {@code name} refers to the module-level variable for the rest of the current scope. */
    public void declareGlobal(String name) {
        getCurrentScope().declareGlobal(name);
    }

    // added: exposes every scope opened during the walk (global + all entered/exited
    // scopes) so PythonResolver.report() and other tooling can inspect the whole table
    // after build() has run, without depending on the (by-then-collapsed) scope stack.
    public List<Scope> getAllScopes() {
        return allScopes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Scope scope : allScopes) {
            sb.append(scope.toString()).append("\n");
        }
        return sb.toString();
    }
}
