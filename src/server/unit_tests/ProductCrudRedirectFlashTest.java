package server.unit_tests;

import compiler.CompilationPipeline;
import compiler.generation.HtmlFileGenerator;
import compiler.runtime.CompiledApplication;
import html.formatting.HtmlFormatter;
import python.runtime.flask.FlaskRequestData;
import python.runtime.flask.FlaskRuntimeDefaults;
import server.ApplicationRequestDispatcher;
import server.http.ServerResponse;
import server.staticfiles.StaticFileService;
import utils.CompilerSettings;

import java.util.Map;

public final class ProductCrudRedirectFlashTest {
    public static void main(String[] args) {
        CompiledApplication application =
                new CompilationPipeline().compileApplication();
        require(application != null, "Application compilation failed");

        ApplicationRequestDispatcher dispatcher =
                new ApplicationRequestDispatcher(
                        application,
                        new HtmlFileGenerator(
                                CompilerSettings.outputDir,
                                HtmlFormatter.unchanged()
                        ),
                        new StaticFileService(
                                CompilerSettings.outputDir.resolve(
                                        FlaskRuntimeDefaults.STATIC_DIRECTORY_NAME
                                )
                        )
                );

        assertRedirect(dispatcher.dispatch("GET", "/redirect-test"), "/products");

        ServerResponse flashRedirect =
                dispatcher.dispatch("GET", "/flash-redirect-test");
        assertRedirect(flashRedirect, "/");
        ServerResponse flashedHome = dispatcher.dispatch("GET", "/");
        require(flashedHome.bodyAsText().contains("Flash survived the Java redirect"),
                "Flash message did not survive the redirect");

        ServerResponse addRedirect = dispatcher.dispatch(
                new FlaskRequestData(
                        "POST",
                        "/add",
                        Map.of(
                                "name", "Keyboard",
                                "price", "89.50",
                                "image", "keyboard.jpg",
                                "details", "Mechanical keyboard"
                        )
                )
        );
        assertRedirect(addRedirect, "/products");

        ServerResponse afterAdd = dispatcher.dispatch("GET", "/products");
        require(afterAdd.bodyAsText().contains("Total Products: 4"),
                "POST /add did not persist the new product");
        require(afterAdd.bodyAsText().contains("Keyboard"),
                "Added product was not rendered");
        require(afterAdd.bodyAsText().contains("added successfully"),
                "Add-product flash message was not rendered");

        ServerResponse deleteRedirect = dispatcher.dispatch("GET", "/delete/4");
        assertRedirect(deleteRedirect, "/products");

        ServerResponse afterDelete = dispatcher.dispatch("GET", "/products");
        require(afterDelete.bodyAsText().contains("Total Products: 3"),
                "Delete route did not persist removal");
        require(afterDelete.bodyAsText().contains("deleted successfully"),
                "Delete-product flash message was not rendered");

        System.out.println("Product CRUD, redirect, and flash behavior passed.");
    }

    private static void assertRedirect(ServerResponse response, String location) {
        require(response.statusCode() == 302,
                "Expected HTTP 302, received " + response.statusCode());
        require(location.equals(response.headers().get("Location")),
                "Wrong redirect location: " + response.headers());
        require(response.body().length == 0, "Redirect unexpectedly had a body");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
