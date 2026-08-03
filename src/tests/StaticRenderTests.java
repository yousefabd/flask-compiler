package tests;

import compiler.generation.FallbackTemplateRenderRequestProvider;
import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.template.TemplateCall;
import compiler.template.TemplateCallFinder;
import errors.CodeGenError;
import errors.ErrorReporter;
import jinja2.runtime.FlashMessage;
import jinja2.runtime.RenderEnvironment;
import jinja2.runtime.RouteDefinition;
import python.PythonFrontend;
import python.execution.StaticTemplateRenderRequestProvider;
import python.models.root.Program;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Tests for {@link StaticTemplateRenderRequestProvider} — the compile-time render
 * context — and for the provider fallback chain.
 *
 * <p>Two things matter here and both are checked: that a context which <i>can</i> be
 * proven is folded to exactly the values (and Java types) the CPython provider would
 * have produced, and that a context which <i>cannot</i> be proven fails loudly
 * instead of guessing.</p>
 *
 * <pre>
 *   build.bat
 *   run.bat tests.StaticRenderTests
 * </pre>
 */
public final class StaticRenderTests {

    private static final Path RENDER_DIR = Path.of("tests", "render");

    public static void main(String[] args) {
        System.exit(run() == 0 ? 0 : 1);
    }

    /** Runs every case. @return the number that failed, so {@link AllTests} can total them up. */
    public static int run() {
        List<String> failures = new ArrayList<>();
        int total = 0;

        System.out.println("Static render context — test suite");
        System.out.println("==================================");
        System.out.println();

        total++; report("Folds literals to CPython-compatible values", foldsLiterals(), failures);
        total++; report("Resolves module and local constants", foldsConstants(), failures);
        total++; report("Collects @app.route definitions", collectsRoutes(), failures);
        total++; report("Collects flash() messages", collectsFlashMessages(), failures);
        total++; report("Refuses a context it cannot prove", reportsNonConstantContext(), failures);
        total++; report("Refuses a conditional flash()", refusesConditionalFlash(), failures);
        total++; report("Fallback chain uses the next provider", fallbackUsesNextProvider(), failures);
        total++; report("Fallback chain reports every failure", fallbackReportsAllFailures(), failures);

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

    private static String foldsLiterals() {
        Map<String, Object> context;

        try {
            context = requestFor("literals.py", "literals").context();
        } catch (RuntimeException failure) {
            return "folding threw " + failure.getClass().getSimpleName()
                    + ": " + failure.getMessage();
        }

        return firstFailure(
                // Gson's LONG_OR_DOUBLE policy is the contract the CPython provider
                // produces; the folded values must use the same Java types.
                equals("name", "Yousef", context.get("name")),
                equals("age", 24L, context.get("age")),
                equals("price", 999.99, context.get("price")),
                equals("active", Boolean.TRUE, context.get("active")),
                equals("note", null, context.get("note")),
                equals("items", List.of("a", "b"), context.get("items")),
                equals("data", Map.of("id", 1L, "label", "x"), context.get("data")),
                context.containsKey("note")
                        ? null
                        : "a None value must stay in the context as a null entry"
        );
    }

    private static String foldsConstants() {
        Map<String, Object> context;

        try {
            context = requestFor("literals.py", "literals").context();
        } catch (RuntimeException failure) {
            return "folding threw " + failure.getClass().getSimpleName()
                    + ": " + failure.getMessage();
        }

        // total = unit_price * quantity, both module-level constants
        return equals("total", 60L, context.get("total"));
    }

    private static String collectsRoutes() {
        RenderEnvironment environment;

        try {
            environment = requestFor("literals.py", "literals").environment();
        } catch (RuntimeException failure) {
            return "folding threw " + failure.getClass().getSimpleName()
                    + ": " + failure.getMessage();
        }

        RouteDefinition literals = routeFor(environment, "literals");
        RouteDefinition details = routeFor(environment, "product_details");
        RouteDefinition staticFiles = routeFor(environment, "static");

        if (literals == null) return "no route was collected for 'literals'";
        if (details == null) return "no route was collected for 'product_details'";
        if (staticFiles == null) return "Flask's built-in 'static' endpoint is missing";

        return firstFailure(
                equals("literals rule", "/literals", literals.rule()),
                equals("literals arguments", List.of(), literals.arguments()),
                equals("product_details rule", "/product/<int:product_id>", details.rule()),
                // the converter prefix must not end up in the argument name
                equals("product_details arguments", List.of("product_id"), details.arguments()),
                equals("static rule", "/static/<path:filename>", staticFiles.rule())
        );
    }

    private static String collectsFlashMessages() {
        List<FlashMessage> flashed;

        try {
            flashed = requestFor("flashes.py", "saved").environment().flashedMessages();
        } catch (RuntimeException failure) {
            return "folding threw " + failure.getClass().getSimpleName()
                    + ": " + failure.getMessage();
        }

        if (flashed.size() != 2)
            return "expected 2 flashed messages but got " + flashed.size() + ": " + flashed;

        return firstFailure(
                // Flask's default category
                equals("first category", "message", flashed.get(0).category()),
                equals("first message", "Product saved", flashed.get(0).message()),
                equals("second category", "warning", flashed.get(1).category()),
                equals("second message", "Check the price", flashed.get(1).message())
        );
    }

    private static String reportsNonConstantContext() {
        try {
            requestFor("literals.py", "product_details");
            return "expected a CodeGenError: 'product' comes from a route parameter";

        } catch (CodeGenError expected) {
            String message = expected.getMessage();

            if (!message.contains("'product'"))
                return "the error should name the context value, got: " + message;

            if (!message.contains("not a compile-time constant"))
                return "the error should say the value is not constant, got: " + message;

            return null;

        } catch (RuntimeException wrong) {
            return "expected a CodeGenError but got "
                    + wrong.getClass().getSimpleName() + ": " + wrong.getMessage();
        }
    }

    private static String refusesConditionalFlash() {
        try {
            requestFor("flashes.py", "maybe");
            return "expected a CodeGenError: the flash() runs inside an if";

        } catch (CodeGenError expected) {
            if (!expected.getMessage().contains("conditional"))
                return "the error should explain the conditional flash, got: "
                        + expected.getMessage();

            return null;

        } catch (RuntimeException wrong) {
            return "expected a CodeGenError but got "
                    + wrong.getClass().getSimpleName() + ": " + wrong.getMessage();
        }
    }

    private static String fallbackUsesNextProvider() {
        TemplateRenderRequestProvider failing =
                call -> { throw new CodeGenError("app.py", 1, "cannot fold"); };

        TemplateRenderRequestProvider working =
                call -> new TemplateRenderRequest(
                        call.templateName(), Map.of("ok", true), RenderEnvironment.empty());

        TemplateRenderRequestProvider chain =
                new FallbackTemplateRenderRequestProvider(
                        "app.py", List.of(failing, working));

        try {
            TemplateRenderRequest request = chain.provide(sampleCall());
            return equals("context", Map.of("ok", true), request.context());

        } catch (RuntimeException failure) {
            return "the chain should have used the second provider, but threw: "
                    + failure.getMessage();
        }
    }

    private static String fallbackReportsAllFailures() {
        TemplateRenderRequestProvider first =
                call -> { throw new CodeGenError("app.py", 1, "no constant value"); };

        TemplateRenderRequestProvider second =
                call -> { throw new CodeGenError("app.py", 1, "python.exe not found"); };

        TemplateRenderRequestProvider chain =
                new FallbackTemplateRenderRequestProvider(
                        "app.py", List.of(first, second));

        try {
            chain.provide(sampleCall());
            return "expected the chain to fail when every provider fails";

        } catch (CodeGenError expected) {
            String message = expected.getMessage();

            if (!message.contains("no constant value") || !message.contains("python.exe not found"))
                return "both failures should be reported, got: " + message;

            return null;

        } catch (RuntimeException wrong) {
            return "expected a CodeGenError but got " + wrong.getClass().getSimpleName();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    /** Parses one sample app and folds the render context of one of its routes. */
    private static TemplateRenderRequest requestFor(String sourceFile, String functionName) {
        Path source = RENDER_DIR.resolve(sourceFile);

        ErrorReporter reporter = new ErrorReporter();

        Program program = new PythonFrontend(source, reporter).parsePython();

        if (program == null)
            throw new IllegalStateException(
                    "the sample app did not parse:\n" + reporter.formatReport());

        TemplateCall call =
                TemplateCallFinder.findTemplateCalls(program)
                        .stream()
                        .filter(candidate ->
                                candidate.ownerFunctionName().equals(functionName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "no render_template call in '" + functionName + "'"));

        return new StaticTemplateRenderRequestProvider(program, source).provide(call);
    }

    private static TemplateCall sampleCall() {
        return new TemplateCall("index", "page.html", Map.of(), 1);
    }

    private static RouteDefinition routeFor(RenderEnvironment environment, String endpoint) {
        for (RouteDefinition route : environment.routes())
            if (route.endpoint().equals(endpoint)) return route;

        return null;
    }

    /** @return null when the values match, otherwise a description of the difference */
    private static String equals(String what, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) return null;

        return what + ": expected " + describe(expected) + " but got " + describe(actual);
    }

    private static String describe(Object value) {
        if (value == null) return "null";
        return value + " (" + value.getClass().getSimpleName() + ")";
    }

    private static String firstFailure(String... results) {
        for (String result : results)
            if (result != null) return result;

        return null;
    }

    private static void report(String name, String failure, List<String> failures) {
        if (failure == null) {
            System.out.println("  PASS  " + name);
            return;
        }

        System.out.println("  FAIL  " + name + " - " + failure);
        failures.add(name + " - " + failure);
    }
}
