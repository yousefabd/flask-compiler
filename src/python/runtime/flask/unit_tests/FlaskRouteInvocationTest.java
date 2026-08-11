package python.runtime.flask.unit_tests;

import compiler.CompilationPipeline;
import compiler.generation.TemplateRenderRequest;
import compiler.runtime.CompiledApplication;
import python.runtime.flask.FlaskRouteMatch;
import python.runtime.flask.FlaskRouteMatcher;

import java.util.Map;

public final class FlaskRouteInvocationTest {

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

        FlaskRouteMatch match =
                matcher.match(
                        application.routes(),
                        "GET",
                        "/product/2"
                ).orElseThrow(
                        () -> new AssertionError(
                                "/product/2 did not match"
                        )
                );

        Object result =
                application.invokeRoute(match);

        if (!(result
                instanceof TemplateRenderRequest request)) {

            throw new AssertionError(
                    "Route did not return a TemplateRenderRequest"
            );
        }

        require(
                request.templateName()
                        .equals("index.html"),
                "Wrong template was selected"
        );

        Object productValue =
                request.context().get("product");

        if (!(productValue instanceof Map<?, ?> product)) {
            throw new AssertionError(
                    "Product context value is missing"
            );
        }

        require(
                "Smartphone".equals(
                        product.get("name")
                ),
                "Wrong product was resolved"
        );

        require(
                Integer.valueOf(2).equals(
                        product.get("id")
                ),
                "Wrong product ID was resolved"
        );

        String renderedHtml =
                application.render(request);

        require(
                renderedHtml.contains(
                        "Product Details"
                ),
                "Details branch was not rendered"
        );

        require(
                renderedHtml.contains(
                        "Smartphone"
                ),
                "Product name is missing from HTML"
        );

        require(
                renderedHtml.contains(
                        "/delete/2"
                ),
                "Generated delete URL is incorrect"
        );

        System.out.println(
                "Route invocation passed."
        );

        System.out.println(
                "GET /product/2 rendered Smartphone details."
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