package python.symbol_table;


import python.models.expr_statement.Condition;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Symbol
{
    private final String name;
    private final SymbolKind kind; // VARIABLE, FUNCTION, PARAMETER, BUILTIN
    private final int declarationLine; // -1 when unknown (pre-resolver symbols)

    // added: filled in by python.resolver.PythonResolver, not by SymbolTableBuilder —
    // a declaration alone doesn't tell you the value, only a second (resolution) pass does
    private final Set<Integer> usageLines = new LinkedHashSet<>(); // every line this symbol was read on

    // added: static type information, mirroring jinja2.symbol_table.Symbol.
    // `value` is the declared initializer expression (null when not applicable),
    // `type` is what SymbolTableBuilder could infer from it — ANY when unknown.
    private final SymbolType type;
    private final Condition  value;

    // added: only set for FUNCTION symbols — lets TypeCheckerRule verify call arity
    // and annotated parameter types (mirrors jinja2 Symbol.getParameters()).
    private final FunctionDef declaration;

    public Symbol(String name, SymbolKind kind) {
        this(name, kind, -1);
    }

    public Symbol(String name, SymbolKind kind, int declarationLine) {
        this(name, kind, declarationLine, SymbolType.ANY, null);
    }

    /** Variables, parameters, loop variables and builtins. */
    public Symbol(String name, SymbolKind kind, int declarationLine,
                  SymbolType type, Condition value) {
        this.name = name;
        this.kind = kind;
        this.declarationLine = declarationLine;
        this.type = type == null ? SymbolType.ANY : type;
        this.value = value;
        this.declaration = null;
    }

    /** Functions — type is always CALLABLE, the definition is kept for call checking. */
    public Symbol(String name, int declarationLine, FunctionDef declaration) {
        this.name = name;
        this.kind = SymbolKind.FUNCTION;
        this.declarationLine = declarationLine;
        this.type = SymbolType.CALLABLE;
        this.value = null;
        this.declaration = declaration;
    }

    public String getName() { return name; }
    public SymbolKind getKind() { return kind; }
    public int getDeclarationLine() { return declarationLine; }
    public SymbolType getType() { return type; }
    public Condition getValue() { return value; }
    public FunctionDef getDeclaration() { return declaration; }

    /** Declared parameters — empty for anything that is not a function. */
    public List<Parameter> getParameters() {
        if (declaration == null || declaration.parameters == null)
            return Collections.emptyList();
        return declaration.parameters;
    }

    public void addUsage(int line) { usageLines.add(line); }
    public Set<Integer> getUsageLines() { return usageLines; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name).append(": ").append(type);
        if (declarationLine >= 0) sb.append(" (line ").append(declarationLine).append(')');
        if (!usageLines.isEmpty()) sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}
