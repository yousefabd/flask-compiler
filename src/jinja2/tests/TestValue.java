package jinja2.tests;

/*
 * Keeps "undefined" separate from a defined value containing null.
 */
public record TestValue(
        boolean defined,
        Object value
) {
    public static TestValue defined(Object value) {
        return new TestValue(true, value);
    }

    public static TestValue undefined() {
        return new TestValue(false, null);
    }
}