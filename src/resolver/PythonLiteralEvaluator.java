package resolver;

import python.models.atom_statement.*;
import python.models.expr_statement.Condition;
import python.models.expr_statement.Expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Best-effort literal evaluation of a Python expression into a
 * {@link ConstantValue} — shared by {@code python.resolver.PythonResolver}
 * (constant tracking during whole-program resolution) and
 * {@code codegen.ModuleContextExtractor} (initial-value extraction for
 * static HTML generation), so the two don't duplicate the same atom-matching
 * logic.
 */
public final class PythonLiteralEvaluator {

    private PythonLiteralEvaluator() {}

    /** Returns {@link ConstantValue#unknown()} for anything not provably a literal. */
    public static ConstantValue evaluate(Condition value) {
        if (value instanceof IntegerAtom ia) return ConstantValue.ofInt(ia.value);
        if (value instanceof FloatAtom fa)   return ConstantValue.ofFloat(fa.value);
        if (value instanceof BoolAtom ba)    return ConstantValue.ofBool(ba.value);
        if (value instanceof StringAtom sa)  return ConstantValue.ofString(stripPyQuotes(sa.value));
        if (value instanceof None)           return ConstantValue.none();
        if (value instanceof ParenAtom pa)   return evaluate(pa.inner);

        if (value instanceof python.models.atom_statement.List la) {
            List<ConstantValue> items = new ArrayList<>();
            if (la.content != null)
                for (Expression e : la.content) {
                    ConstantValue v = evaluate(e);
                    if (!v.isKnown()) return ConstantValue.unknown();
                    items.add(v);
                }
            return ConstantValue.ofList(items);
        }
        if (value instanceof Dictionary dict) {
            Map<String, ConstantValue> map = new LinkedHashMap<>();
            if (dict.keys != null)
                for (int i = 0; i < dict.keys.size(); i++) {
                    ConstantValue k = evaluate(dict.keys.get(i));
                    ConstantValue v = evaluate(dict.values.get(i));
                    if (!v.isKnown() || k.getKind() != ConstantValue.Kind.STRING)
                        return ConstantValue.unknown();
                    map.put(k.asString(), v);
                }
            return ConstantValue.ofDict(map);
        }
        return ConstantValue.unknown(); // calls, binary ops, identifiers, ... — not provable here
    }

    public static String stripPyQuotes(String raw) {
        for (String q : new String[]{"'''", "\"\"\"", "'", "\""})
            if (raw.length() >= 2 * q.length() && raw.startsWith(q) && raw.endsWith(q))
                return raw.substring(q.length(), raw.length() - q.length());
        return raw;
    }
}
