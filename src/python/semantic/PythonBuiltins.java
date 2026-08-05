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
            "True", "False", "None", "NotImplemented", "Ellipsis", "__debug__",
            // module dunders that exist at import time
            "__name__", "__file__", "__doc__", "__package__", "__loader__",
            "__spec__", "__builtins__",
            // constructors / conversions
            "int", "float", "str", "bool", "list", "dict", "set", "tuple",
            "bytes", "bytearray", "frozenset", "complex", "object", "type",
            "memoryview", "slice", "super", "staticmethod", "classmethod",
            "property",
            // common functions
            "print", "len", "range", "enumerate", "zip", "sorted", "reversed",
            "sum", "min", "max", "abs", "round", "any", "all", "map", "filter",
            "isinstance", "issubclass", "getattr", "setattr", "hasattr",
            "delattr", "repr", "format", "ascii", "input", "open", "iter",
            "next", "id", "hash", "divmod", "pow", "chr", "ord", "hex", "oct",
            "bin", "callable", "vars", "dir", "globals", "locals", "exec",
            "eval", "compile", "__import__",
            // exceptions used in ordinary Flask code
            "Exception", "BaseException", "ValueError", "TypeError",
            "KeyError", "IndexError", "AttributeError", "NameError",
            "UnboundLocalError", "ZeroDivisionError", "StopIteration",
            "StopAsyncIteration", "RuntimeError", "RecursionError", "OSError",
            "IOError", "ArithmeticError", "LookupError", "ImportError",
            "ModuleNotFoundError", "FileNotFoundError", "FileExistsError",
            "PermissionError", "TimeoutError", "ConnectionError", "EOFError",
            "NotImplementedError", "OverflowError", "AssertionError",
            "SystemExit", "KeyboardInterrupt", "GeneratorExit",
            "Warning", "UserWarning", "DeprecationWarning"
    );

    /**
     * Return type of a builtin when called. Only entries that are provably
     * exact are listed; everything else resolves to {@link PythonType#ANY}
     * so no check is performed on it.
     */
    private static final Map<String, PythonType> CALL_RESULTS = Map.ofEntries(
            Map.entry("len", PythonType.INT),
            Map.entry("str", PythonType.STRING),
            Map.entry("int", PythonType.INT),
            Map.entry("float", PythonType.FLOAT),
            Map.entry("bool", PythonType.BOOL),
            Map.entry("list", PythonType.LIST),
            Map.entry("dict", PythonType.DICT),
            Map.entry("set", PythonType.SET),
            Map.entry("sorted", PythonType.LIST),
            Map.entry("repr", PythonType.STRING),
            Map.entry("hex", PythonType.STRING),
            Map.entry("oct", PythonType.STRING),
            Map.entry("bin", PythonType.STRING),
            Map.entry("ascii", PythonType.STRING),
            Map.entry("chr", PythonType.STRING),
            Map.entry("ord", PythonType.INT)
            // `abs`/`round` are deliberately NOT here: their return type
            // depends on the argument's type (abs(-1) is int, abs(-1.5) is
            // float) — not provable without looking at the call site, so they
            // stay ANY via the getOrDefault below rather than being guessed.
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
