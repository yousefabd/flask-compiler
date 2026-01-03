package css.symbol_table;

import css.models.declarations.Value;

import java.util.*;

public class SymbolTable {
    private final Deque<Map<String, Value>> scopes = new ArrayDeque<>();
    public List<Map<String,Value>> allScopes = new ArrayList<>();

    public SymbolTable() {
        pushScope(); // global scope
    }

    public void pushScope() {
        scopes.push(new HashMap<>());
    }

    public void popScope() {
        allScopes.add(scopes.peek());
        scopes.pop();

    }

    /* ---------------- Define ---------------- */

    public void define(String name, Value value) {
        assert scopes.peek() != null;
        scopes.peek().put(name, value);
    }


    public Value resolve(String name) {
        for (Map<String, Value> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }
    public List<Map<String,Value>> getAllScopes(){
        return allScopes;
    }
}
