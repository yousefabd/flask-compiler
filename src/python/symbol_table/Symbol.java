package python.symbol_table;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** A static declaration and the source lines on which it is read. */
public final class Symbol {
    private final String name;
    private final SymbolKind kind;
    private final int declarationLine;
    private final Set<Integer> usageLines = new LinkedHashSet<>();

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

    public void addUsage(int line) {
        usageLines.add(line);
    }

    public Set<Integer> getUsageLines() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(usageLines));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(kind).append(' ').append(name);
        if (declarationLine >= 0) {
            result.append(" (line ").append(declarationLine).append(')');
        }
        if (!usageLines.isEmpty()) {
            result.append(" | used at lines ").append(usageLines);
        }
        return result.toString();
    }
}
