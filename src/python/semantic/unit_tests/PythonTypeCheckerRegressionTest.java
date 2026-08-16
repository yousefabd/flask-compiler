package python.semantic.unit_tests;

import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.semantic.PythonSemanticResult;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PythonTypeCheckerRegressionTest {
    private static final Path FIXTURES = Path.of("tests", "python", "semantic");

    public static void main(String[] args) {
        validPythonOperationsAndRebindingsRemainConservative();
        definiteAugmentedAndAttributeErrorsAreReportedPrecisely();

        System.out.println("Python type-checker regressions passed.");
    }

    private static void validPythonOperationsAndRebindingsRemainConservative() {
        Analysis analysis = analyze("type_checker_valid_regressions.py");
        require(!analysis.result().hasErrors(),
                "Valid Python operations or rebindings received diagnostics: "
                        + analysis.result().diagnostics());
    }

    private static void definiteAugmentedAndAttributeErrorsAreReportedPrecisely() {
        Analysis analysis = analyze("type_checker_invalid_regressions.py");
        List<CompilerProblem> problems = analysis.result().diagnostics();
        List<CompilerProblem> mismatches = problems.stream()
                .filter(problem -> "TYPE_MISMATCH".equals(problem.getKind()))
                .toList();
        List<CompilerProblem> typeErrors = problems.stream()
                .filter(problem -> "TYPE_ERROR".equals(problem.getKind()))
                .toList();

        require(problems.size() == 8, "Unexpected diagnostics: " + problems);
        require(mismatches.size() == 1 && mismatches.getFirst().getLine() == 2,
                "Invalid augmented assignment was not reported once: " + mismatches);
        require(typeErrors.size() == 7,
                "Known builtin attribute errors were not complete: " + typeErrors);
        require(typeErrors.stream().map(CompilerProblem::getLine)
                        .collect(java.util.stream.Collectors.toSet())
                        .equals(Set.of(5, 7, 9, 11, 13, 15, 17)),
                "Known builtin attribute error lines were inaccurate: " + typeErrors);

        Set<String> unique = new HashSet<>();
        for (CompilerProblem problem : problems) {
            require(problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS,
                    "Wrong diagnostic stage: " + problem);
            require(analysis.source().toString().equals(problem.getFile()),
                    "Wrong diagnostic source: " + problem);
            String key = problem.getKind() + "|" + problem.getLine()
                    + "|" + problem.getMessage();
            require(unique.add(key), "Duplicate diagnostic: " + problem);
        }
    }

    private static Analysis analyze(String filename) {
        Path source = FIXTURES.resolve(filename);
        ErrorReporter reporter = new ErrorReporter();
        PythonFrontend frontend = new PythonFrontend(source, reporter);
        Program program = frontend.parsePython();
        require(program != null,
                "Fixture did not parse: " + source + "\n" + reporter.formatReport());

        PythonSemanticResult result = frontend.analyzePython(program);
        require(result.diagnostics().equals(reporter.getProblems()),
                "Frontend and semantic diagnostics diverged");
        return new Analysis(source, result);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private record Analysis(Path source, PythonSemanticResult result) { }
}
