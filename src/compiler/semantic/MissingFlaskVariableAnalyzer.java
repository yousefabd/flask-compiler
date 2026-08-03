package compiler.semantic;

import compiler.template.TemplateCall;
import jinja2.models.file.TemplateFile;
import python.symbol_table.CompilerError;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Compares the free variables a Jinja template needs against the keyword
 * context the Python routes actually pass to {@code render_template(...)}.
 *
 * <pre>
 * Python TemplateCall data + Jinja free-variable data
 *     -&gt; missing Flask variable errors
 * </pre>
 *
 * <p>Both inputs are structured compiler data — {@link TemplateCall} records
 * produced from the Python AST and identifier nodes read from the Jinja AST.
 * No variable name is ever recovered by parsing a human-readable message.</p>
 *
 * <h2>Deliberate limitation</h2>
 * <p>The same template is intentionally rendered from several routes with
 * different {@code page} values and therefore different context arguments —
 * {@code index.html} is rendered by {@code index()}, {@code view_products()},
 * {@code product_details()} and {@code add_product()}, and a variable such as
 * {@code product} is only read inside the branch for one of those pages.
 * Reporting every variable that any individual call fails to supply would
 * therefore be wrong.</p>
 *
 * <p>This first implementation is deliberately conservative: a variable is
 * reported only when <em>no</em> {@code render_template()} call for that
 * template supplies it. It follows that a variable supplied by just one route
 * is accepted for all of them. Narrowing this further needs per-branch
 * reachability analysis of the template against each call's {@code page}
 * value, which is out of scope here.</p>
 */
public final class MissingFlaskVariableAnalyzer {

    private MissingFlaskVariableAnalyzer() {
    }

    /**
     * @param templates       parsed templates, keyed by the name used in the call
     * @param callsByTemplate every render_template call, grouped by template
     * @return one error per (template, missing variable) pair
     */
    public static List<CompilerError> analyze(
            Map<String, TemplateFile> templates,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        List<CompilerError> errors = new ArrayList<>();

        for (Map.Entry<String, TemplateFile> entry : templates.entrySet()) {
            String templateName = entry.getKey();
            TemplateFile template = entry.getValue();

            List<TemplateCall> calls = callsByTemplate.getOrDefault(templateName, List.of());
            if (calls.isEmpty()) continue;   // nothing renders it — nothing to compare against

            Set<String> supplied = suppliedAcrossAllCalls(calls);
            Map<String, Integer> required = TemplateFreeVariableCollector.collect(template);

            int reportLine = calls.getFirst().line();

            for (Map.Entry<String, Integer> variable : required.entrySet()) {
                String name = variable.getKey();
                if (supplied.contains(name)) continue;

                errors.add(new CompilerError(
                        CompilerError.Kind.MISSING_FLASK_VARIABLE,
                        "Template '" + templateName + "' uses variable '" + name
                                + "' (template line " + variable.getValue()
                                + ") but no render_template('" + templateName
                                + "', ...) call supplies it",
                        reportLine));
            }
        }

        return errors;
    }

    private static Set<String> suppliedAcrossAllCalls(List<TemplateCall> calls) {
        Set<String> supplied = new LinkedHashSet<>();
        for (TemplateCall call : calls) supplied.addAll(call.contextArguments().keySet());
        return supplied;
    }
}
