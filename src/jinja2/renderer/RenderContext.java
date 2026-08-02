package jinja2.renderer;

import jinja2.runtime.RenderEnvironment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RenderContext {

    private final RenderContext parent;
    private final Map<String, Object> localValues;
    private final RenderEnvironment environment;

    private RenderContext(
            RenderContext parent,
            RenderEnvironment environment
    ) {
        this.parent = parent;

        this.environment =
                Objects.requireNonNull(environment);

        this.localValues =
                new LinkedHashMap<>();
    }

    public static RenderContext root(
            Map<String, ?> initialValues,
            RenderEnvironment environment
    ) {
        RenderContext context =
                new RenderContext(
                        null,
                        environment
                );

        context.localValues.putAll(initialValues);

        return context;
    }

    /*
     * Keeps older evaluator tests working when they do not need
     * any Flask runtime information.
     */
    public static RenderContext root(
            Map<String, ?> initialValues
    ) {
        return root(
                initialValues,
                RenderEnvironment.empty()
        );
    }

    public RenderContext child() {
        return new RenderContext(
                this,
                environment
        );
    }

    public RenderEnvironment getEnvironment() {
        return environment;
    }

    public void setLocal(
            String name,
            Object value
    ) {
        localValues.put(name, value);
    }

    public Object resolve(String name) {
        if (localValues.containsKey(name)) {
            return localValues.get(name);
        }

        if (parent != null) {
            return parent.resolve(name);
        }

        throw new IllegalStateException(
                "No render value was provided for '"
                        + name
                        + "'"
        );
    }

    public boolean contains(String name) {
        if (localValues.containsKey(name)) {
            return true;
        }

        return parent != null
                && parent.contains(name);
    }
}