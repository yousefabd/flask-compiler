package jinja2.symbol_table;

import jinja2.models.statement.ParameterNode;

import java.util.Collections;
import java.util.List;

public class Symbol {

    private final String name;
    private final SymbolKind kind;
    private final int lineNumber;
    private final List<ParameterNode> parameters; // non-null only for MACRO

    public Symbol(String name, SymbolKind kind, int lineNumber) {
        this.name       = name;
        this.kind       = kind;
        this.lineNumber = lineNumber;
        this.parameters = Collections.emptyList();
    }

    public Symbol(String name, SymbolKind kind, int lineNumber,
                  List<ParameterNode> parameters) {
        this.name       = name;
        this.kind       = kind;
        this.lineNumber = lineNumber;
        this.parameters = parameters;
    }

    public String getName()                    { return name; }
    public SymbolKind getKind()                { return kind; }
    public int getLineNumber()                 { return lineNumber; }
    public List<ParameterNode> getParameters() { return parameters; }

    @Override
    public String toString() {
        return kind + " " + name + " (line " + lineNumber + ")";
    }
}