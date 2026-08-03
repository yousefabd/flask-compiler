package semantic_tests;

import python.symbol_table.CompilerError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Minimal assertion + reporting helper.
 *
 * <p>The project has no test framework on the classpath, so the test suite is
 * a plain {@code main}. Every case compares the <em>complete</em> set of
 * reported error kinds against what it expects, which is what makes an extra
 * false positive — or an unexpected INTERNAL failure — fail the run rather
 * than pass unnoticed.</p>
 */
public final class TestReport {

    private int passed;
    private final List<String> failures = new ArrayList<>();

    /** Runs one case; any exception escaping it is reported as INTERNAL. */
    public void check(String name, Runnable body) {
        try {
            body.run();
            passed++;
            System.out.println("  PASS  " + name);
        } catch (AssertionError failure) {
            failures.add(name + "\n        " + failure.getMessage());
            System.out.println("  FAIL  " + name);
            System.out.println("        " + failure.getMessage().replace("\n", "\n        "));
        } catch (RuntimeException | StackOverflowError unexpected) {
            String detail = unexpected.getClass().getSimpleName()
                    + (unexpected.getMessage() != null ? ": " + unexpected.getMessage() : "");
            failures.add(name + "\n        INTERNAL " + detail);
            System.out.println("  FAIL  " + name);
            System.out.println("        INTERNAL " + detail);
        }
    }

    /**
     * Asserts that {@code errors} contains exactly {@code expected}, in any
     * order and counting duplicates.
     */
    public static void expectKinds(List<CompilerError> errors, CompilerError.Kind... expected) {
        List<String> actualKinds = errors.stream().map(e -> e.getKind().name()).sorted().toList();
        List<String> expectedKinds =
                java.util.Arrays.stream(expected).map(Enum::name).sorted().toList();

        if (!actualKinds.equals(expectedKinds))
            throw new AssertionError(
                    "expected kinds " + expectedKinds + " but got " + actualKinds + describe(errors));
    }

    public static void expectNoErrors(List<CompilerError> errors) {
        if (!errors.isEmpty())
            throw new AssertionError("expected no errors but got " + errors.size() + describe(errors));
    }

    /** Asserts a specific error kind was reported on a specific source line. */
    public static void expectOnLine(List<CompilerError> errors, CompilerError.Kind kind, int line) {
        boolean found = errors.stream()
                .anyMatch(e -> e.getKind() == kind && e.getLine() == line);
        if (!found)
            throw new AssertionError(
                    "expected " + kind + " on line " + line + describe(errors));
    }

    /** Asserts the message of the single reported error mentions {@code fragment}. */
    public static void expectMessageContains(List<CompilerError> errors, String fragment) {
        boolean found = errors.stream().anyMatch(e -> e.getMessage().contains(fragment));
        if (!found)
            throw new AssertionError("expected a message containing \"" + fragment + "\"" + describe(errors));
    }

    public static void expectTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static String describe(List<CompilerError> errors) {
        if (errors.isEmpty()) return "\n    (no errors reported)";
        StringBuilder sb = new StringBuilder("\n    reported:");
        errors.stream()
                .sorted(Comparator.comparingInt(CompilerError::getLine))
                .forEach(e -> sb.append("\n      ").append(e));
        return sb.toString();
    }

    public boolean allPassed() { return failures.isEmpty(); }

    public void printSummary() {
        System.out.println();
        // ASCII: the Windows console this runs on is not UTF-8 by default.
        System.out.println("-".repeat(60));
        System.out.printf("%d passed, %d failed%n", passed, failures.size());
        if (!failures.isEmpty()) {
            System.out.println();
            System.out.println("Failures:");
            for (String failure : failures) System.out.println("  - " + failure);
        }
    }
}
