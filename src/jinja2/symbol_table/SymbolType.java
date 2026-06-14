package jinja2.symbol_table;

public enum SymbolType {
    STRING,
    NUMBER,
    BOOLEAN,
    NONE,
    LIST,
    DICT,
    CALLABLE,  // macro
    ANY        // runtime-determined; can't infer statically
}