package python.semantic;

/** What produced a name binding. Mirrors {@code jinja2.symbol_table.SymbolKind}. */
public enum BindingKind {
    VARIABLE,       // x = ...
    PARAMETER,      // def f(x)
    FUNCTION,       // def x()
    IMPORT,         // import x / from m import x
    LOOP_VARIABLE   // for x in ...
}
