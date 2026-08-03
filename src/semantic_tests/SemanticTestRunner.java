package semantic_tests;

import compiler.CompilationPipeline;
import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.semantic.MissingFlaskVariableAnalyzer;
import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import jinja2.runtime.RenderEnvironment;
import jinja2.tests.JinjaTestRegistry;
import python.models.root.Program;
import python.symbol_table.CompilerError;
import utils.CompilerSettings;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static python.symbol_table.CompilerError.Kind.DUPLICATE_FUNCTION;
import static python.symbol_table.CompilerError.Kind.DUPLICATE_PARAMETER;
import static python.symbol_table.CompilerError.Kind.MISSING_FLASK_VARIABLE;
import static python.symbol_table.CompilerError.Kind.SCOPE;
import static python.symbol_table.CompilerError.Kind.TYPE_ERROR;
import static python.symbol_table.CompilerError.Kind.TYPE_MISMATCH;
import static python.symbol_table.CompilerError.Kind.UNDEFINED_VARIABLE;
import static python.symbol_table.CompilerError.Kind.USE_BEFORE_ASSIGNMENT;
import static semantic_tests.TestReport.expectKinds;
import static semantic_tests.TestReport.expectMessageContains;
import static semantic_tests.TestReport.expectNoErrors;
import static semantic_tests.TestReport.expectOnLine;
import static semantic_tests.TestReport.expectTrue;

/**
 * The Python semantic-analysis test suite.
 *
 * <p>Run it with:</p>
 * <pre>
 *   javac --release 21 -d build/classes -cp "lib/*" @build/sources.txt
 *   java -cp "build/classes;lib/*" semantic_tests.SemanticTestRunner
 * </pre>
 *
 * <p>Exits with status 1 when any case fails, so it can gate a build.</p>
 */
public final class SemanticTestRunner {

    public static void main(String[] args) {
        TestReport report = new TestReport();

        System.out.println("Undefined variable");
        undefinedVariableTests(report);

        System.out.println("Scope");
        scopeTests(report);

        System.out.println("Use before assignment (NameError)");
        useBeforeAssignmentTests(report);

        System.out.println("Type error");
        typeErrorTests(report);

        System.out.println("Type mismatch");
        typeMismatchTests(report);

        System.out.println("Duplicate declarations");
        duplicateTests(report);

        System.out.println("Missing Flask variable");
        missingFlaskVariableTests(report);

        System.out.println("Whole-project");
        projectTests(report);

        report.printSummary();
        if (!report.allPassed()) System.exit(1);
    }

    // ─────────────────────────────────────────────────────────────
    // UNDEFINED VARIABLE
    // ─────────────────────────────────────────────────────────────

    private static void undefinedVariableTests(TestReport report) {

        report.check("an unresolvable name is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    print(missing_name)
                    """);
            expectKinds(errors, UNDEFINED_VARIABLE);
            expectOnLine(errors, UNDEFINED_VARIABLE, 1);
            expectMessageContains(errors, "missing_name");
        });

        report.check("builtin names are not undefined", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    values = [3, 1, 2]
                    print(len(values))
                    print(str(len(values)))
                    print(int("4"))
                    for index in range(3):
                        print(index)
                    total = sum(values)
                    biggest = max(values)
                    ok = True
                    missing = None
                    """);
            expectNoErrors(errors);
        });

        report.check("a name defined later in the module is visible from a function", () -> {
            // Function bodies run after the module finished executing.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def show():
                        print(configured_later)

                    configured_later = 5
                    """);
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // SCOPE
    // ─────────────────────────────────────────────────────────────

    private static void scopeTests(TestReport report) {

        report.check("a variable local to another function is a scope error", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def create_value():
                        secret = 10

                    print(secret)
                    """);
            expectKinds(errors, SCOPE);
            expectOnLine(errors, SCOPE, 4);
            expectMessageContains(errors, "create_value");
        });

        report.check("one function cannot see another function's local", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def producer():
                        value = 1

                    def consumer():
                        return value
                    """);
            expectKinds(errors, SCOPE);
        });

        report.check("if/for/while do not create a scope", () -> {
            // The old project treated these as scopes. Python does not.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def compute(flag, items):
                        if flag:
                            chosen = 1
                        else:
                            chosen = 2
                        print(chosen)

                        for item in items:
                            last = item
                        print(last)
                        print(item)

                        counter = 0
                        while counter < 3:
                            doubled = counter * 2
                            counter = counter + 1
                        print(doubled)
                    """);
            expectNoErrors(errors);
        });

        report.check("a module-level global stays visible inside a function", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    products = []

                    def add(item):
                        global products
                        products = products + [item]
                        return products
                    """);
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // USE BEFORE ASSIGNMENT  (bonus error #1)
    // ─────────────────────────────────────────────────────────────

    private static void useBeforeAssignmentTests(TestReport report) {

        report.check("reading a local before it is assigned is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def compute():
                        print(total)
                        total = 5
                    """);
            expectKinds(errors, USE_BEFORE_ASSIGNMENT);
            expectOnLine(errors, USE_BEFORE_ASSIGNMENT, 2);
        });

        report.check("use before assignment is distinct from undefined", () -> {
            // `declared` exists but not yet; `never_declared` does not exist at all.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    print(declared)
                    print(never_declared)
                    declared = 1
                    """);
            expectKinds(errors, USE_BEFORE_ASSIGNMENT, UNDEFINED_VARIABLE);
            expectOnLine(errors, USE_BEFORE_ASSIGNMENT, 1);
            expectOnLine(errors, UNDEFINED_VARIABLE, 2);
        });

        report.check("a name assigned later in a loop body is not reported", () -> {
            // On the second iteration `carry` is bound, so this is legal.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def run(items):
                        for item in items:
                            print(carry)
                            carry = item
                    """);
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE ERROR
    // ─────────────────────────────────────────────────────────────

    private static void typeErrorTests(TestReport report) {

        report.check("adding an int and a str is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    value = 5 + "hello"
                    """);
            expectKinds(errors, TYPE_ERROR);
            expectOnLine(errors, TYPE_ERROR, 1);
            expectMessageContains(errors, "'int' and 'str'");
        });

        report.check("indexing an int is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    number = 10
                    first = number[0]
                    """);
            expectKinds(errors, TYPE_ERROR);
            expectMessageContains(errors, "not subscriptable");
        });

        report.check("calling a non-callable is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    total = 5
                    total()
                    """);
            expectKinds(errors, TYPE_ERROR);
            expectMessageContains(errors, "not callable");
        });

        report.check("an unknown runtime type produces no guessed error", () -> {
            // `payload` comes from a call the compiler cannot evaluate, so its
            // type is ANY and no check may fire.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    import os

                    def handle(payload, count):
                        combined = payload + "text"
                        scaled = payload * count
                        item = payload[0]
                        payload()
                        env = os.environ["HOME"]
                        return combined + scaled + item + env
                    """);
            expectNoErrors(errors);
        });

        report.check("valid operations on known types are accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    unit_price = 20
                    quantity = 3
                    total = unit_price * quantity
                    label = "x" * 3
                    greeting = "a" + "b"
                    ratio = 10 / 4
                    joined = [1] + [2]
                    formatted = "%s items" % 4
                    flag = 5 > 3
                    """);
            expectNoErrors(errors);
        });

        report.check("comparing a str with an int is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    name = "abc"
                    limit = 3
                    over = name > limit
                    """);
            expectKinds(errors, TYPE_ERROR);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE MISMATCH
    // ─────────────────────────────────────────────────────────────

    private static void typeMismatchTests(TestReport report) {

        report.check("an annotated parameter given the wrong known type is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(age: int):
                        pass

                    set_age("twenty")
                    """);
            expectKinds(errors, TYPE_MISMATCH);
            expectOnLine(errors, TYPE_MISMATCH, 4);
            expectMessageContains(errors, "expects 'int'");
        });

        report.check("a keyword argument is checked against its annotation", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def register(name: str, age: int):
                        pass

                    register(name="Yousef", age="old")
                    """);
            expectKinds(errors, TYPE_MISMATCH);
        });

        report.check("a correctly typed argument is accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(age: int):
                        pass

                    def set_price(price: float):
                        pass

                    set_age(24)
                    set_price(10)
                    set_price(9.99)
                    """);
            expectNoErrors(errors);
        });

        report.check("an unannotated parameter is never a mismatch", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(age):
                        pass

                    set_age("twenty")
                    """);
            expectNoErrors(errors);
        });

        report.check("an argument of unknown type is not reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(age: int):
                        pass

                    def handler(incoming):
                        set_age(incoming)
                    """);
            expectNoErrors(errors);
        });

        report.check("normal reassignment to a different type is legal Python", () -> {
            // Explicitly not a type mismatch: the first assignment must not
            // fix the variable's type.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    x = 1
                    x = "text"
                    x = [1, 2]
                    print(x)
                    """);
            expectNoErrors(errors);
        });

        report.check("a reassigned variable becomes unknown rather than wrong", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    value = 1
                    value = "text"
                    result = value + 5
                    """);
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // DUPLICATES  (bonus errors #2 and #3 — already present, now covered)
    // ─────────────────────────────────────────────────────────────

    private static void duplicateTests(TestReport report) {

        report.check("a function defined twice is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def handle():
                        pass

                    def handle():
                        pass
                    """);
            expectKinds(errors, DUPLICATE_FUNCTION);
        });

        report.check("a duplicated parameter name is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def handle(value, value):
                        pass
                    """);
            expectKinds(errors, DUPLICATE_PARAMETER);
        });

        report.check("distinct functions and parameters are accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def first(a, b):
                        return a

                    def second(a, b):
                        return b
                    """);
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // MISSING FLASK VARIABLE
    // ─────────────────────────────────────────────────────────────

    private static void missingFlaskVariableTests(TestReport report) {

        report.check("a context variable no route supplies is reported", () -> {
            List<CompilerError> errors = analyzeFlask(
                    """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('page.html', title='Home')
                    """,
                    Map.of("page.html", """
                    <h1>{{ title }}</h1>
                    <p>{{ subtitle }}</p>
                    """));
            expectKinds(errors, MISSING_FLASK_VARIABLE);
            expectMessageContains(errors, "subtitle");
        });

        report.check("loop, set and macro locals are not missing variables", () -> {
            List<CompilerError> errors = analyzeFlask(
                    """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('page.html', items=[])
                    """,
                    Map.of("page.html", """
                    {% set heading = "Items" %}
                    <h1>{{ heading }}</h1>

                    {% macro render_row(label, value) %}
                    <span>{{ label }}: {{ value }}</span>
                    {% endmacro %}

                    <section>
                    {% for item in items %}
                    <p>{{ render_row(item, loop.index) }}</p>
                    {% endfor %}
                    </section>

                    <a href="{{ url_for('index') }}">home</a>
                    """));
            expectNoErrors(errors);
        });

        report.check("a variable supplied by any route satisfies every route", () -> {
            // The same template is rendered from several routes with different
            // context — the conservative rule accepts the union.
            List<CompilerError> errors = analyzeFlask(
                    """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('index.html', page='home')

                    @app.route('/products')
                    def view_products():
                        return render_template('index.html', page='products', products=[])
                    """,
                    Map.of("index.html", """
                    {% if page == 'home' %}
                    <h1>Home</h1>
                    {% else %}
                    {% for product in products %}
                    <p>{{ product }}</p>
                    {% endfor %}
                    {% endif %}
                    """));
            expectNoErrors(errors);
        });

        report.check("'is defined' does not demand a context variable", () -> {
            List<CompilerError> errors = analyzeFlask(
                    """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('page.html')
                    """,
                    Map.of("page.html", """
                    {% if note is defined %}
                    <p>{{ note }}</p>
                    {% endif %}
                    """));
            expectNoErrors(errors);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // WHOLE PROJECT
    // ─────────────────────────────────────────────────────────────

    private static void projectTests(TestReport report) {

        report.check("the real tests/app.py analyzes without false errors", () -> {
            Path appSource = Path.of("tests", "app.py");
            expectTrue(java.nio.file.Files.exists(appSource),
                    "tests/app.py not found — run from the project root");

            Program program = SemanticTestSupport.parseFile(appSource);
            expectNoErrors(SemanticTestSupport.analyzeAll(program));
        });

        report.check("the real templates report no missing Flask variables", () -> {
            Path appSource = Path.of("tests", "app.py");
            Path templatesDirectory = Path.of("tests", "templates");
            expectTrue(java.nio.file.Files.exists(templatesDirectory),
                    "tests/templates not found — run from the project root");

            Program program = SemanticTestSupport.parseFile(appSource);

            List<TemplateCall> calls = TemplateCallFinder.findTemplateCalls(program);
            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(calls);

            ErrorReporter templateReporter = new ErrorReporter();
            Map<String, TemplateFile> parsed = new TemplateFrontend(
                    templatesDirectory, templateReporter, new JinjaTestRegistry())
                    .parseTemplates(callsByTemplate.keySet());

            expectTrue(!templateReporter.hasErrors(),
                    "real templates did not parse: " + templateReporter.formatReport());

            expectNoErrors(MissingFlaskVariableAnalyzer.analyze(parsed, callsByTemplate));
        });

        report.check("semantic errors stop compilation before code generation", () -> {
            Path fixture = Path.of("build", "fixtures", "stops-before-codegen");
            SemanticTestSupport.deleteRecursively(fixture);

            SemanticTestSupport.write(fixture.resolve("app.py"), """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        print(definitely_undefined_name)
                        return render_template('page.html', title='Home')
                    """);
            SemanticTestSupport.write(fixture.resolve("templates").resolve("page.html"),
                    "<h1>{{ title }}</h1>\n");

            RecordingProvider provider = new RecordingProvider();
            runPipeline(fixture, provider, "index");

            expectTrue(!provider.wasCalled,
                    "code generation ran even though the program has a semantic error");

            SemanticTestSupport.deleteRecursively(fixture);
        });

        report.check("a clean program does reach code generation", () -> {
            // Control for the previous case: proves the stop is caused by the
            // errors, not by the pipeline halting unconditionally.
            Path fixture = Path.of("build", "fixtures", "reaches-codegen");
            SemanticTestSupport.deleteRecursively(fixture);

            SemanticTestSupport.write(fixture.resolve("app.py"), """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('page.html', title='Home')
                    """);
            SemanticTestSupport.write(fixture.resolve("templates").resolve("page.html"),
                    "<h1>{{ title }}</h1>\n");

            RecordingProvider provider = new RecordingProvider();
            runPipeline(fixture, provider, "index");

            expectTrue(provider.wasCalled,
                    "a clean program never reached code generation");

            SemanticTestSupport.deleteRecursively(fixture);
        });
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    /**
     * Runs the Python + Jinja analysis needed for the missing-Flask-variable
     * check over an in-memory fixture project.
     */
    private static List<CompilerError> analyzeFlask(String appSource, Map<String, String> templates) {
        Path fixture = Path.of("build", "fixtures", "flask-" + Math.abs(appSource.hashCode()));
        SemanticTestSupport.deleteRecursively(fixture);

        Path templatesDirectory = fixture.resolve("templates");
        templates.forEach((name, content) ->
                SemanticTestSupport.write(templatesDirectory.resolve(name), content));

        Path applicationFile = fixture.resolve("app.py");
        SemanticTestSupport.write(applicationFile, appSource);

        Path previousAppSource = CompilerSettings.appSource;
        Path previousTemplatesDir = CompilerSettings.templatesDir;
        try {
            CompilerSettings.appSource = applicationFile;
            CompilerSettings.templatesDir = templatesDirectory;

            Program program = SemanticTestSupport.parseFile(applicationFile);

            // The fixture's own Python must be clean, or the test is testing
            // the wrong thing.
            List<CompilerError> pythonErrors = SemanticTestSupport.analyzeAll(program);
            expectTrue(pythonErrors.isEmpty(),
                    "fixture app.py has Python errors: " + pythonErrors);

            List<TemplateCall> calls = TemplateCallFinder.findTemplateCalls(program);
            Map<String, List<TemplateCall>> callsByTemplate =
                    TemplateCallFinder.groupTemplateCalls(calls);

            ErrorReporter templateReporter = new ErrorReporter();
            TemplateFrontend frontend = new TemplateFrontend(
                    templatesDirectory, templateReporter, new JinjaTestRegistry());

            Map<String, TemplateFile> parsed =
                    frontend.parseTemplates(callsByTemplate.keySet());

            expectTrue(!templateReporter.hasErrors(),
                    "fixture template did not parse: " + templateReporter.formatReport());

            return MissingFlaskVariableAnalyzer.analyze(parsed, callsByTemplate);
        } finally {
            CompilerSettings.appSource = previousAppSource;
            CompilerSettings.templatesDir = previousTemplatesDir;
            SemanticTestSupport.deleteRecursively(fixture);
        }
    }

    private static void runPipeline(Path fixture, RecordingProvider provider, String function) {
        Path previousAppSource = CompilerSettings.appSource;
        Path previousTemplatesDir = CompilerSettings.templatesDir;
        try {
            CompilerSettings.appSource = fixture.resolve("app.py");
            CompilerSettings.templatesDir = fixture.resolve("templates");
            new CompilationPipeline(provider).compileSnapshot(function);
        } finally {
            CompilerSettings.appSource = previousAppSource;
            CompilerSettings.templatesDir = previousTemplatesDir;
        }
    }

    /** Stands in for CPython so the tests observe whether generation was reached. */
    private static final class RecordingProvider implements TemplateRenderRequestProvider {
        private boolean wasCalled;

        @Override
        public TemplateRenderRequest provide(TemplateCall call) {
            wasCalled = true;
            return new TemplateRenderRequest(
                    call.templateName(),
                    Map.of("title", "Home"),
                    new RenderEnvironment(List.of(), List.of()));
        }
    }
}
