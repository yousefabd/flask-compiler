package tests;

import compiler.CompilationPipeline;
import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import compiler.template.TemplateContextChecker;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import jinja2.tests.JinjaTestRegistry;
import python.PythonFrontend;
import python.models.root.Program;
import utils.CompilerSettings;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test suite for the Python error handling.
 *
 * <p>Every case feeds one small program from {@code tests/errors/} through the real
 * front end and then checks three things, which are exactly the three promises the
 * error handling makes:</p>
 *
 * <ol>
 *   <li>the compiler <b>does not crash</b> — any escaping exception fails the case;</li>
 *   <li>the expected error <b>appears in the report</b>, with its error name and message;</li>
 *   <li>it appears in the <b>correct section</b> of the report ("Semantic Errors").</li>
 * </ol>
 *
 * <p>Run it with:</p>
 * <pre>
 *   build.bat
 *   run.bat tests.PythonErrorTests
 *   run.bat tests.PythonErrorTests --show    (also prints every report)
 * </pre>
 *
 * <p>Exits with status 0 when every case passes and 1 otherwise, so it can be wired
 * into a build.</p>
 */
public final class PythonErrorTests {

    private static final Path ERRORS_DIR = Path.of("tests", "errors");
    private static final String SEMANTIC_SECTION = "Semantic Errors";

    /**
     * @param name        what the case demonstrates
     * @param sourceFile  file under tests/errors/
     * @param errorName   the error name the report must show, e.g. {@code UndefinedError}
     * @param messagePart a distinctive fragment of the expected message
     */
    private record ErrorCase(String name, String sourceFile,
                             String errorName, String messagePart) {}

    private static final List<ErrorCase> CASES = List.of(
            // ── the six required errors ─────────────────────────────
            new ErrorCase("Undefined variable",
                    "undefined.py", "UndefinedError",
                    "Variable 'x' is not defined"),

            new ErrorCase("Type error in a binary operation",
                    "type_error.py", "TypeError",
                    "Unsupported operand types for +: int and str"),

            new ErrorCase("Variable used outside its scope",
                    "scope.py", "ScopeError",
                    "Variable 'x' is out of scope here"),

            new ErrorCase("Type mismatch on reassignment",
                    "type_mismatch.py", "TypeMismatchError",
                    "Expected int, got str for 'x'"),

            new ErrorCase("Use before declaration",
                    "name_error.py", "NameError",
                    "Variable 'value' is used before it is declared at line 2"),

            // ── duplicate declarations ──────────────────────────────
            new ErrorCase("Duplicate variable declaration",
                    "duplicate_declaration.py", "DuplicateDeclarationError",
                    "Variable 'x' is already declared in this scope"),

            new ErrorCase("Duplicate function definition",
                    "duplicate_function.py", "DuplicateFunctionError",
                    "Function 'handler' is already defined"),

            new ErrorCase("Duplicate function parameter",
                    "duplicate_parameter.py", "DuplicateParameterError",
                    "Duplicate parameter 'a' in function 'f'"),

            // ── bonus checks ────────────────────────────────────────
            new ErrorCase("Duplicate Flask route",
                    "duplicate_route.py", "DuplicateRouteError",
                    "Route '/products' is already handled by 'view_products'"),

            new ErrorCase("Wrong number of call arguments",
                    "argument_count.py", "ArgumentCountError",
                    "Function 'add' expects 2 argument(s), but 1 were given"),

            new ErrorCase("Calling a value that is not callable",
                    "not_callable.py", "TypeError",
                    "'total' of type int is not callable"),

            new ErrorCase("Argument type contradicts the annotation",
                    "argument_type.py", "TypeMismatchError",
                    "Expected str, got int for parameter 'name' of 'greet'"),

            new ErrorCase("Index access on a non-indexable value",
                    "index_access.py", "TypeError",
                    "Type int does not support index access"),

            new ErrorCase("Augmented assignment to an unknown name",
                    "augmented_undefined.py", "UndefinedError",
                    "Variable 'counter' is not defined")
    );

    // added: `--show` prints the compiler report each case produced, so the exact
    // output a user would see in the report area can be read at a glance.
    private static boolean show;

    public static void main(String[] args) {
        System.exit(run(args) == 0 ? 0 : 1);
    }

    /** Runs every case. @return the number that failed, so {@link AllTests} can total them up. */
    public static int run(String[] args) {
        show = args.length > 0 && args[0].equals("--show");

        List<String> failures = new ArrayList<>();
        int total = 0;

        System.out.println("Python error handling — test suite");
        System.out.println("==================================");
        System.out.println();

        for (ErrorCase testCase : CASES) {
            total++;
            String failure = runErrorCase(testCase);
            report(testCase.name(), failure, failures);

            if (show) {
                System.out.println("        source: " + ERRORS_DIR.resolve(testCase.sourceFile()));
                System.out.println(indent(analyze(ERRORS_DIR.resolve(testCase.sourceFile()))));
                System.out.println();
            }
        }

        total++;
        report("Missing Flask variable",
                runMissingFlaskVariableCase(), failures);

        total++;
        report("A correct program reports nothing",
                runCleanProgramCase(), failures);

        total++;
        report("The whole pipeline survives a broken app",
                runPipelineCase(), failures);

        System.out.println();
        System.out.printf("%d/%d passed.%n", total - failures.size(), total);

        if (!failures.isEmpty()) {
            System.out.println();
            System.out.println("Failures:");
            for (String failure : failures)
                System.out.println("  " + failure);
        }

        return failures.size();
    }

    // ─────────────────────────────────────────────────────────────
    // CASES
    // ─────────────────────────────────────────────────────────────

    /** @return null when the case passes, otherwise the reason it failed */
    private static String runErrorCase(ErrorCase testCase) {
        Path source = ERRORS_DIR.resolve(testCase.sourceFile());

        String report;

        try {
            report = analyze(source);
        } catch (RuntimeException crash) {
            return "the compiler crashed with "
                    + crash.getClass().getSimpleName() + ": " + crash.getMessage();
        }

        return checkReport(report, testCase.errorName(), testCase.messagePart());
    }

    /**
     * The backend/template contract: {@code missing_variable.html} reads
     * {@code name} and {@code products}, the route passes only {@code name}.
     */
    private static String runMissingFlaskVariableCase() {
        Path source = ERRORS_DIR.resolve("missing_flask_variable.py");

        ErrorReporter reporter = new ErrorReporter();
        String report;

        try {
            PythonFrontend pythonFrontend = new PythonFrontend(source, reporter);

            Program program = pythonFrontend.parsePython();

            if (program == null)
                return "the sample program did not parse";

            pythonFrontend.analyzePython(program);

            List<TemplateCall> calls =
                    TemplateCallFinder.findTemplateCalls(program);

            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(calls);

            TemplateFrontend templateFrontend =
                    new TemplateFrontend(
                            ERRORS_DIR.resolve("templates"),
                            reporter,
                            new JinjaTestRegistry());

            Map<String, TemplateFile> templates =
                    templateFrontend.parseTemplates(callsByTemplate.keySet());

            reporter.reportAll(
                    source.toString(),
                    TemplateContextChecker.findMissingContextVariables(
                            templateFrontend, templates, callsByTemplate));

            report = reporter.formatReport();

        } catch (RuntimeException crash) {
            return "the compiler crashed with "
                    + crash.getClass().getSimpleName() + ": " + crash.getMessage();
        }

        if (show) {
            System.out.println("        source: " + source);
            System.out.println(indent(report));
            System.out.println();
        }

        String failure = checkReport(report, "MissingFlaskVariableError",
                "'products' was not passed to render_template('missing_variable.html')");

        if (failure != null) return failure;

        // `name` IS passed, so it must not be reported.
        if (report.contains("'name' was not passed"))
            return "reported 'name' as missing even though the route passes it";

        return null;
    }

    /** The sample app patterns must stay silent — no false positives. */
    private static String runCleanProgramCase() {
        String report;

        try {
            report = analyze(ERRORS_DIR.resolve("clean.py"));
        } catch (RuntimeException crash) {
            return "the compiler crashed with "
                    + crash.getClass().getSimpleName() + ": " + crash.getMessage();
        }

        if (!report.strip().equals("No errors."))
            return "expected a clean report but got:\n" + indent(report);

        return null;
    }

    /**
     * End-to-end check of requirement "do not crash": runs the real
     * {@link compiler.CompilationPipeline} over an app with two Python errors and
     * confirms that it returns normally, prints "Compilation failed:" and lists both
     * errors — instead of throwing out of the pipeline.
     */
    private static String runPipelineCase() {
        Path savedSource = CompilerSettings.appSource;
        Path savedTemplates = CompilerSettings.templatesDir;
        PrintStream savedOut = System.out;

        ByteArrayOutputStream captured = new ByteArrayOutputStream();

        try {
            CompilerSettings.appSource = ERRORS_DIR.resolve("broken_app.py");
            CompilerSettings.templatesDir = Path.of("tests", "templates");

            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

            // Generation is never reached — the run stops at the semantic errors.
            new CompilationPipeline(call -> {
                throw new IllegalStateException(
                        "code generation must not run for a program with errors");
            }).compileSnapshot("index");

        } catch (RuntimeException crash) {
            System.setOut(savedOut);
            return "the pipeline crashed with "
                    + crash.getClass().getSimpleName() + ": " + crash.getMessage();
        } finally {
            System.setOut(savedOut);
            CompilerSettings.appSource = savedSource;
            CompilerSettings.templatesDir = savedTemplates;
        }

        String output = captured.toString(StandardCharsets.UTF_8);

        if (show) {
            System.out.println("        source: " + ERRORS_DIR.resolve("broken_app.py"));
            System.out.println(indent(
                    output.substring(Math.max(0, output.indexOf("Compilation failed:")))));
            System.out.println();
        }

        if (!output.contains("Compilation failed:"))
            return "expected the pipeline to report a failed compilation, got:\n"
                    + indent(output);

        if (!output.contains("[UndefinedError]")
                || !output.contains("missing_name"))
            return "expected the undefined render_template argument to be reported, got:\n"
                    + indent(output);

        if (!output.contains("[TypeMismatchError]")
                || !output.contains("Expected str, got int for 'title'"))
            return "expected the reassignment type mismatch to be reported, got:\n"
                    + indent(output);

        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    /** Runs the real front end over one file and returns the formatted report. */
    private static String analyze(Path source) {
        ErrorReporter reporter = new ErrorReporter();

        PythonFrontend frontend = new PythonFrontend(source, reporter);

        Program program = frontend.parsePython();

        if (program != null)
            frontend.analyzePython(program);

        return reporter.formatReport();
    }

    /**
     * Verifies that the report mentions the expected error, with the expected text,
     * inside the Semantic Errors section.
     *
     * @return null when everything matches, otherwise the reason it did not
     */
    private static String checkReport(String report, String errorName, String messagePart) {
        if (report.strip().equals("No errors."))
            return "expected " + errorName + " but the report was empty";

        String section = sectionOf(report, SEMANTIC_SECTION);

        if (section == null)
            return "expected a \"" + SEMANTIC_SECTION + "\" section, got:\n" + indent(report);

        if (!section.contains("[" + errorName + "]"))
            return "expected [" + errorName + "] in the "
                    + SEMANTIC_SECTION + " section, got:\n" + indent(section);

        if (!section.contains(messagePart))
            return "expected the message to contain \"" + messagePart + "\", got:\n"
                    + indent(section);

        return null;
    }

    /**
     * The lines the reporter printed under one section heading. Sections are the
     * "Syntax Errors:" / "Semantic Errors:" / ... blocks {@code ErrorReporter}
     * groups its problems into; their entries are the indented lines below.
     */
    private static String sectionOf(String report, String heading) {
        String[] lines = report.split("\\R");

        StringBuilder section = new StringBuilder();
        boolean inSection = false;

        for (String line : lines) {
            if (line.startsWith(heading + ":")) {
                inSection = true;
                continue;
            }

            // a non-indented line starts the next section
            if (inSection && !line.startsWith(" ")) break;

            if (inSection) section.append(line).append('\n');
        }

        return inSection ? section.toString() : null;
    }

    private static void report(String name, String failure, List<String> failures) {
        if (failure == null) {
            System.out.println("  PASS  " + name);
            return;
        }

        System.out.println("  FAIL  " + name + " — " + failure);
        failures.add(name + " — " + failure);
    }

    private static String indent(String text) {
        return text.strip().replaceAll("(?m)^", "        ");
    }
}
