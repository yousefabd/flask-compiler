import compiler.CompilationPipeline;
import compiler.runtime.CompiledApplication;
import server.ApplicationRequestDispatcher;
import server.JavaApplicationServer;

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

        ApplicationRequestDispatcher dispatcher =
                new ApplicationRequestDispatcher(
                        application
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