package tests;

/**
 * Runs every test suite in one go.
 *
 * <pre>
 *   build.bat
 *   run.bat tests.AllTests
 * </pre>
 *
 * <p>Each suite prints its own results; this only totals them and sets the exit
 * code, so a build can gate on a single command. Every suite still runs even when
 * an earlier one fails — a red render suite should not hide the state of the
 * error-handling one.</p>
 */
public final class AllTests {

    public static void main(String[] args) {
        int failures = 0;

        failures += PythonErrorTests.run(args);
        System.out.println();

        failures += StaticRenderTests.run();
        System.out.println();

        System.out.println("==================================");

        if (failures == 0) {
            System.out.println("All suites passed.");
            System.exit(0);
        }

        System.out.printf("%d case(s) failed across all suites.%n", failures);
        System.exit(1);
    }
}
