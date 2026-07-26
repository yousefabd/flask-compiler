package python.symbol_table;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Symbol
{
    private final String name;
    private final SymbolKind kind; // VARIABLE, FUNCTION, PARAMETER, ...
    private final int declarationLine; // -1 when unknown (pre-resolver symbols)

    // added: filled in by python.resolver.PythonResolver, not by SymbolTableBuilder —
    // a declaration alone doesn't tell you the value, only a second (resolution) pass does
    private final Set<Integer> usageLines = new LinkedHashSet<>(); // every line this symbol was read on

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


    public void addUsage(int line) { usageLines.add(line); }
    public Set<Integer> getUsageLines() { return usageLines; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name);
        if (declarationLine >= 0) sb.append(" (line ").append(declarationLine).append(')');
        if (!usageLines.isEmpty()) sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}
