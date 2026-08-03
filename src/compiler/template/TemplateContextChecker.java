package compiler.template;

import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import python.symbol_table.CompilerError;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checks that every variable a template reads is actually handed to it by the
 * backend, and reports what is missing as a Python-side
 * {@code MissingFlaskVariableError}.
 *
 * <p>The same gap is visible from both ends of the pipeline. The Jinja analysis
 * reports it as an undefined variable at the line <i>inside the template</i>; this
 * checker reports it at the {@code render_template(...)} call <i>in app.py</i>, which
 * is where the fix has to be made.</p>
 *
 * <p>A name counts as supplied when <b>any</b> {@code render_template} call for that
 * template passes it — matching how {@code CompilationPipeline} unions the context of
 * all calls before analyzing a template. A template rendered from several routes is
 * therefore only flagged for names no route supplies at all.</p>
 */
public final class TemplateContextChecker {

    private TemplateContextChecker() {}

    /**
     * @param templates       parsed templates, keyed by template name
     * @param callsByTemplate every render_template call, grouped by template name
     * @return one error per (template, missing variable), in discovery order
     */
    public static List<CompilerError> findMissingContextVariables(
            TemplateFrontend templateFrontend,
            Map<String, TemplateFile> templates,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        List<CompilerError> errors = new ArrayList<>();

        for (Map.Entry<String, TemplateFile> entry : templates.entrySet()) {
            String templateName = entry.getKey();

            List<TemplateCall> calls =
                    callsByTemplate.getOrDefault(templateName, List.of());

            if (calls.isEmpty()) continue;

            Set<String> required =
                    templateFrontend.collectRequiredContextVariables(entry.getValue());

            if (required.isEmpty()) continue;

            Set<String> supplied = new LinkedHashSet<>();
            for (TemplateCall call : calls)
                supplied.addAll(call.contextArguments().keySet());

            TemplateCall firstCall = calls.get(0);

            for (String name : required) {
                if (supplied.contains(name)) continue;

                errors.add(new CompilerError(
                        CompilerError.Kind.MISSING_FLASK_VARIABLE,
                        "'" + name + "' was not passed to render_template('"
                                + templateName + "')",
                        firstCall.line(),
                        "function '" + firstCall.ownerFunctionName() + "'",
                        name));
            }
        }

        return errors;
    }
}
