package python.semantic;

/** Conservative static types used by the Python semantic checker. */
public enum PythonType {
    UNKNOWN,
    NONE,
    BOOLEAN,
    INTEGER,
    FLOAT,
    NUMBER,
    STRING,
    LIST,
    DICT,
    SET,
    CALLABLE;

    public boolean isIntegral() {
        return this == INTEGER || this == BOOLEAN;
    }

    public boolean isNumeric() {
        return isIntegral() || this == FLOAT || this == NUMBER;
    }

    public boolean isIndexable() {
        return this == STRING || this == LIST || this == DICT;
    }

    public boolean isIterable() {
        return isIndexable() || this == SET;
    }
}
