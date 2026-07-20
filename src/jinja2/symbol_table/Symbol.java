package jinja2.symbol_table;

import jinja2.models.expression.*;
import jinja2.models.expression.literal.*;
import jinja2.models.statement.ParameterNode;
import resolver.ConstantValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Symbol {

    private final String              name;
    private final SymbolKind          kind;
    private final int                 lineNumber;
    private final ExpressionNode      value;       // declared initializer, null when not applicable
    private final SymbolType          type;        // statically inferred (shallow)
    private final List<ParameterNode> parameters;  // non-empty only for MACRO

    // added: filled in by SymbolTableBuilder.visitSetStatement for provable
    // {% set x = <literal> %} assignments, left null otherwise. Distinct from
    // `value` above, which is the raw *declared expression*, not its evaluated
    // compile-time constant — read by jinja2.resolver.TemplateResolver's report
    // and by the code generator's constant-folding preview.
    private ConstantValue resolvedValue;
    private final List<Integer> usageLines = new ArrayList<>();   // every line this symbol was read on

    /** Variables, loop vars, parameters, blocks. */
    public Symbol(String name, SymbolKind kind, int lineNumber, ExpressionNode value) {
        this.name       = name;
        this.kind       = kind;
        this.lineNumber = lineNumber;
        this.value      = value;
        this.parameters = Collections.emptyList();
        this.type       = inferType(kind, value);
    }

    /** Macros — value is always null, type is always CALLABLE. */
    public Symbol(String name, int lineNumber, List<ParameterNode> parameters) {
        this.name       = name;
        this.kind       = SymbolKind.MACRO;
        this.lineNumber = lineNumber;
        this.value      = null;
        this.parameters = parameters;
        this.type       = SymbolType.CALLABLE;
    }

    private static SymbolType inferType(SymbolKind kind, ExpressionNode value) {
        if (kind == SymbolKind.MACRO)              return SymbolType.CALLABLE;
        if (value == null)                         return SymbolType.ANY;
        if (value instanceof StringLiteralNode)    return SymbolType.STRING;
        if (value instanceof NumberLiteralNode)    return SymbolType.NUMBER;
        if (value instanceof BooleanLiteralNode)   return SymbolType.BOOLEAN;
        if (value instanceof NoneLiteralNode)      return SymbolType.NONE;
        if (value instanceof ListExpressionNode)   return SymbolType.LIST;
        if (value instanceof DictionaryExpressionNode) return SymbolType.DICT;
        return SymbolType.ANY;
    }

    public String              getName()       { return name; }
    public SymbolKind          getKind()       { return kind; }
    public int                 getLineNumber() { return lineNumber; }
    public ExpressionNode      getValue()      { return value; }
    public SymbolType          getType()       { return type; }
    public List<ParameterNode> getParameters() { return parameters; }

    public ConstantValue getResolvedValue()       { return resolvedValue; }
    public void setResolvedValue(ConstantValue v) { this.resolvedValue = v; }

    public void addUsage(int line)       { usageLines.add(line); }
    public List<Integer> getUsageLines() { return usageLines; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name).append(": ").append(type)
          .append(" (line ").append(lineNumber).append(')');
        if (resolvedValue != null)
            sb.append(" = ").append(resolvedValue.isKnown() ? resolvedValue.display() : "unknown");
        if (!usageLines.isEmpty())
            sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}
