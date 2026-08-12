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

public final class FlaskRequestExecutionTest {

    public static void main(String[] args) {
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
                        generator,
                        staticFileService
                );

        ServerResponse response =
                dispatcher.dispatch(
                        "GET",
                        "/add"
                );

        require(
                response.statusCode() == 200,
                "GET /add did not return HTTP 200"
        );

        require(
                response.bodyAsText()
                        .contains("Add New Product"),
                "Add-product page was not rendered"
        );

        require(
                response.bodyAsText()
                        .contains(
                                "<form method=\"POST\""
                        ),
                "Generated add-product form is missing"
        );

        System.out.println(
                "Flask request context passed."
        );

        System.out.println(
                "GET /add -> 200"
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