package python.runtime;

import compiler.generation.TemplateRenderRequest;
import python.models.root.Program;
import python.runtime.flask.FlaskApplication;
import python.runtime.flask.FlaskRequestData;
import python.runtime.flask.FlaskRuntimeBindings;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class PythonApplicationRuntime {

    private final PythonEnvironment module;
    private final PythonInterpreter interpreter;
    private final FlaskRuntimeBindings flaskBindings;


    public PythonApplicationRuntime(
            Path appSource,
            Program program
    ) {
        Objects.requireNonNull(appSource);
        Objects.requireNonNull(program);

        this.module =
                PythonEnvironment.module();

        this.interpreter =
                new PythonInterpreter(
                        new PythonExpressionEvaluator()
                );

        this.flaskBindings =
                new FlaskRuntimeBindings();

        module.defineLocal(
                "__name__",
                deriveModuleName(appSource)
        );

        flaskBindings.installInto(module);

        /*
         * Executes the already-parsed Python AST once.
         */
        interpreter.executeModule(
                program,
                module
        );
    }

    public Object invoke(
            String functionName,
            List<Object> positional,
            Map<String, Object> keywords
    ) {
        Objects.requireNonNull(functionName);

        Object runtimeValue =
                module.resolve(functionName);

        if (!(runtimeValue
                instanceof PythonCallable callable)) {

            throw new IllegalArgumentException(
                    "Python value '"
                            + functionName
                            + "' is not callable"
            );
        }

        return invoke(
                callable,
                positional,
                keywords
        );
    }
    public Object invoke(
            PythonCallable callable,
            List<Object> positional,
            Map<String, Object> keywords
    ) {
        Objects.requireNonNull(callable);
        Objects.requireNonNull(positional);
        Objects.requireNonNull(keywords);

        return callable.call(
                new PythonCallArguments(
                        positional,
                        keywords,
                        0
                )
        );
    }
    public TemplateRenderRequest invokeRenderFunction(
            String functionName
    ) {
        return invokeRenderFunction(
                functionName,
                List.of(),
                Map.of()
        );
    }

    public TemplateRenderRequest invokeRenderFunction(
            String functionName,
            List<Object> positional,
            Map<String, Object> keywords
    ) {
        Object result =
                invoke(
                        functionName,
                        positional,
                        keywords
                );

        if (!(result
                instanceof TemplateRenderRequest request)) {

            throw new IllegalStateException(
                    "Python function '"
                            + functionName
                            + "' did not return"
                            + " a TemplateRenderRequest"
            );
        }

        return request;
    }

    public PythonEnvironment moduleEnvironment() {
        return module;
    }
    public FlaskApplication flaskApplication() {
        return flaskBindings.currentApplication();
    }

    private String deriveModuleName(
            Path appSource
    ) {
        String filename =
                appSource.getFileName().toString();

        int extensionIndex =
                filename.lastIndexOf('.');

        return extensionIndex <= 0
                ? filename
                : filename.substring(
                0,
                extensionIndex
        );
    }
    public void beginRequest(
            FlaskRequestData request
    ) {
        flaskBindings.beginRequest(request);
    }

    public void endRequest() {
        flaskBindings.endRequest();
    }
}