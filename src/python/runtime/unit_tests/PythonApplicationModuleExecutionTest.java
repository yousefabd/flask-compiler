package python.runtime.unit_tests;

import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.runtime.PythonCallArguments;
import python.runtime.PythonEnvironment;
import python.runtime.PythonExpressionEvaluator;
import python.runtime.PythonFunction;
import python.runtime.PythonInterpreter;
import python.runtime.flask.FlaskApplication;
import python.runtime.flask.FlaskRuntimeBindings;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class PythonApplicationModuleExecutionTest {

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

        verifyApplication(
                module,
                moduleName
        );

        verifyInitialProducts(module);

        verifyRegisteredFunctions(module);

        verifyProductHelpers(module);

        System.out.println(
                "Real Python application module passed."
        );
    }

    private static void verifyApplication(
            PythonEnvironment module,
            String moduleName
    ) {
        Object appValue =
                module.resolve("app");

        require(
                appValue instanceof FlaskApplication,
                "app is not a FlaskApplication"
        );

        FlaskApplication application =
                (FlaskApplication) appValue;

        require(
                moduleName.equals(
                        application.importName()
                ),
                "Flask received the wrong module name"
        );

        require(
                application.secretKey() != null,
                "app.secret_key was not assigned"
        );

        System.out.println(
                "Module: "
                        + application.importName()
        );

        System.out.println(
                "Secret key captured: "
                        + application.secretKey()
        );
    }

    private static void verifyInitialProducts(
            PythonEnvironment module
    ) {
        Object productsValue =
                module.resolve("products");

        require(
                productsValue instanceof List<?>,
                "products is not a runtime list"
        );

        List<?> products =
                (List<?>) productsValue;

        require(
                products.size() == 3,
                "Expected three initial products"
        );

        require(
                products.getFirst() instanceof Map<?, ?>,
                "First product is not a dictionary"
        );

        Map<?, ?> firstProduct =
                (Map<?, ?>) products.getFirst();

        require(
                "Laptop".equals(
                        firstProduct.get("name")
                ),
                "First product was initialized incorrectly"
        );

        System.out.println(
                "Initial products: "
                        + products.size()
        );
    }

    private static void verifyRegisteredFunctions(
            PythonEnvironment module
    ) {
        requireFunction(
                module,
                "find_product_by_id"
        );

        requireFunction(
                module,
                "get_max_product_id"
        );

        requireFunction(
                module,
                "remove_product_by_id"
        );

        /*
         * This also confirms decorated route functions
         * are registered as normal Python functions.
         */
        requireFunction(
                module,
                "view_products"
        );
    }

    private static void verifyProductHelpers(
            PythonEnvironment module
    ) {
        PythonFunction findProduct =
                requireFunction(
                        module,
                        "find_product_by_id"
                );

        Object foundValue =
                findProduct.call(
                        new PythonCallArguments(
                                List.of(2),
                                Map.of(),
                                0
                        )
                );

        require(
                foundValue instanceof Map<?, ?>,
                "find_product_by_id did not return a dictionary"
        );

        Map<?, ?> foundProduct =
                (Map<?, ?>) foundValue;

        require(
                "Smartphone".equals(
                        foundProduct.get("name")
                ),
                "Found the wrong product"
        );

        PythonFunction getMaximum =
                requireFunction(
                        module,
                        "get_max_product_id"
                );

        Object maximumValue =
                getMaximum.call(
                        new PythonCallArguments(
                                List.of(),
                                Map.of(),
                                0
                        )
                );

        require(
                maximumValue instanceof Number number
                        && number.intValue() == 3,
                "Maximum product ID should be 3"
        );

        PythonFunction removeProduct =
                requireFunction(
                        module,
                        "remove_product_by_id"
                );

        removeProduct.call(
                new PythonCallArguments(
                        List.of(1),
                        Map.of(),
                        0
                )
        );

        /*
         * Resolve products again because the helper rebinds
         * the global name to a new list.
         */
        List<?> remainingProducts =
                (List<?>) module.resolve("products");

        require(
                remainingProducts.size() == 2,
                "Product removal produced the wrong size"
        );

        Map<?, ?> firstRemaining =
                (Map<?, ?>) remainingProducts.getFirst();

        require(
                Integer.valueOf(2).equals(
                        firstRemaining.get("id")
                ),
                "The wrong product was removed"
        );

        System.out.println(
                "Found product: "
                        + foundProduct.get("name")
        );

        System.out.println(
                "Maximum ID: "
                        + maximumValue
        );

        System.out.println(
                "Remaining products after removing ID 1: "
                        + remainingProducts.size()
        );
    }

    private static PythonFunction requireFunction(
            PythonEnvironment module,
            String name
    ) {
        Object value =
                module.resolve(name);

        require(
                value instanceof PythonFunction,
                name
                        + " was not registered"
        );

        return (PythonFunction) value;
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