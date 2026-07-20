package jinja2.symbol_table;

import jinja2.models.TemplateNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    private final Scope templateScope;
    private Scope currentScope;
    private final List<Scope> allScopes  = new ArrayList<>();
    // added: every identifier node resolved so far, mapped to the Symbol it refers to —
    // populated by SymbolTableBuilder.visitIdentifier(), read by jinja2.resolver.TemplateResolver
    private final Map<TemplateNode, Symbol> bindings = new IdentityHashMap<>();

    public SymbolTable() {
        templateScope = new Scope("template", ScopeKind.TEMPLATE, null);
        currentScope = templateScope;
        allScopes.add(templateScope);
    }

    public void enterScope(String name, ScopeKind kind) {
        currentScope = new Scope(name, kind, getCurrentScope());
        allScopes.add(currentScope);
    }

    public void exitScope() {
        currentScope = currentScope.getParent();
    }

    public Scope getCurrentScope()  { return currentScope; }
    public Scope getTemplateScope() { return templateScope; }

    public boolean define(Symbol symbol) {
        return getCurrentScope().define(symbol);
    }

    public void overwrite(Symbol symbol) {
        getCurrentScope().overwrite(symbol);
    }

    public boolean defineInTemplateScope(Symbol symbol) {
        return templateScope.define(symbol);
    }

    public Symbol resolve(String name) {
        return getCurrentScope().resolve(name);
    }
    public Symbol resolveGlobal(String name) {
        for(Scope scope : allScopes) {
            Symbol sym = scope.resolveLocal(name);

            if (sym != null)
                return sym;
        }
        return null;
    }

    public List<Scope> getAllScopes() { return allScopes; }

    // added: called by SymbolTableBuilder.visitIdentifier() once it resolves an
    // identifier, so the AST node stays traceable back to its declaring Symbol —
    // this is the "connect every identifier node with its declaration" requirement.
    public void recordBinding(TemplateNode node, Symbol symbol) {
        bindings.put(node, symbol);
    }

    public Symbol getBinding(TemplateNode node) { return bindings.get(node); }
    public Map<TemplateNode, Symbol> getBindings() { return bindings; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        printScopeTree(templateScope, "", true, sb);
        return sb.toString();
    }

    private void printScopeTree(
            Scope scope,
            String prefix,
            boolean isLast,
            StringBuilder sb) {

        sb.append(prefix);

        if (!prefix.isEmpty())
            sb.append(isLast ? "└── " : "├── ");

        sb.append(scope.getKind())
                .append(" \"")
                .append(scope.getName())
                .append("\"")
                .append("\n");

        for (Symbol symbol : scope.getSymbols()) {
            sb.append(prefix)
                    .append(isLast ? "    " : "│   ")
                    .append("• ")
                    .append(symbol)
                    .append("\n");
        }

        List<Scope> children = getChildren(scope);

        for (int i = 0; i < children.size(); i++) {
            printScopeTree(
                    children.get(i),
                    prefix + (isLast ? "    " : "│   "),
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