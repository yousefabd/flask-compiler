package python.runtime;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Stores live values while executing the Python AST.
 * Module values persist for the lifetime of the generated application.
 * Every function invocation receives a fresh local frame.
 */
public final class PythonEnvironment {

    private final PythonEnvironment moduleScope;

    private final Map<String, Object> localValues =
            new LinkedHashMap<>();

    private final Set<String> globalNames =
            new LinkedHashSet<>();

    private PythonEnvironment(
            PythonEnvironment moduleScope
    ) {
        this.moduleScope =
                moduleScope == null
                        ? this
                        : moduleScope;
    }

    /**
     * Creates the persistent module-level environment.
     */
    public static PythonEnvironment module() {
        return new PythonEnvironment(null);
    }

    /**
     * Creates a fresh environment for one function invocation.
     */
    public PythonEnvironment createFunctionFrame() {
        return new PythonEnvironment(moduleScope);
    }

    /**
     * Defines something that must be local, such as a parameter.
     */
    public void defineLocal(
            String name,
            Object value
    ) {
        localValues.put(
                requireValidName(name),
                value
        );
    }

    /**
     * Implements Python's: global products
     */
    public void declareGlobal(String name) {
        globalNames.add(
                requireValidName(name)
        );
    }

    /**
     * Assigns locally unless the name was declared global.
     */
    public void assign(
            String name,
            Object value
    ) {
        String validName =
                requireValidName(name);

        if (!isModuleScope()
                && globalNames.contains(validName)) {

            moduleScope.localValues.put(
                    validName,
                    value
            );

            return;
        }

        localValues.put(
                validName,
                value
        );
    }

    /**
     * Resolves locals first, then persistent module values.
     * containsKey is required because Java null represents Python None.
     */
    public Object resolve(String name) {
        String validName =
                requireValidName(name);

        if (!isModuleScope()
                && globalNames.contains(validName)) {

            return resolveModuleValue(validName);
        }

        if (localValues.containsKey(validName)) {
            return localValues.get(validName);
        }

        if (!isModuleScope()
                && moduleScope.localValues.containsKey(validName)) {

            return moduleScope.localValues.get(validName);
        }

        /*
         * Temporary internal failure. The semantic analyzer will
         * eventually report undefined names before execution.
         */
        throw new IllegalStateException(
                "No Python runtime value exists for '"
                        + validName
                        + "'"
        );
    }

    public boolean contains(String name) {
        String validName =
                requireValidName(name);

        if (!isModuleScope()
                && globalNames.contains(validName)) {

            return moduleScope.localValues.containsKey(
                    validName
            );
        }

        return localValues.containsKey(validName)
                || !isModuleScope()
                && moduleScope.localValues.containsKey(
                validName
        );
    }

    private Object resolveModuleValue(String name) {
        if (moduleScope.localValues.containsKey(name)) {
            return moduleScope.localValues.get(name);
        }

        throw new IllegalStateException(
                "No Python runtime value exists for '"
                        + name
                        + "'"
        );
    }

    private boolean isModuleScope() {
        return this == moduleScope;
    }

    private static String requireValidName(
            String name
    ) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Python variable name cannot be blank"
            );
        }

        return name;
    }
}