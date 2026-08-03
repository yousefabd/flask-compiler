package python.symbol_table.semantic_rules;

/**
 * A single Python semantic check that runs after the symbol table has been
 * built and every identifier has been resolved.
 *
 * <p>Same contract as {@code jinja2.symbol_table.semantic_rules.ISemanticRule}:
 * a rule never throws and never prints — it appends
 * {@link python.symbol_table.CompilerError}s to the context, and the frontend
 * hands them to {@link errors.ErrorReporter}.</p>
 */
public interface ISemanticRule {
    void validate(SemanticContext semanticContext);
}
