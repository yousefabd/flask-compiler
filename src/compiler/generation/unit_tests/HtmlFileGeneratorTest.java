package compiler.generation.unit_tests;

import compiler.CompilationPipeline;
import compiler.generation.HtmlFileGenerator;
import compiler.generation.TemplateRenderRequest;
import compiler.runtime.CompiledApplication;
import html.formatting.HtmlFormatter;
import utils.CompilerSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class HtmlFileGeneratorTest {

    public static void main(
            String[] args
    ) throws IOException {
        CompiledApplication application =
                new CompilationPipeline()
                        .compileApplication();

        require(
                application != null,
                "Application compilation failed"
        );

        HtmlFileGenerator generator =
                new HtmlFileGenerator(
                        CompilerSettings.outputDir,
                        HtmlFormatter.unchanged()
                );

        TemplateRenderRequest initialRequest =
                application.invokeRenderFunction(
                        "view_products"
                );

        Path generatedFile =
                generator.generate(
                        application,
                        initialRequest
                );

        require(
                Files.isRegularFile(generatedFile),
                "Generated HTML file does not exist"
        );

        String initialHtml =
                Files.readString(generatedFile);

        require(
                initialHtml.contains(
                        "Total Products: 3"
                ),
                "Initial HTML contains the wrong product count"
        );

        requireResolved(initialHtml);

        application.invokePythonFunction(
                "remove_product_by_id",
                List.<Object>of(1),
                Map.of()
        );

        TemplateRenderRequest updatedRequest =
                application.invokeRenderFunction(
                        "view_products"
                );

        Path regeneratedFile =
                generator.generate(
                        application,
                        updatedRequest
                );

        require(
                generatedFile.equals(regeneratedFile),
                "Regeneration wrote to a different file"
        );

        String regeneratedHtml =
                Files.readString(regeneratedFile);

        require(
                regeneratedHtml.contains(
                        "Total Products: 2"
                ),
                "Regenerated HTML contains the wrong product count"
        );

        require(
                !regeneratedHtml.contains("Laptop"),
                "Deleted product remains in generated HTML"
        );

        requireResolved(regeneratedHtml);

        System.out.println(
                "Generated file: "
                        + generatedFile
        );

        System.out.println(
                "Regeneration changed the product count from 3 to 2."
        );

        System.out.println(
                "HTML file generation passed."
        );
    }

    private static void requireResolved(
            String html
    ) {
        require(
                !html.contains("{{")
                        && !html.contains("{%"),
                "Generated file contains unresolved Jinja"
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