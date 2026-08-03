package python.semantic.semantic_rules;

/**
 * One Python semantic check that runs after name resolution.
 *
 * <p>Mirrors {@code jinja2.symbol_table.semantic_rules.ISemanticRule}.</p>
 */
public interface ISemanticRule {
    void validate(SemanticContext semanticContext);
}
