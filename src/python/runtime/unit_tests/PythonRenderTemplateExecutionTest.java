package python.runtime.unit_tests;

import compiler.generation.TemplateRenderRequest;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.*;
import python.runtime.flask.FlaskApplication;
import python.runtime.flask.FlaskRoute;
import python.runtime.flask.FlaskRuntimeBindings;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class PythonRenderTemplateExecutionTest {

    public static void main(String[] args) {
        Path source =
                Path.of(
                        "tests",
                        "app.py"
                );

        ErrorReporter reporter =
                new ErrorReporter();

        PythonFrontend frontend =
                new PythonFrontend(
                        source,
                        reporter
                );

        Program program =
                frontend.parsePython();

        if (program == null) {
            reporter.printReport();

            throw new AssertionError(
                    "Real app.py parsing failed"
            );
        }

        frontend.analyzePython(program);

        PythonEnvironment module =
                PythonEnvironment.module();

        String moduleName =
                deriveModuleName(source);

        module.defineLocal(
                "__name__",
                moduleName
        );

        FlaskRuntimeBindings flaskBindings =
                new FlaskRuntimeBindings();

        flaskBindings.installInto(module);

        PythonInterpreter interpreter =
                new PythonInterpreter(
                        new PythonExpressionEvaluator()
                );

        /*
         * This executes only the module-level AST.
         *
         * Function bodies are registered but not executed.
         * The final app.run() branch is skipped because
         * __name__ is "app", not "__main__".
         */
        interpreter.executeModule(
                program,
                module
        );
        Object functionValue =
                module.resolve("view_products");

        require(
                functionValue instanceof PythonFunction,
                "view_products was not registered"
        );

        PythonFunction viewProducts =
                (PythonFunction) functionValue;

        Object result =
                viewProducts.call(
                        new PythonCallArguments(
                                List.of(),
                                Map.of(),
                                0
                        )
                );

        require(
                result instanceof TemplateRenderRequest,
                "view_products did not return"
                        + " a TemplateRenderRequest"
        );

        TemplateRenderRequest renderRequest =
                (TemplateRenderRequest) result;

        require(
                "index.html".equals(
                        renderRequest.templateName()
                ),
                "Wrong template name"
        );

        require(
                "products".equals(
                        renderRequest.context().get("page")
                ),
                "Wrong page context value"
        );

        Object contextProducts =
                renderRequest.context().get("products");

        require(
                contextProducts
                        instanceof List<?> products
                        && products.size() == 3,
                "Products context was not captured"
        );

        require(
                contextProducts
                        == module.resolve("products"),
                "render_template copied or replaced"
                        + " the live products value"
        );

        jinja2.runtime.RouteDefinition productDetails =
                renderRequest.environment()
                        .routes()
                        .stream()
                        .filter(route ->
                                route.endpoint()
                                        .equals("product_details")
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new AssertionError(
                                        "product_details route is missing"
                                )
                        );

        require(
                "/product/<int:product_id>".equals(
                        productDetails.rule()
                ),
                "Dynamic product route has the wrong rule"
        );

        require(
                productDetails.arguments()
                        .equals(List.of("product_id")),
                "Dynamic route arguments were not extracted"
        );

        FlaskApplication application =
                (FlaskApplication) module.resolve("app");

        FlaskRoute addRoute =
                application.routes()
                        .stream()
                        .filter(route ->
                                route.endpoint()
                                        .equals("add_product")
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new AssertionError(
                                        "add_product route is missing"
                                )
                        );

        require(
                addRoute.methods().contains("GET")
                        && addRoute.methods().contains("POST"),
                "add_product HTTP methods were not captured"
        );

        System.out.println(
                "Routes captured: "
                        + application.routes().size()
        );

        System.out.println(
                "Product route: "
                        + productDetails.rule()
        );

        System.out.println(
                "Product arguments: "
                        + productDetails.arguments()
        );

        System.out.println(
                "Add methods: "
                        + addRoute.methods()
        );

        require(
                renderRequest.environment()
                        .flashedMessages()
                        .isEmpty(),
                "Flashed messages should still be empty"
        );

        System.out.println(
                "Java render_template execution passed."
        );

        System.out.println(
                "Template: "
                        + renderRequest.templateName()
        );

        System.out.println(
                "Page: "
                        + renderRequest.context()
                        .get("page")
        );

        System.out.println(
                "Products: "
                        + ((List<?>) contextProducts)
                        .size()
        );
    }

    private static String deriveModuleName(
            Path source
    ) {
        String filename =
                source.getFileName().toString();

        int extensionIndex =
                filename.lastIndexOf('.');

        if (extensionIndex <= 0) {
            return filename;
        }

        return filename.substring(
                0,
                extensionIndex
        );
    }

    private static void require(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}