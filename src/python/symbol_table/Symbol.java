package python.symbol_table;

public class Symbol 
{
    private final String name;
    private final SymbolKind kind; // VARIABLE, FUNCTION, PARAMETER, ...

    public Symbol(String name, SymbolKind kind) {
        this.name = name;
        this.kind = kind;
    }

    public String getName() { return name; }
    public SymbolKind getKind() { return kind; }

    @Override
    public String toString() {
        return kind + " " + name;
    }
}