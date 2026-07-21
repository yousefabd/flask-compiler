package codegen;

import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.enums.Operation;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.SmallStatement;

import resolver.ConstantValue;
import resolver.PythonLiteralEvaluator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extracts the *initial* literal value of every top-level Python variable
 * (e.g. {@code products = [...]} at module scope), for use as the base
 * "context data" static HTML generation resolves templates against.
 *
 * <p><b>Why this is not the same thing as {@code python.resolver.PythonResolver}:</b>
 * that resolver deliberately invalidates a symbol's value the moment it sees
 * <i>any</i> mutation anywhere in the program — including inside a route
 * handler that runs only later, at request time (e.g. {@code products.append(...)}
 * in {@code add_product()}). That is exactly right for its job (proving a
 * value is safe to fold at every point in the program). It is wrong for this
 * job: a static-site generator does not simulate requests, so the only
 * sensible "value" of {@code products} for building the static pages is the
 * one literally written at the top of the file. This class therefore looks
 * ONLY at top-level assignment statements and ignores everything inside
 * function bodies — matching the "Context Data Extraction" phase the project
 * spec calls out as its own step, separate from the Resolver.</p>
 */
public final class ModuleContextExtractor {

    private ModuleContextExtractor() {}

    public static Map<String, ConstantValue> extract(Program program) {
        Map<String, ConstantValue> context = new LinkedHashMap<>();
        if (program.statements == null) return context;

        for (Statement st : program.statements) {
            if (!(st instanceof SimpleStatement ss) || ss.smallStatementList == null) continue;
            for (SmallStatement sm : ss.smallStatementList) {
                if (!(sm instanceof ExpressionStatement es) || es.haveEquals != Operation.EQUALS) continue;
                if (es.conditions == null || es.assigns == null) continue;

                for (int i = 0; i < es.conditions.size() && i < es.assigns.size(); i++) {
                    Condition target = es.conditions.get(i);
                    if (!(target instanceof IDTrailer idt)) continue;
                    if (idt.trailers != null && !idt.trailers.isEmpty()) continue; // not a bare `name = ...`

                    ConstantValue value = PythonLiteralEvaluator.evaluate(es.assigns.get(i));
                    if (value.isKnown())
                        context.put(idt.id.name, value);
                }
            }
        }
        return context;
    }
}
