package python.semantic;

import python.models.root.Program;
import python.semantic.semantic_rules.ISemanticRule;
import python.semantic.semantic_rules.SemanticContext;
import python.semantic.semantic_rules.TypeCheckerRule;
import python.symbol_table.CompilerError;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs Python semantic analysis in the intended dependency direction:
 *
 * <pre>
 * Python AST
 *     -&gt; name resolution and bindings   (NameResolver)
 *     -&gt; type checking                  (semantic rules)
 *     -&gt; semantic errors
 * </pre>
 *
 * <p>Read-only with respect to the AST and every other compiler stage: it
 * evaluates nothing, interprets nothing, and touches neither rendering nor
 * code generation. It only produces a {@link ResolutionResult} and a list of
 * {@link CompilerError}s.</p>
 */
public final class PythonSemanticAnalyzer {

    private final List<ISemanticRule> rules;

    public PythonSemanticAnalyzer() {
        this(List.of(new TypeCheckerRule()));
    }

    public PythonSemanticAnalyzer(List<ISemanticRule> rules) {
        this.rules = rules;
    }

    public Result analyze(Program program) {
        List<CompilerError> errors = new ArrayList<>();

        ResolutionResult resolution = new NameResolver(errors).resolve(program);

        SemanticContext context = new SemanticContext(program, resolution, errors);
        for (ISemanticRule rule : rules) rule.validate(context);

        return new Result(resolution, errors);
    }

    /** Analysis output: the bindings that were proved, and what went wrong. */
    public record Result(ResolutionResult resolution, List<CompilerError> errors) {
    }
}
