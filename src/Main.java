import compiler.CompilationPipeline;
import compiler.generation.HtmlFileGenerator;
import compiler.generation.SupportingFilesCopier;
import compiler.runtime.CompiledApplication;
import html.formatting.HtmlFormatter;
import python.runtime.flask.FlaskRuntimeDefaults;
import server.ApplicationRequestDispatcher;
import server.JavaApplicationServer;
import server.staticfiles.StaticFileService;
import utils.CompilerSettings;

import java.io.IOException;

public class Main {

    public static void main(
            String[] args
    ) throws IOException {
        CompilationPipeline pipeline =
                new CompilationPipeline();

        CompiledApplication application =
                pipeline.compileApplication();

        if (application == null) {
            return;
        }
        SupportingFilesCopier supportingFilesCopier =
                new SupportingFilesCopier(
                        CompilerSettings.appSource,
                        CompilerSettings.staticDir,
                        CompilerSettings.outputDir
                );

        supportingFilesCopier.copy();

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

        JavaApplicationServer server =
                new JavaApplicationServer(
                        8080,
                        dispatcher
                );

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> server.stop(0),
                                "application-server-shutdown"
                        )
                );

        server.start();

        System.out.println(
                "Compilation completed successfully."
        );

        System.out.println(
                "Application running at http://localhost:"
                        + server.port()
        );
    }
}