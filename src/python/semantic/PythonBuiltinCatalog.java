package python.semantic;

import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Immutable semantic metadata; it is independent of runtime environments. */
final class PythonBuiltinCatalog {
    private static final Map<String, PythonType> TYPES;

    static {
        Map<String, PythonType> types = new LinkedHashMap<>();
        types.put("__name__", PythonType.STRING);
        types.put("float", PythonType.CALLABLE);
        TYPES = Collections.unmodifiableMap(types);
    }

    private PythonBuiltinCatalog() { }

    static Map<String, Symbol> createSymbols() {
        Map<String, Symbol> symbols = new LinkedHashMap<>();
        for (String name : TYPES.keySet()) {
            symbols.put(name, new Symbol(name, SymbolKind.BUILTIN, -1));
        }
        return Collections.unmodifiableMap(symbols);
    }

    static PythonType typeOf(String name) {
        return TYPES.getOrDefault(name, PythonType.UNKNOWN);
    }
}
