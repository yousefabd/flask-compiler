package python.runtime;

/**
 * Internal interpreter control flow.
 * This is not a Python runtime error.
 */
final class PythonReturnSignal
        extends RuntimeException {

    private final Object value;

    PythonReturnSignal(Object value) {
        super(null, null, false, false);
        this.value = value;
    }

    Object value() {
        return value;
    }
}