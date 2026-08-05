package jinja2.dependency;

import jinja2.models.TemplateNode;
import jinja2.models.expression.literal.StringLiteralNode;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.IncludeStatementNode;
import utils.CompilerUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Finds template files that can be determined without executing the template.
 * Dynamic include expressions remain in the AST and are evaluated by the
 * renderer, but only string-literal includes can be discovered and parsed
 * during the front-end stage.
 */
public final class TemplateDependencyFinder {

    private TemplateDependencyFinder() {
    }

    public static Set<String> findStaticIncludes(
            TemplateFile template
    ) {
        Objects.requireNonNull(template);

        Set<String> templateNames =
                new LinkedHashSet<>();

        collectStaticIncludes(
                template,
                templateNames
        );

        return templateNames;
    }

    private static void collectStaticIncludes(
            TemplateNode node,
            Set<String> templateNames
    ) {
        if (node instanceof IncludeStatementNode include
                && include.getTemplateExpression()
                instanceof StringLiteralNode stringLiteral) {

            templateNames.add(
                    CompilerUtils.stripStringQuotes(
                            stringLiteral.getValue()
                    )
            );
        }

        for (TemplateNode child : node.getChildren()) {
            collectStaticIncludes(
                    child,
                    templateNames
            );
        }
    }
}