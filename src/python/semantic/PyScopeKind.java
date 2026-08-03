package python.semantic;

/**
 * The only two scope kinds Python actually has in this language subset.
 *
 * <p>Deliberately does not contain IF/FOR/WHILE. Python has no block scope:
 * a name assigned inside {@code if}, {@code for} or {@code while} belongs to
 * the enclosing function (or module) scope and stays visible after the block
 * ends.</p>
 */
public enum PyScopeKind {
    MODULE,
    FUNCTION
}
