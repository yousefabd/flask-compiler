import compiler.CompilationPipeline;
import compiler.generation.TemplateRenderRequestProvider;
import python.execution.CPythonTemplateRenderRequestProvider;
import utils.CompilerSettings;

public class Main {

    public static void main(String[] args) {
        /*
         * For now, render_test is the default integration-test function.
         * You can also pass another function name as the first program
         * argument.
         */
        String functionToRender =
                args.length == 0
                        ? "url_test"
                        : args[0];

        TemplateRenderRequestProvider renderRequestProvider =
                new CPythonTemplateRenderRequestProvider(
                        CompilerSettings.pythonExecutable,
                        CompilerSettings.renderCaptureScript,
                        CompilerSettings.appSource
                );

        CompilationPipeline pipeline =
                new CompilationPipeline(
                        renderRequestProvider
                );

        pipeline.compileSnapshot(
                functionToRender
        );
    }
}