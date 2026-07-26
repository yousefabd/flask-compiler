package jinja2.renderer;

import java.util.LinkedHashMap;
import java.util.Map;

public class RenderContext {
    private final RenderContext parent;
    private final Map<String, Object> localValues;

    private RenderContext(RenderContext parent) {
        this.parent = parent;
        this.localValues = new LinkedHashMap<>();
    }

    public static RenderContext root(Map<String, ?> initialValues) {
        RenderContext context = new RenderContext(null);
        context.localValues.putAll(initialValues);
        return context;
    }

    public RenderContext child() {
        return new RenderContext(this);
    }

    public void setLocal(String name, Object value) {
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
                "No render value was provided for '" + name + "'"
        );
    }

    public boolean contains(String name) {
        if (localValues.containsKey(name)) {
            return true;
        }

        return parent != null && parent.contains(name);
    }
}
