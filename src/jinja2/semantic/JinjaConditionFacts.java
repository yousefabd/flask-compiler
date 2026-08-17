package jinja2.semantic;

import jinja2.models.expression.BinaryExpressionNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.expression.IndexAccessNode;
import jinja2.models.expression.Operation;
import jinja2.models.expression.PropertyAccessNode;
import jinja2.models.expression.TestExpressionNode;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Extracts facts guaranteed by a true Jinja condition.
 */
public final class JinjaConditionFacts {

    private JinjaConditionFacts() {
    }

    public static Set<String> definedWhenTrue(
            ExpressionNode condition
    ) {
        Set<String> result =
                new LinkedHashSet<>();

        collectDefinedWhenTrue(
                condition,
                result
        );

        return Collections.unmodifiableSet(result);
    }

    private static void collectDefinedWhenTrue(
            ExpressionNode condition,
            Set<String> result
    ) {
        if (condition instanceof TestExpressionNode test) {
            if (testGuaranteesDefinition(test)) {
                IdentifierNode root =
                        findRootIdentifier(
                                test.getValue()
                        );

                if (root != null) {
                    result.add(root.getName());
                }
            }

            return;
        }

        /*
         * For A and B to be true, both A and B must be true.
         * Therefore their guaranteed facts can be combined.
         */
        if (condition instanceof BinaryExpressionNode binary
                && binary.getOperation() == Operation.AND) {

            collectDefinedWhenTrue(
                    binary.getLeft(),
                    result
            );

            collectDefinedWhenTrue(
                    binary.getRight(),
                    result
            );
        }

        /*
         * Do not extract facts from OR:
         *
         *     value is defined or fallback
         *
         * The condition may be true while value is undefined.
         */
    }

    private static boolean testGuaranteesDefinition(
            TestExpressionNode test
    ) {
        return "defined".equals(test.getTestName())
                && !test.isNegated()
                || "undefined".equals(test.getTestName())
                && test.isNegated();
    }

    private static IdentifierNode findRootIdentifier(
            ExpressionNode expression
    ) {
        if (expression instanceof IdentifierNode identifier) {
            return identifier;
        }

        if (expression instanceof PropertyAccessNode property) {
            return findRootIdentifier(
                    property.getTarget()
            );
        }

        if (expression instanceof IndexAccessNode index) {
            return findRootIdentifier(
                    index.getTarget()
            );
        }

        return null;
    }
}