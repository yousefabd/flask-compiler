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

    /** Records that {@code name} refers to the module-level variable for the rest of the current scope. */
    public void declareGlobal(String name) {
        getCurrentScope().declareGlobal(name);
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
