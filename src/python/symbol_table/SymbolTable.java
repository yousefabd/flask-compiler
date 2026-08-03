package python.symbol_table;

import python.models.atom_statement.ID;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable
{
    private final Scope globalScope;
    private final Deque<Scope> scopeStack = new ArrayDeque<>();
    List<Scope> allScopes = new ArrayList<>();

    // added: every ID node the builder resolved, mapped to the Symbol it refers to.
    // Mirrors jinja2.symbol_table.SymbolTable.bindings — it is what lets
    // semantic_rules.TypeCheckerRule ask "what type is this identifier?" without
    // redoing scope resolution.
    private final Map<ID, Symbol> bindings = new IdentityHashMap<>();

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

    // added: mirrors jinja2 SymbolTable.resolveGlobal — searches EVERY scope opened
    // during the walk, not just the visible chain. SymbolTableBuilder uses it to tell
    // "this name exists, but not here" (ScopeError) from "this name exists nowhere"
    // (UndefinedError).
    /** Finds {@code name} in any scope of the program, ignoring visibility. */
    public Symbol resolveAnywhere(String name) {
        for (Scope scope : allScopes) {
            Symbol sym = scope.resolveLocal(name);
            if (sym != null) return sym;
        }
        return null;
    }

    // added: called by SymbolTableBuilder once it resolves an identifier, so the AST
    // node stays traceable back to its declaring Symbol (same contract as the Jinja
    // symbol table's recordBinding).
    public void recordBinding(ID node, Symbol symbol) {
        bindings.put(node, symbol);
    }

    public Symbol getBinding(ID node) { return bindings.get(node); }

    public Map<ID, Symbol> getBindings() { return bindings; }

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

    // added: prints the scope tree the way jinja2.symbol_table.SymbolTable does,
    // so both tables read the same in the compiler output. Builtins are left out —
    // they are injected by PythonFrontend, identical on every run, and burying the
    // program's own symbols under forty of them helps nobody.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        printScopeTree(globalScope, "", true, sb);
        return sb.toString().stripTrailing();
    }

    private void printScopeTree(
            Scope scope,
            String prefix,
            boolean isLast,
            StringBuilder sb) {

        sb.append(prefix);

        if (!prefix.isEmpty())
            sb.append(isLast ? "└── " : "├── ");

        sb.append('"').append(scope.getName()).append('"').append('\n');

        String childPrefix = prefix + (isLast ? "    " : "│   ");

        for (Symbol symbol : scope.getSymbols()) {
            if (symbol.getKind() == SymbolKind.BUILTIN) continue;

            sb.append(childPrefix).append("• ").append(symbol).append('\n');
        }

        List<Scope> children = getChildren(scope);

        for (int i = 0; i < children.size(); i++) {
            printScopeTree(
                    children.get(i),
                    childPrefix,
                    i == children.size() - 1,
                    sb);
        }
    }

    private List<Scope> getChildren(Scope parent) {
        List<Scope> result = new ArrayList<>();

        for (Scope scope : allScopes) {
            if (scope.getParent() == parent)
                result.add(scope);
        }

        return result;
    }
}
