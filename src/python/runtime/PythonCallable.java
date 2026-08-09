package python.runtime;

public interface PythonCallable {

    Object call(PythonCallArguments arguments);
}