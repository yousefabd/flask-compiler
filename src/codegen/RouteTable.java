package codegen;

import python.models.atom_statement.StringAtom;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.expr_statement.Condition;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.trailer.Argument;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps a Flask view function's name to its {@code @app.route(...)} URL
 * pattern (e.g. {@code product_details -> "/product/<int:product_id>"}), so
 * the static HTML generator can resolve {@code url_for(...)} calls at
 * compile time instead of leaving them as unresolved runtime calls — which
 * would defeat the point of producing dependency-free static HTML.
 */
public final class RouteTable {

    /** Flask's built-in static-file endpoint isn't declared with @app.route. */
    private static final String STATIC_PATTERN = "/static/<filename>";

    private final Map<String, String> patterns = new LinkedHashMap<>();

    public static RouteTable build(Program program) {
        RouteTable table = new RouteTable();
        table.patterns.put("static", STATIC_PATTERN);
        if (program.statements != null)
            for (Statement st : program.statements)
                table.collect(st);
        return table;
    }

    private void collect(Statement st) {
        if (!(st instanceof DecoratorStatement ds) || ds.function == null || ds.function.id == null) return;
        if (ds.decorators == null) return;

        for (Decorator dec : ds.decorators) {
            if (dec.dottedName == null || dec.dottedName.size() < 2) continue;
            if (!"app".equals(dec.dottedName.get(0).name) || !"route".equals(dec.dottedName.get(1).name)) continue;
            if (dec.arguments == null || dec.arguments.isEmpty()) continue;

            Argument first = dec.arguments.get(0);
            Condition pattern = first.isAssigned() ? null : first.arg; // route path is always positional
            if (pattern instanceof StringAtom sa)
                patterns.put(ds.function.id.name,
                        resolver.PythonLiteralEvaluator.stripPyQuotes(sa.value));
        }
    }

    /** The raw Flask pattern, e.g. {@code /product/<int:product_id>}, or null if unknown. */
    public String patternFor(String endpoint) { return patterns.get(endpoint); }

    /**
     * Resolves {@code url_for(endpoint, k=v, ...)} to a concrete path by
     * substituting each {@code <converter:name>} / {@code <name>} placeholder
     * with the matching keyword argument. Returns null if the endpoint is
     * unknown or a required placeholder has no matching argument.
     */
    public String resolve(String endpoint, Map<String, String> args) {
        String pattern = patterns.get(endpoint);
        if (pattern == null) return null;

        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < pattern.length()) {
            char c = pattern.charAt(i);
            if (c == '<') {
                int end = pattern.indexOf('>', i);
                if (end < 0) { out.append(pattern.substring(i)); break; }
                String token = pattern.substring(i + 1, end);
                String name = token.contains(":") ? token.substring(token.indexOf(':') + 1) : token;
                String value = args.get(name);
                if (value == null) return null; // required path parameter not supplied
                out.append(value);
                i = end + 1;
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }
}
