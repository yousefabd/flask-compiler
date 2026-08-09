package python.runtime;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class PythonTruthiness {

    private PythonTruthiness() {
    }

    public static boolean isTruthy(Object value) {
        switch (value) {
            case null -> {
                return false;
            }
            case Boolean booleanValue -> {
                return booleanValue;
            }
            case Number number -> {
                return number.doubleValue() != 0.0;
            }
            case String string -> {
                return !string.isEmpty();
            }
            case Collection<?> collection -> {
                return !collection.isEmpty();
            }
            case Map<?, ?> map -> {
                return !map.isEmpty();
            }
            default -> {
            }
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) != 0;
        }

        return true;
    }
}