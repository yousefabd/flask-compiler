import compiler.CompilationPipeline;

public class Main {

    public static void main(String[] args) {
        String functionToRender =
                args.length == 0
                        ? "view_products"
                        : args[0];

        CompilationPipeline pipeline =
                new CompilationPipeline();

        pipeline.compileSnapshot(
                functionToRender
        );
    }
}