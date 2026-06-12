package jinja2.symbol_table;

import jinja2.models.statement.ParameterNode;

import java.util.Collections;
import java.util.List;

public class Symbol {

    private final String name;
    private final SymbolKind kind;
    private final int lineNumber;
    private final List<ParameterNode> parameters; // non-null only for MACRO
    private final Type type;                      // inferred type for VARIABLE / LOOP_VAR

    public Symbol(String name, SymbolKind kind, int lineNumber) {
        this(name, kind, lineNumber, Type.UNKNOWN, Collections.emptyList());
    }

    public Symbol(String name, SymbolKind kind, int lineNumber, Type type) {
        this(name, kind, lineNumber, type, Collections.emptyList());
    }

    public Symbol(String name, SymbolKind kind, int lineNumber,
                  List<ParameterNode> parameters) {
        this(name, kind, lineNumber, Type.UNKNOWN, parameters);
    }

    private Symbol(String name, SymbolKind kind, int lineNumber,
                   Type type, List<ParameterNode> parameters) {
        this.name       = name;
        this.kind       = kind;
        this.lineNumber = lineNumber;
        this.type       = type;
        this.parameters = parameters;
    }

    public String getName()                    { return name; }
    public SymbolKind getKind()                { return kind; }
    public int getLineNumber()                 { return lineNumber; }
    public List<ParameterNode> getParameters() { return parameters; }
    public Type getType()                      { return type; }

    @Override
    public String toString() {
        String typeSuffix = type == Type.UNKNOWN ? "" : ": " + type.label();
        return kind + " " + name + typeSuffix + " (line " + lineNumber + ")";
    }
}