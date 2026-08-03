package python.symbol_table;

public enum SymbolKind {
    VARIABLE,
    FUNCTION,
    PARAMETER,
    // added: names Python provides for free (print, len, __name__, ...). They are
    // declared in the global scope before the walk starts, and are exempt from the
    // duplicate-declaration check so user code may legally shadow them.
    BUILTIN
}
