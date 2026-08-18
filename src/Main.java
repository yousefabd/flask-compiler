import compiler.CompilationPipeline;
import compiler.artifacts.AstJsonSerializer;
import compiler.artifacts.CompilerArtifactWriter;
import compiler.generation.HtmlFileGenerator;
import compiler.generation.SupportingFilesCopier;
import compiler.preparation.PreparedApplication;
import compiler.runtime.CompiledApplication;
import errors.CompilerException;
import errors.CompilerIoError;
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
        CompilerArtifactWriter artifactWriter =
                new CompilerArtifactWriter(
                        CompilerSettings.compilerOutputDir
                );

        AstJsonSerializer astJsonSerializer =
                new AstJsonSerializer();
        try {
            /*
             * These two artifacts are available even when compilation
             * fails.
             */
            artifactWriter.writeSemanticReport(
                    pipeline.formatReport()
            );

            artifactWriter.writeAnalysisLog(
                    pipeline.formatAnalysisLog()
            );

            /*
             * Complete AST artifacts are written only after both
             * frontends prepared successfully.
             */
            if (application != null) {
                PreparedApplication preparation =
                        application.preparation();

                artifactWriter.writePythonAst(
                        astJsonSerializer.serializePython(
                                preparation
                                        .backend()
                                        .program()
                        )
                );

                artifactWriter.writeJinjaAst(
                        astJsonSerializer.serializeJinja(
                                preparation
                                        .frontend()
                                        .templates()
                        )
                );

                artifactWriter.writeCssAst(
                        astJsonSerializer.serializeCss(
                                preparation
                                        .css()
                                        .stylesheets()
                        )
                );
            }

        } catch (CompilerException exception) {
            System.err.println(
                    exception.toProblem()
            );

            return;
        }


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