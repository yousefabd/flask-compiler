package python.semantic.unit_tests;

import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.semantic.PythonSemanticResult;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PythonGlobalDeclarationValidatorTest {
    private static final Path SOURCE = Path.of(
            "tests", "python", "semantic", "global_declaration_order.py"
    );

    public static void main(String[] args) {
        rejectsUsesAndBindingsBeforeGlobal();
        System.out.println("Python global declaration validation passed.");
    }

    private static void rejectsUsesAndBindingsBeforeGlobal() {
        ErrorReporter reporter = new ErrorReporter();
        PythonFrontend frontend = new PythonFrontend(SOURCE, reporter);
        Program program = frontend.parsePython();
        require(
                program != null,
                "Fixture did not parse: " + reporter.formatReport()
        );

        PythonSemanticResult result = frontend.analyzePython(program);
        List<CompilerProblem> problems = result.diagnostics();
        require(
                problems.equals(reporter.getProblems()),
                "Frontend and semantic-result diagnostics diverged"
        );

        Map<String, Integer> expectedLines = new LinkedHashMap<>();
        expectedLines.put("parameter_name", 11);
        expectedLines.put("os", 11);
        expectedLines.put("assigned_name", 11);
        expectedLines.put("loop_name", 11);
        expectedLines.put("read_name", 11);
        expectedLines.put("object_name", 11);
        expectedLines.put("branch_condition", 16);
        expectedLines.put("iterable_name", 18);
        expectedLines.put("while_condition", 20);
        expectedLines.put("inner_missing", 32);

        require(
                problems.size() == expectedLines.size(),
                "Global-order diagnostics cascaded or were missed: " + problems
        );
        verifyProblems(problems, expectedLines);
    }

    private static void verifyProblems(
            List<CompilerProblem> problems,
            Map<String, Integer> expectedLines
    ) {
        Set<String> reportedNames = new HashSet<>();
        String prefix = "Name ";
        String suffix =
                " is used or assigned before its global declaration";

        for (CompilerProblem problem : problems) {
            require(
                    "SCOPE".equals(problem.getKind()),
                    "Global-order error used the wrong category: " + problem
            );
            require(
                    problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS,
                    "Global-order error used the wrong stage: " + problem
            );
            require(
                    SOURCE.toString().equals(problem.getFile()),
                    "Global-order error used the wrong file: " + problem
            );
            require(
                    problem.getMessage().startsWith(prefix)
                            && problem.getMessage().endsWith(suffix),
                    "Unexpected global-order message: " + problem
            );

            String name = problem.getMessage().substring(
                    prefix.length(),
                    problem.getMessage().length() - suffix.length()
            );
            Integer expectedLine = expectedLines.get(name);
            require(
                    expectedLine != null,
                    "Unexpected global-order name: " + name
            );
            require(
                    problem.getLine() == expectedLine,
                    "Wrong global declaration line for " + name
                            + ": " + problem.getLine()
            );
            require(
                    reportedNames.add(name),
                    "Duplicate global-order diagnostic for " + name
            );
        }

        require(
                reportedNames.equals(expectedLines.keySet()),
                "Missing global-order diagnostics: " + reportedNames
        );
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
