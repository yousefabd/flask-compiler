import compiler.CompilationPipeline;
import compiler.generation.TemplateContextProvider;
import python.execution.CPythonTemplateContextProvider;
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
                        ? "render_test"
                        : args[0];

        TemplateContextProvider contextProvider =
                new CPythonTemplateContextProvider(
                        CompilerSettings.pythonExecutable,
                        CompilerSettings.renderCaptureScript,
                        CompilerSettings.appSource
                );

        CompilationPipeline pipeline =
                new CompilationPipeline(
                        contextProvider
                );

        pipeline.compileSnapshot(
                functionToRender
        );
    }
}