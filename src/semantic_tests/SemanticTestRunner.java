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

import static python.symbol_table.CompilerError.Kind.BREAK_OUTSIDE_LOOP;
import static python.symbol_table.CompilerError.Kind.CONTINUE_OUTSIDE_LOOP;
import static python.symbol_table.CompilerError.Kind.DUPLICATE_PARAMETER;
import static python.symbol_table.CompilerError.Kind.MISSING_FLASK_VARIABLE;
import static python.symbol_table.CompilerError.Kind.RETURN_OUTSIDE_FUNCTION;
import static python.symbol_table.CompilerError.Kind.SCOPE;
import static python.symbol_table.CompilerError.Kind.TYPE_ERROR;
import static python.symbol_table.CompilerError.Kind.TYPE_MISMATCH;
import static python.symbol_table.CompilerError.Kind.UNDEFINED_VARIABLE;
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
 *
 * <p>This suite was revised after an adversarial review found several
 * confirmed defects (see {@code docs/SEMANTIC_ANALYSIS_CHANGES.md} for the
 * full list): a crash on {@code from x import *}, two legacy checks that
 * flagged legal Python as an error, a flow-insensitivity error kind that was
 * wrong in both directions and was removed rather than patched, an incorrect
 * function/variable rebinding model, function annotations looked up by name
 * instead of by resolved binding, several confirmed type-checker gaps, a
 * dotted-import binding bug, and a case where the integrated pipeline
 * double-reported one problem under two different error kinds. Each fix has
 * a dedicated regression test below, grouped under "Adversarial regressions".</p>
 */
public final class SemanticTestRunner {

    public static void main(String[] args) {
        TestReport report = new TestReport();

        System.out.println("Undefined variable");
        undefinedVariableTests(report);

        System.out.println("Scope");
        scopeTests(report);

        System.out.println("Flow-insensitive resolution (documented limitation)");
        flowInsensitivityTests(report);

        System.out.println("Type error");
        typeErrorTests(report);

        System.out.println("Type mismatch");
        typeMismatchTests(report);

        System.out.println("Legacy declaration-placement checks (bonus errors)");
        declarationPlacementTests(report);

        System.out.println("Missing Flask variable");
        missingFlaskVariableTests(report);

        System.out.println("Adversarial regressions (post-review)");
        adversarialRegressionTests(report);

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
    // FLOW-INSENSITIVE RESOLUTION
    //
    // A name declared anywhere in a scope resolves everywhere in that scope,
    // regardless of textual position. This under-approximates real CPython,
    // which can raise UnboundLocalError / NameError for a read that precedes
    // its assignment on the branch/iteration actually taken. Reporting that
    // correctly needs control-flow analysis (which branch of an `if` ran,
    // which loop iteration). An earlier version approximated it with
    // execution-order tracking and got it wrong in both directions — see
    // docs/SEMANTIC_ANALYSIS_CHANGES.md. Rather than ship a check that is
    // confidently wrong on findable inputs, the distinction is not attempted.
    // ─────────────────────────────────────────────────────────────

    private static void flowInsensitivityTests(TestReport report) {

        report.check("a name read before its assignment in the same function is accepted", () -> {
            // A real CPython UnboundLocalError. Deliberately not reported —
            // see the section comment above.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def compute():
                        print(total)
                        total = 5
                    """);
            expectNoErrors(errors);
        });

        report.check("a name assigned only inside one loop iteration is accepted on read", () -> {
            // A real CPython UnboundLocalError on the first iteration.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def run(items):
                        for item in items:
                            print(carry)
                            carry = item
                    """);
            expectNoErrors(errors);
        });

        report.check("a name assigned only inside one 'if' branch is accepted after it", () -> {
            // A real CPython UnboundLocalError whenever `flag` is falsy.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def compute(flag):
                        if flag:
                            value = 1
                        print(value)
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

        report.check("a return value contradicting its return annotation is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def get_id() -> int:
                        return "x"
                    """);
            expectKinds(errors, TYPE_MISMATCH);
            expectMessageContains(errors, "return annotation");
        });

        report.check("a return value matching its return annotation is accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def get_id() -> int:
                        return 5
                    """);
            expectNoErrors(errors);
        });

        report.check("a bare return under a return annotation is not checked", () -> {
            // Bare `return` implies None; checking that against a concrete
            // annotation like `-> int` would need Optional-aware annotations
            // this grammar's bare-name annotations cannot express.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def maybe_get(flag) -> int:
                        if flag:
                            return 5
                        return
                    """);
            expectNoErrors(errors);
        });

        report.check("a default value contradicting its parameter annotation is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(x: int = "x"):
                        pass
                    """);
            expectKinds(errors, TYPE_MISMATCH);
            expectOnLine(errors, TYPE_MISMATCH, 1);
            expectMessageContains(errors, "default value");
        });

        report.check("a default value matching its parameter annotation is accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def set_age(x: int = 5):
                        pass
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
    // LEGACY DECLARATION-PLACEMENT CHECKS  (bonus errors)
    //
    // All four are real CPython SyntaxErrors, already implemented in
    // python.symbol_table.SymbolTableBuilder before this work started; they
    // had no test coverage. Two siblings that were also in that builder —
    // reporting a redefined function, and reporting `global` at module level
    // — were removed after adversarial review confirmed both flag completely
    // legal Python (see the "rebinding" and "declaration placement"
    // regressions below).
    // ─────────────────────────────────────────────────────────────

    private static void declarationPlacementTests(TestReport report) {

        report.check("a duplicated parameter name is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def handle(value, value):
                        pass
                    """);
            expectKinds(errors, DUPLICATE_PARAMETER);
        });

        report.check("'return' outside a function is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    return 5
                    """);
            expectKinds(errors, RETURN_OUTSIDE_FUNCTION);
        });

        report.check("'break' outside a loop is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    break
                    """);
            expectKinds(errors, BREAK_OUTSIDE_LOOP);
        });

        report.check("'continue' outside a loop is reported", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    continue
                    """);
            expectKinds(errors, CONTINUE_OUTSIDE_LOOP);
        });

        report.check("return/break/continue in their proper place are accepted", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def first(a, b):
                        return a

                    def second(a, b):
                        return b

                    for value in [1, 2, 3]:
                        if value == 2:
                            continue
                        if value == 3:
                            break
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
            // context — the conservative rule accepts the union. See the
            // "MissingFlaskVariableAnalyzer" section of
            // docs/SEMANTIC_ANALYSIS_CHANGES.md for why this stays a
            // documented limitation rather than being fixed here.
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
    // ADVERSARIAL REGRESSIONS  (post-review)
    // ─────────────────────────────────────────────────────────────

    private static void adversarialRegressionTests(TestReport report) {

        report.check("'from x import *' does not crash the legacy declaration checks", () -> {
            // Previously an NPE: FromImportStatement.targets is null for a
            // star import (see PythonVisitor.visitFromImport), and the legacy
            // builder dereferenced it unconditionally.
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    from math import *
                    """);
            expectNoErrors(errors);
        });

        report.check("redefining a function is legal Python, not an error", () -> {
            // The second `def` simply replaces the first at runtime — CPython
            // raises nothing. Previously reported DUPLICATE_FUNCTION.
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    def handle():
                        pass

                    def handle():
                        pass
                    """);
            expectNoErrors(errors);
        });

        report.check("'global' at module level is legal Python, not an error", () -> {
            // Redundant (the module namespace already is the global
            // namespace) but not a SyntaxError. Previously reported
            // GLOBAL_AT_MODULE_LEVEL.
            List<CompilerError> errors = SemanticTestSupport.analyzeAll("""
                    global x
                    x = 1
                    print(x)
                    """);
            expectNoErrors(errors);
        });

        report.check("a dotted import binds only its first component", () -> {
            // `import os.path` binds `os`, not `path`.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    import os.path
                    print(os)
                    """);
            expectNoErrors(errors);
        });

        report.check("the unbound tail of a dotted import is not treated as available", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    import os.path
                    print(path)
                    """);
            expectKinds(errors, UNDEFINED_VARIABLE);
            expectMessageContains(errors, "path");
        });

        report.check("a function later reassigned to a value is not confidently callable", () -> {
            // CPython raises TypeError at convert() here (`convert` is 3 by
            // then). Reporting that correctly needs execution-order modeling,
            // which is out of scope — so this types ANY rather than
            // confidently claiming the call is fine.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def convert(value: int):
                        pass

                    convert = 3
                    convert()
                    """);
            expectNoErrors(errors);
        });

        report.check("a value later reassigned to a function is not confidently non-callable", () -> {
            // CPython calls the function successfully here. Previously
            // reported TYPE_ERROR "'int' object is not callable", because the
            // name stayed bound to its first (VARIABLE) declaration.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    convert = 3

                    def convert(value: int):
                        pass

                    convert("text")
                    """);
            expectNoErrors(errors);
        });

        report.check("same-named functions in different scopes keep separate annotations", () -> {
            // Previously, a name-keyed function registry let the call inside
            // outer() get checked against the module-level helper's
            // annotation (x: int) instead of the actually-called, shadowing
            // helper(y: str) — wrongly reporting TYPE_MISMATCH for "ok".
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    def helper(x: int):
                        pass

                    def outer():
                        def helper(y: str):
                            pass
                        helper("ok")
                    """);
            expectNoErrors(errors);
        });

        report.check("'and'/'or' do not force a boolean result type", () -> {
            // `"" or "fallback"` evaluates to the string "fallback", not a
            // bool. Previously typed unconditionally as BOOL, which made
            // `value + "!"` a false TYPE_ERROR ('bool' and 'str').
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    value = "" or "fallback"
                    combined = value + "!"
                    """);
            expectNoErrors(errors);
        });

        report.check("'not' still yields a real boolean", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    flag = True
                    negated = not flag
                    combined = negated + "!"
                    """);
            expectKinds(errors, TYPE_ERROR);
        });

        report.check("matrix multiplication on plain numbers is reported", () -> {
            // No built-in type supports `@` — only custom __matmul__
            // implementations (e.g. numpy arrays), which type ANY and never
            // reach this check.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    value = 5 @ 2
                    """);
            expectKinds(errors, TYPE_ERROR);
        });

        report.check("'in' against a string requires a string left operand", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    found = 1 in "abc"
                    """);
            expectKinds(errors, TYPE_ERROR);
        });

        report.check("'in' against a list allows any left operand type", () -> {
            // Unlike string containment, list/dict/set membership is a plain
            // equality scan — no left-operand type restriction.
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    found = 1 in [1, 2, 3]
                    also_found = "x" in {"x": 1}
                    """);
            expectNoErrors(errors);
        });

        report.check("'hex' and other numeric-formatting builtins are recognized", () -> {
            List<CompilerError> errors = SemanticTestSupport.analyze("""
                    print(hex(10))
                    print(oct(10))
                    print(bin(10))
                    """);
            expectNoErrors(errors);
        });

        report.check("integrated pipeline does not double-report one missing Flask variable", () -> {
            // Jinja's own analysis already reports UNDEFINED_VARIABLE for a
            // name absent from the (unioned) context set — the same
            // condition MissingFlaskVariableAnalyzer checks, since both are
            // fed the identical union. Previously both fired for the same
            // root cause. The pipeline now stops as soon as Jinja reports
            // anything, before the Flask-side check runs.
            Path fixture = Path.of("build", "fixtures", "dedup-missing-flask");
            SemanticTestSupport.deleteRecursively(fixture);

            SemanticTestSupport.write(fixture.resolve("app.py"), """
                    from flask import Flask, render_template

                    app = Flask(__name__)

                    @app.route('/')
                    def index():
                        return render_template('page.html')
                    """);
            SemanticTestSupport.write(fixture.resolve("templates").resolve("page.html"),
                    "<h1>{{ title }}</h1>\n");

            RecordingProvider provider = new RecordingProvider();
            String output = SemanticTestSupport.captureStdout(
                    () -> runPipeline(fixture, provider, "index"));

            expectTrue(output.contains("UNDEFINED_VARIABLE"),
                    "expected Jinja's own UNDEFINED_VARIABLE in the report:\n" + output);
            expectTrue(!output.contains("MISSING_FLASK_VARIABLE"),
                    "MISSING_FLASK_VARIABLE should not also fire once Jinja already reported "
                            + "the same problem:\n" + output);
            expectTrue(!provider.wasCalled,
                    "code generation ran despite a reported semantic error");

            SemanticTestSupport.deleteRecursively(fixture);
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
