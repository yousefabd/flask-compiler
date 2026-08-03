package python.symbol_table.semantic_rules;

import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.atom_statement.StringAtom;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.trailer.Argument;
import python.symbol_table.CompilerError;
import utils.CompilerUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Flask-specific declaration rule: two routes may not answer the same URL rule.
 *
 * <p>Werkzeug rejects this at import time with
 * {@code View function mapping is overwriting an existing endpoint}, so catching it
 * statically saves a run. Reported as {@code DuplicateRouteError}.</p>
 */
public class FlaskRouteRule implements ISemanticRule {

    @Override
    public void validate(SemanticContext context) {
        Map<String, String> routeOwners = new LinkedHashMap<>();
        walk(context.root(), routeOwners, context);
    }

    private void walk(ASTNode node, Map<String, String> routeOwners, SemanticContext context) {
        if (node == null) return;

        if (node instanceof DecoratorStatement decorated)
            checkRoutes(decorated, routeOwners, context);

        for (ASTNode child : node.getChildren())
            walk(child, routeOwners, context);
    }

    private void checkRoutes(DecoratorStatement decorated,
                             Map<String, String> routeOwners,
                             SemanticContext context) {
        if (decorated.decorators == null) return;

        String functionName =
                decorated.function != null && decorated.function.id != null
                        ? decorated.function.id.name
                        : "<anonymous>";

        for (Decorator decorator : decorated.decorators) {
            String rule = routeRule(decorator);
            if (rule == null) continue;

            String existingOwner = routeOwners.get(rule);

            if (existingOwner != null) {
                context.error(
                        CompilerError.Kind.DUPLICATE_ROUTE,
                        "Route '" + rule + "' is already handled by '" + existingOwner + "'",
                        decorator.getLine(),
                        "function '" + functionName + "'",
                        rule);
                continue;
            }

            routeOwners.put(rule, functionName);
        }
    }

    /** The URL rule of an {@code @app.route("/path")} decorator, or null for other decorators. */
    private static String routeRule(Decorator decorator) {
        if (decorator.dottedName == null || decorator.dottedName.isEmpty()) return null;

        ID lastName = decorator.dottedName.get(decorator.dottedName.size() - 1);

        if (!"route".equals(lastName.name)) return null;
        if (decorator.arguments == null || decorator.arguments.isEmpty()) return null;

        Argument first = decorator.arguments.get(0);

        if (first.isAssigned() || !(first.arg instanceof StringAtom path)) return null;

        return CompilerUtils.stripStringQuotes(path.value);
    }
}
