package Jinja2.models.Primary;

import Jinja2.models.expression.Expression;

public abstract class Primary extends Expression {
    protected Primary(String name, int lineNumber) {
        super("Primary." + name, lineNumber);
    }
}
