package server.unit_tests;

import compiler.CompilationPipeline;
import compiler.generation.HtmlFileGenerator;
import compiler.runtime.CompiledApplication;
import html.formatting.HtmlFormatter;
import python.runtime.flask.FlaskRuntimeDefaults;
import server.ApplicationRequestDispatcher;
import server.http.ServerResponse;
import server.staticfiles.StaticFileService;
import utils.CompilerSettings;

public final class ApplicationRequestDispatcherTest {

    public static void main(String[] args) {
        CompilationPipeline pipeline =
                new CompilationPipeline();

        CompiledApplication application =
                pipeline.compileApplication();

        require(
                application != null,
                "Application compilation failed"
        );

        HtmlFileGenerator htmlFileGenerator =
                new HtmlFileGenerator(
                        CompilerSettings.outputDir,
                        HtmlFormatter.unchanged()
                );
        StaticFileService staticFileService =
                new StaticFileService(
                        CompilerSettings.outputDir.resolve(
                                FlaskRuntimeDefaults
                                        .STATIC_DIRECTORY_NAME
                        )
                );
        ApplicationRequestDispatcher dispatcher =
                new ApplicationRequestDispatcher(
                        application,
                        htmlFileGenerator,
                        staticFileService
                );

        ServerResponse catalog =
                dispatcher.dispatch(
                        "GET",
                        "/products"
                );

        require(
                catalog.statusCode() == 200,
                "Catalog did not return HTTP 200"
        );

        require(
                catalog.bodyAsText()
                        .contains("Product Catalog"),
                "Catalog HTML is incorrect"
        );

        require(
                catalog.bodyAsText()
                        .contains("Total Products: 3"),
                "Catalog context is incorrect"
        );

        ServerResponse details =
                dispatcher.dispatch(
                        "GET",
                        "/product/2"
                );

        require(
                details.statusCode() == 200,
                "Product details did not return HTTP 200"
        );

        require(
                details.bodyAsText()
                        .contains("Smartphone"),
                "Wrong product was rendered"
        );

        ServerResponse missing =
                dispatcher.dispatch(
                        "GET",
                        "/does-not-exist"
                );

        require(
                missing.statusCode() == 404,
                "Missing route did not return HTTP 404"
        );

        require(
                missing.bodyAsText()
                        .equals("Not Found"),
                "Wrong HTTP 404 body"
        );

        System.out.println(
                "Application request dispatch passed."
        );

        System.out.println(
                "GET /products -> 200"
        );

        System.out.println(
                "GET /product/2 -> 200"
        );

        System.out.println(
                "GET /does-not-exist -> 404"
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