package errors;

/** The pipeline stage an error was produced in. Used for grouping/reporting. */
public enum CompilerStage {
    PARSING,
    SEMANTIC_ANALYSIS,
    CODE_GENERATION,
    IO,
    PYTHON_EXECUTION
}
