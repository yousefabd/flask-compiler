package python.semantic;

import java.util.Map;
import java.util.Set;

/**
 * Names that are always available to a Python module without being declared.
 *
 * <p>Used by {@link NameResolver} so that {@code print}, {@code len},
 * {@code range}, {@code int} and friends are never reported as undefined
 * variables.</p>
 */
public final class PythonBuiltins {

    private PythonBuiltins() {
    }

    private static final Set<String> NAMES = Set.of(
            // constants
            "True", "False", "None", "NotImplemented", "Ellipsis",
            // module dunders that exist at import time
            "__name__", "__file__", "__doc__",
            // constructors / conversions
            "int", "float", "str", "bool", "list", "dict", "set", "tuple",
            "bytes", "frozenset", "complex", "object", "type",
            // common functions
            "print", "len", "range", "enumerate", "zip", "sorted", "reversed",
            "sum", "min", "max", "abs", "round", "any", "all", "map", "filter",
            "isinstance", "issubclass", "getattr", "setattr", "hasattr",
            "repr", "format", "input", "open", "iter", "next", "id", "hash",
            "divmod", "pow", "chr", "ord", "callable", "vars", "dir",
            // exceptions used in ordinary Flask code
            "Exception", "BaseException", "ValueError", "TypeError",
            "KeyError", "IndexError", "AttributeError", "NameError",
            "ZeroDivisionError", "StopIteration", "RuntimeError", "OSError"
    );

    /**
     * Return type of a builtin when called. Only entries that are provably
     * exact are listed; everything else resolves to {@link PythonType#ANY}
     * so no check is performed on it.
     */
    private static final Map<String, PythonType> CALL_RESULTS = Map.of(
            "len", PythonType.INT,
            "str", PythonType.STRING,
            "int", PythonType.INT,
            "float", PythonType.FLOAT,
            "bool", PythonType.BOOL,
            "list", PythonType.LIST,
            "dict", PythonType.DICT,
            "set", PythonType.SET,
            "sorted", PythonType.LIST,
            "repr", PythonType.STRING
    );

    public static boolean contains(String name) {
        return NAMES.contains(name);
    }

    public static Set<String> names() {
        return NAMES;
    }

    /** Result type of calling {@code name}, or ANY when not provable. */
    public static PythonType callResult(String name) {
        return CALL_RESULTS.getOrDefault(name, PythonType.ANY);
    }
}
