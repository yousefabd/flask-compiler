package errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Central collector for every problem found during a compilation run.
 *
 * <p>Each sub-pipeline keeps its own native error type
 * ({@code jinja2.symbol_table.CompilerError}, {@code python.symbol_table.CompilerError});
 * the pipeline driver funnels them into this reporter, which prints one
 * consistent report and decides whether code generation may proceed.</p>
 */
public class ErrorReporter {

    private final List<CompilerProblem> problems = new ArrayList<>();

    public void report(CompilerProblem problem) {
        problems.add(problem);
    }

    public void report(CompilerException exception) {
        problems.add(exception.toProblem());
    }

    /** Adopts a Jinja2 semantic error, preserving its kind and line. */
    public void report(String file, jinja2.symbol_table.CompilerError error) {
        problems.add(new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS,
                error.getKind().name(),
                file,
                error.getLine(),
                error.getMessage()));
    }

    /**
     * Adopts a Python semantic error, preserving its kind, line and scope.
     *
     * <p>Python kinds print under their error name ({@code UndefinedError},
     * {@code ScopeError}, {@code TypeMismatchError}, ...) so the report reads the
     * way Python itself names these failures.</p>
     */
    public void report(String file, python.symbol_table.CompilerError error) {
        problems.add(new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS,
                error.getKind().errorName(),
                file,
                error.getLine(),
                error.getMessage(),
                error.getContext()));
    }

    /** Adopts a whole batch of Python semantic errors. */
    public void reportAll(String file, List<python.symbol_table.CompilerError> errors) {
        for (python.symbol_table.CompilerError error : errors)
            report(file, error);
    }

    /** Wraps an unexpected exception so it is reported instead of crashing. */
    public void reportUnexpected(CompilerStage stage, String file, Throwable t) {
        String message = t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName();
        problems.add(new CompilerProblem(stage, "INTERNAL", file, -1,
                "Unexpected error: " + message));
    }

    public boolean hasErrors() {
        return !problems.isEmpty();
    }

    public List<CompilerProblem> getProblems() {
        return problems;
    }

    /** Prints all collected problems grouped by stage, in the existing display format. */
    public void printReport() {
        System.out.println(formatReport());
    }

    /** Same content as {@link #printReport()}, as a string — reused by generated report files. */
    public String formatReport() {
        if (problems.isEmpty())
            return "No errors.";

        StringBuilder sb = new StringBuilder();
        for (CompilerStage stage : CompilerStage.values()) {
            List<CompilerProblem> inStage = new ArrayList<>();
            for (CompilerProblem p : problems)
                if (p.getStage() == stage) inStage.add(p);
            if (inStage.isEmpty()) continue;

            sb.append(stageTitle(stage)).append(":\n");
            for (CompilerProblem p : inStage)
                sb.append("  ").append(p).append('\n');
        }
        return sb.toString();
    }

    private static String stageTitle(CompilerStage stage) {
        return switch (stage) {
            case PARSING -> "Syntax Errors";
            case SEMANTIC_ANALYSIS -> "Semantic Errors";
            case CODE_GENERATION -> "Code Generation Errors";
            case IO -> "I/O Errors";
            default -> stage.name();
        };
    }
}
