package jinja2.symbol_table;
public enum SymbolKind {
    VARIABLE,    // {% set x = expr %} or {% set x %}...{% endset %}
    LOOP_VAR,    // {% for x in items %} — x
    MACRO,       // {% macro name(params) %}
    PARAMETER,   // parameter inside a macro
    BLOCK        // {% block name %}
}