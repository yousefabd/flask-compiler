package python.runtime.flask.unit_tests;

import compiler.CompilationPipeline;
import compiler.runtime.CompiledApplication;
import python.runtime.flask.FlaskRouteMatch;
import python.runtime.flask.FlaskRouteMatcher;

public final class FlaskRouteMatcherTest {

    public static void main(String[] args) {
        CompilationPipeline pipeline =
                new CompilationPipeline();

        CompiledApplication application =
                pipeline.compileApplication();

        require(
                application != null,
                "Application compilation failed"
        );

        FlaskRouteMatcher matcher =
                new FlaskRouteMatcher();

        FlaskRouteMatch products =
                matcher.match(
                        application.routes(),
                        "GET",
                        "/products"
                ).orElseThrow(
                        () -> new AssertionError(
                                "/products did not match"
                        )
                );

        require(
                products.route()
                        .endpoint()
                        .equals("view_products"),
                "Wrong /products handler"
        );

        FlaskRouteMatch productDetails =
                matcher.match(
                        application.routes(),
                        "GET",
                        "/product/2"
                ).orElseThrow(
                        () -> new AssertionError(
                                "/product/2 did not match"
                        )
                );

        require(
                productDetails.route()
                        .endpoint()
                        .equals("product_details"),
                "Wrong product-details handler"
        );

        require(
                Integer.valueOf(2).equals(
                        productDetails.arguments()
                                .get("product_id")
                ),
                "product_id was not converted to an integer"
        );

        require(
                matcher.match(
                        application.routes(),
                        "POST",
                        "/add"
                ).isPresent(),
                "POST /add should match"
        );

        require(
                matcher.match(
                        application.routes(),
                        "DELETE",
                        "/add"
                ).isEmpty(),
                "DELETE /add should not match"
        );

        require(
                matcher.match(
                        application.routes(),
                        "GET",
                        "/missing"
                ).isEmpty(),
                "Unknown route unexpectedly matched"
        );

        System.out.println(
                "Route matching passed."
        );

        System.out.println(
                "Matched /product/2 -> "
                        + productDetails.route().endpoint()
                        + " "
                        + productDetails.arguments()
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