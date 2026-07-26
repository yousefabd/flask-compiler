package jinja2.renderer;

import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.expression.literal.BooleanLiteralNode;
import jinja2.models.expression.literal.NoneLiteralNode;
import jinja2.models.expression.literal.NumberLiteralNode;
import jinja2.models.expression.literal.StringLiteralNode;
import utils.CompilerUtils;

public final class ExpressionEvaluator {

    public Object evaluate(
            ExpressionNode expression,
            RenderContext context
    ) {
        return switch (expression) {
            case IdentifierNode identifier ->
                    context.resolve(identifier.getName());

            case StringLiteralNode stringLiteral ->
                    CompilerUtils.stripStringQuotes(
                            stringLiteral.getValue()
                    );

            case NumberLiteralNode numberLiteral ->
                    parseNumber(numberLiteral.getValue());

            case BooleanLiteralNode booleanLiteral ->
                    booleanLiteral.getValue();

            case NoneLiteralNode ignored ->
                    null;

            default -> throw new UnsupportedOperationException(
                    "Expression is not supported yet: "
                            + expression.getClass().getSimpleName()
                            + " at line "
                            + expression.getLineNumber()
            );
        };
    }

    private Number parseNumber(String rawValue) {
        if (rawValue.contains(".")) {
            return Double.parseDouble(rawValue);
        }

        return Long.parseLong(rawValue);
    }
}