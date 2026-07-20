package resolver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A compile-time-known value discovered by a resolver.
 *
 * <p>Both the Python and the Jinja2 resolver attach one of these to a
 * {@code Symbol} whenever they can prove, by static analysis alone, exactly
 * what value a variable holds — e.g. a literal assignment (`x = 5`) or a
 * literal keyword argument at a {@code render_template} call site
 * (`page='home'`). Anything that depends on runtime state (a function call,
 * user input, a mutated list, ...) is represented as {@link Kind#UNKNOWN}
 * instead of guessed at.</p>
 *
 * <p>This is deliberately a small, best-effort constant-folding value model —
 * not a full interpreter's runtime value — appropriate for an educational
 * compiler: it only ever records a value when the source text alone
 * guarantees it.</p>
 */
public final class ConstantValue {

    public enum Kind { INT, FLOAT, STRING, BOOL, NONE, LIST, DICT, UNKNOWN }

    private static final ConstantValue UNKNOWN = new ConstantValue(Kind.UNKNOWN, null, null, null);
    private static final ConstantValue NONE    = new ConstantValue(Kind.NONE, null, null, null);

    private final Kind kind;
    private final Object scalar;                 // Integer, Double, String or Boolean
    private final List<ConstantValue> elements;   // non-null only for LIST
    private final Map<String, ConstantValue> entries; // non-null only for DICT

    private ConstantValue(Kind kind, Object scalar,
                          List<ConstantValue> elements, Map<String, ConstantValue> entries) {
        this.kind = kind;
        this.scalar = scalar;
        this.elements = elements;
        this.entries = entries;
    }

    public static ConstantValue unknown()            { return UNKNOWN; }
    public static ConstantValue none()                { return NONE; }
    public static ConstantValue ofInt(int v)          { return new ConstantValue(Kind.INT, v, null, null); }
    public static ConstantValue ofFloat(double v)     { return new ConstantValue(Kind.FLOAT, v, null, null); }
    public static ConstantValue ofString(String v)    { return new ConstantValue(Kind.STRING, v, null, null); }
    public static ConstantValue ofBool(boolean v)     { return new ConstantValue(Kind.BOOL, v, null, null); }
    public static ConstantValue ofList(List<ConstantValue> v) {
        return new ConstantValue(Kind.LIST, null, new ArrayList<>(v), null);
    }
    public static ConstantValue ofDict(Map<String, ConstantValue> v) {
        return new ConstantValue(Kind.DICT, null, null, new LinkedHashMap<>(v));
    }

    public Kind getKind()          { return kind; }
    public boolean isKnown()       { return kind != Kind.UNKNOWN; }

    public int asInt()             { return (Integer) scalar; }
    public double asFloat()        { return (Double) scalar; }
    public String asString()       { return (String) scalar; }
    public boolean asBool()        { return (Boolean) scalar; }
    public List<ConstantValue> asList()          { return elements; }
    public Map<String, ConstantValue> asDict()   { return entries; }

    /** Human-readable form used by the resolver report and error-free debugging. */
    public String display() {
        return switch (kind) {
            case INT, FLOAT, BOOL -> String.valueOf(scalar);
            case STRING -> "'" + scalar + "'";
            case NONE -> "None";
            case LIST -> elements.stream().map(ConstantValue::display)
                    .reduce((a, b) -> a + ", " + b).map(s -> "[" + s + "]").orElse("[]");
            case DICT -> entries.entrySet().stream()
                    .map(e -> "'" + e.getKey() + "': " + e.getValue().display())
                    .reduce((a, b) -> a + ", " + b).map(s -> "{" + s + "}").orElse("{}");
            case UNKNOWN -> "unknown";
        };
    }

    @Override
    public String toString() { return display(); }
}
