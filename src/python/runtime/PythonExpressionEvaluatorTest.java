package python.runtime;

import python.models.atom_statement.*;
import python.models.expr_statement.IDTrailer;

import java.util.Map;

public final class PythonExpressionEvaluatorTest {

    public static void main(String[] args) {
        PythonExpressionEvaluator evaluator =
                new PythonExpressionEvaluator();

        PythonEnvironment environment =
                PythonEnvironment.module();

        python.models.atom_statement.List products =
                new python.models.atom_statement.List(1);

        products.addItem(
                new StringAtom("'Laptop'", 1)
        );

        products.addItem(
                new StringAtom("\"Phone\"", 1)
        );

        Dictionary user =
                new Dictionary(1);

        user.addItem(
                new StringAtom("'name'", 1),
                new StringAtom("'Yousef'", 1)
        );

        user.addItem(
                new StringAtom("'age'", 1),
                new IntegerAtom(24, 1)
        );

        user.addItem(
                new StringAtom("'active'", 1),
                new BoolAtom(true, 1)
        );

        user.addItem(
                new StringAtom("'note'", 1),
                new None(1)
        );

        Dictionary input =
                new Dictionary(1);

        input.addItem(
                new StringAtom("'user'", 1),
                user
        );

        input.addItem(
                new StringAtom("'products'", 1),
                products
        );

        input.addItem(
                new StringAtom("'price'", 1),
                new FloatAtom(999.99f, 1)
        );

        Object result =
                evaluator.evaluate(
                        input,
                        environment
                );

        require(
                result instanceof Map<?, ?>,
                "Dictionary did not resolve to a Java Map"
        );

        Object parenthesized =
                evaluator.evaluate(
                        new ParenAtom(
                                new IntegerAtom(7, 1),
                                1
                        ),
                        environment
                );

        require(
                Integer.valueOf(7).equals(parenthesized),
                "Parenthesized expression was incorrect"
        );
        environment.assign("name", "Yousef");
        environment.assign("age", 24);
        environment.assign("nothing", null);

        Object directIdentifier =
                evaluator.evaluate(
                        new ID("name", 1),
                        environment
                );

        Object trailerIdentifier =
                evaluator.evaluate(
                        new IDTrailer(
                                new ID("age", 1),
                                java.util.List.of(),
                                1
                        ),
                        environment
                );

        Object noneIdentifier =
                evaluator.evaluate(
                        new ID("nothing", 1),
                        environment
                );

        require(
                "Yousef".equals(directIdentifier),
                "Direct identifier lookup failed"
        );

        require(
                Integer.valueOf(24)
                        .equals(trailerIdentifier),
                "IDTrailer identifier lookup failed"
        );

        require(
                noneIdentifier == null,
                "Defined Python None value resolved incorrectly"
        );

        System.out.println(
                "Identifiers: "
                        + directIdentifier
                        + ", "
                        + trailerIdentifier
                        + ", "
                        + noneIdentifier
        );

        System.out.println(
                "PythonExpressionEvaluator test passed."
        );

        System.out.println(result);

        System.out.println(
                "Parenthesized: "
                        + parenthesized
        );
    }

    private static void require(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}