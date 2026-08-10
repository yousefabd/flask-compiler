package python.runtime.flask;

import compiler.generation.TemplateRenderRequest;
import jinja2.runtime.RenderEnvironment;
import python.runtime.PythonCallArguments;
import python.runtime.PythonCallable;
import python.runtime.PythonEnvironment;
import python.runtime.PythonNativeFunction;

import java.util.Objects;
import java.util.function.Supplier;

public final class FlaskRuntimeBindings {

    private final Supplier<RenderEnvironment>
            renderEnvironmentSupplier;

    private final PythonCallable flaskConstructor;

    private final PythonCallable renderTemplateFunction;
    public FlaskRuntimeBindings() {
        this(RenderEnvironment::empty);
    }

    public FlaskRuntimeBindings(
            Supplier<RenderEnvironment>
                    renderEnvironmentSupplier
    ) {
        this.renderEnvironmentSupplier =
                Objects.requireNonNull(
                        renderEnvironmentSupplier
                );

        this.flaskConstructor =
                new PythonNativeFunction(
                        "Flask",
                        this::createFlaskApplication
                );

        this.renderTemplateFunction =
                new PythonNativeFunction(
                        "render_template",
                        this::renderTemplate
                );
    }

    public void installInto(
            PythonEnvironment module
    ) {
        Objects.requireNonNull(module);

        module.defineLocal(
                "Flask",
                flaskConstructor
        );

        module.defineLocal(
                "render_template",
                renderTemplateFunction
        );
    }

    private Object createFlaskApplication(
            PythonCallArguments arguments
    ) {
        if (!arguments.keywords().isEmpty()
                || arguments.positional().size() != 1) {

            throw new IllegalArgumentException(
                    "Flask() expects exactly one"
                            + " positional argument"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object importName =
                arguments.positional().getFirst();

        if (!(importName instanceof String name)) {
            throw new IllegalArgumentException(
                    "Flask() import name must be a string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        return new FlaskApplication(name);
    }
    private Object renderTemplate(
            PythonCallArguments arguments
    ) {
        if (arguments.positional().size() != 1) {
            throw new IllegalArgumentException(
                    "render_template() expects exactly"
                            + " one positional template name"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Object templateValue =
                arguments.positional().get(0);

        if (!(templateValue instanceof String templateName)
                || templateName.isBlank()) {

            throw new IllegalArgumentException(
                    "render_template() template name"
                            + " must be a non-empty string"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        RenderEnvironment renderEnvironment =
                Objects.requireNonNull(
                        renderEnvironmentSupplier.get(),
                        "Render environment supplier returned null"
                );

        return new TemplateRenderRequest(
                templateName,
                arguments.keywords(),
                renderEnvironment
        );
    }
}