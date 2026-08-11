package compiler.runtime;

import compiler.generation.TemplateRenderRequest;
import compiler.preparation.PreparedApplication;
import errors.CodeGenError;
import jinja2.models.file.TemplateFile;
import jinja2.renderer.RenderContext;
import jinja2.renderer.TemplateRenderer;
import python.runtime.PythonApplicationRuntime;
import python.runtime.flask.FlaskRoute;
import python.runtime.flask.FlaskRouteMatch;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CompiledApplication {

    private final Path templatesDirectory;
    private final PreparedApplication preparation;
    private final PythonApplicationRuntime pythonRuntime;
    private final TemplateRenderer templateRenderer;

    public CompiledApplication(
            Path appSource,
            Path templatesDirectory,
            PreparedApplication preparation,
            TemplateRenderer templateRenderer
    ) {
        this.templatesDirectory =
                Objects.requireNonNull(templatesDirectory);

        this.preparation =
                Objects.requireNonNull(preparation);

        this.templateRenderer =
                Objects.requireNonNull(templateRenderer);

        this.pythonRuntime =
                new PythonApplicationRuntime(
                        Objects.requireNonNull(appSource),
                        preparation.backend().program()
                );
    }

    public TemplateRenderRequest invokeRenderFunction(
            String functionName
    ) {
        return pythonRuntime.invokeRenderFunction(
                functionName
        );
    }

    public TemplateRenderRequest invokeRenderFunction(
            String functionName,
            List<Object> positional,
            Map<String, Object> keywords
    ) {
        return pythonRuntime.invokeRenderFunction(
                functionName,
                positional,
                keywords
        );
    }

    public Object invokePythonFunction(
            String functionName,
            List<Object> positional,
            Map<String, Object> keywords
    ) {
        return pythonRuntime.invoke(
                functionName,
                positional,
                keywords
        );
    }
    public Object invokeRoute(
            FlaskRouteMatch match
    ) {
        Objects.requireNonNull(match);

        return pythonRuntime.invoke(
                match.route().handler(),
                List.of(),
                match.arguments()
        );
    }
    public List<FlaskRoute> routes() {
        return pythonRuntime
                .flaskApplication()
                .routes();
    }

    public String render(
            TemplateRenderRequest request
    ) {
        Objects.requireNonNull(request);

        Map<String, TemplateFile> templates =
                preparation.frontend().templates();

        TemplateFile template =
                templates.get(request.templateName());

        if (template == null) {
            throw new CodeGenError(
                    templatesDirectory
                            .resolve(request.templateName())
                            .toString(),
                    -1,
                    "The parsed template AST is unavailable"
            );
        }

        RenderContext context =
                RenderContext.root(
                        request.context(),
                        request.environment()
                );

        return templateRenderer.render(
                request.templateName(),
                templates,
                context
        );
    }

    public PreparedApplication preparation() {
        return preparation;
    }
}