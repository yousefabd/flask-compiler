package python.runtime;

import python.models.atom_statement.*;
import python.models.expr_statement.Expression;
import python.models.expr_statement.IDTrailer;
import utils.CompilerUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PythonExpressionEvaluator {

    public Object evaluate(
            Expression expression,
            PythonEnvironment environment
    ) {
        Objects.requireNonNull(expression);
        Objects.requireNonNull(environment);

        return switch (expression) {
            case StringAtom stringAtom ->
                    CompilerUtils.stripStringQuotes(
                            stringAtom.value
                    );

            case IntegerAtom integerAtom ->
                    integerAtom.value;

            case FloatAtom floatAtom ->
                    floatAtom.value;

            case BoolAtom boolAtom ->
                    boolAtom.value;

            case None ignored ->
                    null;

            case ParenAtom parenAtom ->
                    evaluate(
                            parenAtom.inner,
                            environment
                    );

            case python.models.atom_statement.List list ->
                    evaluateList(
                            list,
                            environment
                    );

            case Dictionary dictionary ->
                    evaluateDictionary(
                            dictionary,
                            environment
                    );
            case ID identifier ->
                    environment.resolve(
                            identifier.name
                    );

            case IDTrailer identifierTrailer ->
                    evaluateIdentifierTrailer(
                            identifierTrailer,
                            environment
                    );

            default ->
                    throw new UnsupportedOperationException(
                            "Python expression is not supported yet: "
                                    + expression.getSimpleName()
                                    + " at line "
                                    + expression.getLine()
                    );
        };
    }

    private ArrayList<Object> evaluateList(
            python.models.atom_statement.List list,
            PythonEnvironment environment
    ) {
        ArrayList<Object> values =
                new ArrayList<>();

        for (Expression element : list.content) {
            values.add(
                    evaluate(
                            element,
                            environment
                    )
            );
        }

        return values;
    }

    private Map<Object, Object> evaluateDictionary(
            Dictionary dictionary,
            PythonEnvironment environment
    ) {
        if (dictionary.keys.size()
                != dictionary.values.size()) {

            throw new IllegalStateException(
                    "Malformed Python dictionary at line "
                            + dictionary.getLine()
            );
        }

        Map<Object, Object> values =
                new LinkedHashMap<>();

        for (int index = 0;
             index < dictionary.keys.size();
             index++) {

            Object key =
                    evaluate(
                            dictionary.keys.get(index),
                            environment
                    );

            Object value =
                    evaluate(
                            dictionary.values.get(index),
                            environment
                    );

            /*
             * Repeated keys replace earlier values,
             * matching Python dictionary behavior.
             */
            values.put(key, value);
        }

        return values;
    }
    private Object evaluateIdentifierTrailer(
            IDTrailer expression,
            PythonEnvironment environment
    ) {
        if (expression.trailers != null
                && !expression.trailers.isEmpty()) {

            throw new UnsupportedOperationException(
                    "Python trailers are not supported yet at line "
                            + expression.getLine()
            );
        }

        return environment.resolve(
                expression.id.name
        );
    }
}