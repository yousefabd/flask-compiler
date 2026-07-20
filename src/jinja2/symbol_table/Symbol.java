package jinja2.symbol_table;

import jinja2.models.statement.ParameterNode;
import resolver.ConstantValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Symbol {

    private final String name;
    private final SymbolKind kind;
    private final int lineNumber;
    private final List<ParameterNode> parameters; // non-null only for MACRO
    private final Type type;                      // inferred type for VARIABLE / LOOP_VAR

    // added: filled in during resolution (SymbolTableBuilder.visitIdentifier / the
    // Flask-aware seeding in FlaskProjectGenerator), not at declaration time — a
    // declaration alone doesn't tell you the value, only resolving its uses does
    private ConstantValue value;                                    // null until known/invalidated
    private final List<Integer> usageLines = new ArrayList<>();     // every line this symbol was read on

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

    public ConstantValue getValue()            { return value; }
    public void setValue(ConstantValue value)  { this.value = value; }
    public void invalidateValue()              { this.value = ConstantValue.unknown(); }

    public void addUsage(int line)             { usageLines.add(line); }
    public List<Integer> getUsageLines()       { return usageLines; }

    @Override
    public String toString() {
        String typeSuffix = type == Type.UNKNOWN ? "" : ": " + type.label();
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name).append(typeSuffix)
          .append(" (line ").append(lineNumber).append(')');
        if (value != null) sb.append(" = ").append(value.isKnown() ? value.display() : "unknown");
        if (!usageLines.isEmpty()) sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}