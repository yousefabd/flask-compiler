package compiler.runtime.unit_tests;

import compiler.CompilationPipeline;
import compiler.generation.TemplateRenderRequest;
import compiler.runtime.CompiledApplication;

import java.util.List;
import java.util.Map;

public final class CompiledApplicationPersistenceTest {

    public static void main(String[] args) {
        CompilationPipeline pipeline =
                new CompilationPipeline();

        CompiledApplication application =
                pipeline.compileApplication();

        require(
                application != null,
                "Application compilation failed"
        );

        TemplateRenderRequest beforeDeletion =
                application.invokeRenderFunction(
                        "view_products"
                );

        List<?> productsBefore =
                requireProducts(beforeDeletion);

        require(
                productsBefore.size() == 3,
                "Expected three initial products"
        );

        application.invokePythonFunction(
                "remove_product_by_id",
                List.<Object>of(1),
                Map.of()
        );

        TemplateRenderRequest afterDeletion =
                application.invokeRenderFunction(
                        "view_products"
                );

        List<?> productsAfter =
                requireProducts(afterDeletion);

        require(
                productsAfter.size() == 2,
                "Product deletion did not persist"
        );

        String renderedHtml =
                application.render(afterDeletion);

        require(
                !renderedHtml.contains("Laptop"),
                "Deleted product still appears in HTML"
        );

        require(
                renderedHtml.contains("Smartphone"),
                "Remaining product is missing"
        );

        require(
                renderedHtml.contains("Headphones"),
                "Remaining product is missing"
        );

        System.out.println(
                "Before deletion: "
                        + productsBefore.size()
        );

        System.out.println(
                "After deletion: "
                        + productsAfter.size()
        );

        System.out.println(
                "Persistent application state test passed."
        );
    }

    private static List<?> requireProducts(
            TemplateRenderRequest request
    ) {
        Object products =
                request.context().get("products");

        if (!(products instanceof List<?> list)) {
            throw new AssertionError(
                    "Render context does not contain a products list"
            );
        }

        return list;
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