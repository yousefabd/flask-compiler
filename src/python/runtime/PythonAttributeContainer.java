package python.runtime;

public interface PythonAttributeContainer {

    Object getAttribute(
            String name,
            int sourceLine
    );

    void setAttribute(
            String name,
            Object value,
            int sourceLine
    );
}