package python.symbol_table;

import resolver.ConstantValue;

import java.util.ArrayList;
import java.util.List;

public class Symbol
{
    private final String name;
    private final SymbolKind kind; // VARIABLE, FUNCTION, PARAMETER, ...
    private final int declarationLine; // -1 when unknown (pre-resolver symbols)

    // added: filled in by python.resolver.PythonResolver, not by SymbolTableBuilder —
    // a declaration alone doesn't tell you the value, only a second (resolution) pass does
    private ConstantValue value;                 // null until the resolver determines/invalidates it
    private final List<Integer> usageLines = new ArrayList<>(); // every line this symbol was read on

    public Symbol(String name, SymbolKind kind) {
        this(name, kind, -1);
    }

    public Symbol(String name, SymbolKind kind, int declarationLine) {
        this.name = name;
        this.kind = kind;
        this.declarationLine = declarationLine;
    }

    public String getName() { return name; }
    public SymbolKind getKind() { return kind; }
    public int getDeclarationLine() { return declarationLine; }

    public ConstantValue getValue() { return value; }
    public void setValue(ConstantValue value) { this.value = value; }
    /** Called when a later assignment/mutation proves the tracked value is no longer reliable. */
    public void invalidateValue() { this.value = ConstantValue.unknown(); }

    public void addUsage(int line) { usageLines.add(line); }
    public List<Integer> getUsageLines() { return usageLines; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name);
        if (declarationLine >= 0) sb.append(" (line ").append(declarationLine).append(')');
        if (value != null) sb.append(" = ").append(value.isKnown() ? value.display() : "unknown");
        if (!usageLines.isEmpty()) sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}
