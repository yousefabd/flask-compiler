package python.semantic;

import python.models.expr_statement.Condition;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * One declared name together with everything the analyzer proved about it.
 *
 * <p>Mirrors {@code jinja2.symbol_table.Symbol}. The declaring scope is kept
 * so that {@link NameResolver} can explain a SCOPE error ("declared in
 * function 'create_value'").</p>
 */
public final class Binding {

    private final String name;
    private final BindingKind kind;
    private final int declarationLine;
    private final PyScope owner;

    /**
     * Every right-hand side assigned to this name. Type inference only trusts
     * this when there is exactly one: Python allows rebinding a name to a
     * different type, so more than one assignment means the type is not
     * statically provable and must stay ANY.
     */
    private final List<Condition> assignedValues = new ArrayList<>();

    /** Explicit annotation type ({@code def f(age: int)}), null when absent. */
    private PythonType annotatedType;

    private final Set<Integer> usageLines = new LinkedHashSet<>();

    public Binding(String name, BindingKind kind, int declarationLine, PyScope owner) {
        this.name = name;
        this.kind = kind;
        this.declarationLine = declarationLine;
        this.owner = owner;
    }

    public String getName() { return name; }
    public BindingKind getKind() { return kind; }
    public int getDeclarationLine() { return declarationLine; }
    public PyScope getOwner() { return owner; }

    public List<Condition> getAssignedValues() { return assignedValues; }
    public void addAssignedValue(Condition value) {
        if (value != null) assignedValues.add(value);
    }

    public PythonType getAnnotatedType() { return annotatedType; }
    public void setAnnotatedType(PythonType annotatedType) { this.annotatedType = annotatedType; }

    public void addUsage(int line) { usageLines.add(line); }
    public Set<Integer> getUsageLines() { return usageLines; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind).append(' ').append(name);
        if (annotatedType != null) sb.append(": ").append(annotatedType.display());
        if (declarationLine >= 0) sb.append(" (line ").append(declarationLine).append(')');
        if (!usageLines.isEmpty()) sb.append(" | used at lines ").append(usageLines);
        return sb.toString();
    }
}
