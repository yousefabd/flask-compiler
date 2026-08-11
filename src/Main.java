import compiler.CompilationPipeline;
import compiler.runtime.CompiledApplication;

public class Main {

    public static void main(String[] args) {
        String functionToRender =
                args.length == 0
                        ? "view_products"
                        : args[0];

        CompilationPipeline pipeline =
                new CompilationPipeline();

        CompiledApplication application =
                pipeline.compileApplication();

        if (application == null) {
            return;
        }

        pipeline.compileSnapshot(
                application,
                functionToRender
        );
    }
}