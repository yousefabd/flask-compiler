package python.semantic.semantic_rules;

import python.models.root.Program;
import python.semantic.ResolutionResult;
import python.symbol_table.CompilerError;

import java.util.List;

/**
 * Everything a Python semantic rule may read.
 *
 * <p>Mirrors {@code jinja2.symbol_table.semantic_rules.SemanticContext}. The
 * program and resolution result are read-only inputs; {@code errors} is the
 * only output channel.</p>
 */
public record SemanticContext(
        Program root,
        ResolutionResult resolution,
        List<CompilerError> errors
) {
}
