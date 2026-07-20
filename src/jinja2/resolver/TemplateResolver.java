package jinja2.resolver;

import jinja2.models.TemplateNode;
import jinja2.symbol_table.Scope;
import jinja2.symbol_table.Symbol;
import jinja2.symbol_table.SymbolTable;

import java.util.Map;

/**
 * Reports the resolution state of an already-analyzed template.
 *
 * <p><b>Why this is a thin facade and not a second AST walk:</b> unlike the
 * Python pipeline (where {@code SymbolTableBuilder} never visits expressions
 * used as conditions/iterables, so nothing resolves identifier reads at all),
 * the Jinja2 {@code SymbolTableBuilder.visitExpression}/{@code visitIdentifier}
 * already walks every expression in the template and already calls
 * {@code symbolTable.resolve(name)} to do undefined-variable/type checking.
 * The only thing missing was recording *which* declaration a use resolved to
 * and *where* — so that recording was added directly to
 * {@code visitIdentifier} (see {@link SymbolTable#recordBinding}) instead of
 * re-walking the whole tree a second time here. This class is what turns that
 * captured state into the same kind of report the Python resolver produces.</p>
 */
public class TemplateResolver {

    private final SymbolTable symbolTable;

    public TemplateResolver(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() { return symbolTable; }
    public Map<TemplateNode, Symbol> getBindings() { return symbolTable.getBindings(); }

    /** A readable dump of every scope, its symbols, their resolved values and usage lines. */
    public String report() {
        StringBuilder sb = new StringBuilder();
        sb.append("Template Resolver Report\n");
        sb.append("=========================\n");
        for (Scope scope : symbolTable.getAllScopes()) {
            sb.append("Scope: ").append(scope.getKind()).append(" \"").append(scope.getName()).append("\"\n");
            for (Symbol sym : scope.getSymbols())
                sb.append("  - ").append(sym).append('\n');
        }
        return sb.toString();
    }
}
