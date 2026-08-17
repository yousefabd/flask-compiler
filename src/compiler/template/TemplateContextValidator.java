package compiler.template;

import errors.CompilerProblem;
import errors.CompilerStage;
import jinja2.dependency.TemplateDependencyFinder;
import jinja2.models.file.TemplateFile;
import jinja2.semantic.JinjaFreeVariableResult;
import jinja2.semantic.JinjaIncludeSite;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Validates Jinja external names against Flask render_template contexts.
 *
 * <p>The policy intentionally unions the explicit context names from every
 * call for a template. Static includes inherit their including template's
 * union. This avoids false positives for templates whose guarded branches are
 * rendered by different calls, but it does not prove that every individual
 * call supplies every conditionally needed variable.</p>
 */
public final class TemplateContextValidator {

    private final Path templatesDirectory;

    public TemplateContextValidator(Path templatesDirectory) {
        this.templatesDirectory =
                Objects.requireNonNull(templatesDirectory);
    }

    public List<CompilerProblem> validate(
            Map<String, TemplateFile> templates,
            Map<String, JinjaFreeVariableResult> freeVariablesByTemplate,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Objects.requireNonNull(templates);
        Objects.requireNonNull(freeVariablesByTemplate);
        Objects.requireNonNull(callsByTemplate);

        Map<String, Set<String>> contextsByTemplate =
                buildTemplateContexts(
                        templates,
                        freeVariablesByTemplate,
                        callsByTemplate
                );

        List<CompilerProblem> problems =
                new ArrayList<>();

        for (String templateName : templates.keySet()) {
            JinjaFreeVariableResult freeVariables =
                    Objects.requireNonNull(
                            freeVariablesByTemplate.get(templateName),
                            "Missing free-variable result for template '"
                                    + templateName
                                    + "'"
                    );

            Set<String> suppliedVariables =
                    contextsByTemplate.get(templateName);

            for (Map.Entry<String, Integer> external
                    : freeVariables.externalVariables().entrySet()) {
                if (suppliedVariables.contains(external.getKey())) {
                    continue;
                }

                problems.add(
                        new CompilerProblem(
                                CompilerStage.SEMANTIC_ANALYSIS,
                                "MISSING_FLASK_VARIABLE",
                                templatesDirectory
                                        .resolve(templateName)
                                        .normalize()
                                        .toString(),
                                external.getValue(),
                                "Template variable '"
                                        + external.getKey()
                                        + "' is not provided by any "
                                        + "render_template context for '"
                                        + templateName
                                        + "'"
                        )
                );
            }
        }

        return List.copyOf(problems);
    }
    private Map<String, Set<String>> buildTemplateContexts(
            Map<String, TemplateFile> templates,
            Map<String, JinjaFreeVariableResult> freeVariablesByTemplate,
            Map<String, List<TemplateCall>> callsByTemplate
    ) {
        Map<String, Set<String>> contextsByTemplate =
                new LinkedHashMap<>();

        /*
         * First, collect the variables supplied directly through
         * render_template(...).
         */
        for (String templateName : templates.keySet()) {
            Set<String> suppliedVariables =
                    new LinkedHashSet<>();

            for (TemplateCall call
                    : callsByTemplate.getOrDefault(
                    templateName,
                    List.of()
            )) {

                suppliedVariables.addAll(
                        call.contextArguments().keySet()
                );
            }

            contextsByTemplate.put(
                    templateName,
                    suppliedVariables
            );
        }

        /*
         * Then propagate inherited context and visible Jinja locals
         * through static include statements.
         *
         * Repeating this handles nested includes:
         * parent -> middle -> child.
         */
        boolean changed;

        do {
            changed = false;

            for (String includingTemplate
                    : templates.keySet()) {

                Set<String> includingContext =
                        contextsByTemplate.get(
                                includingTemplate
                        );

                JinjaFreeVariableResult freeVariables =
                        Objects.requireNonNull(
                                freeVariablesByTemplate.get(
                                        includingTemplate
                                ),
                                "Missing free-variable result for template '"
                                        + includingTemplate
                                        + "'"
                        );

                for (JinjaIncludeSite includeSite
                        : freeVariables.includeSites()) {

                    Set<String> includedContext =
                            contextsByTemplate.get(
                                    includeSite.templateName()
                            );

                    /*
                     * A missing parsed template should already be reported
                     * by the template frontend.
                     */
                    if (includedContext == null) {
                        continue;
                    }

                    boolean inheritedContextAdded =
                            includedContext.addAll(
                                    includingContext
                            );

                    boolean visibleLocalsAdded =
                            includedContext.addAll(
                                    includeSite.visibleLocals()
                            );

                    if (inheritedContextAdded
                            || visibleLocalsAdded) {
                        changed = true;
                    }
                }
            }
        } while (changed);

        return contextsByTemplate;
    }
}
