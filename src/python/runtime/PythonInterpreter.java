package python.runtime;

import python.models.Import_statement.ImportStatement;
import python.models.atom_statement.ID;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.SmallStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PythonInterpreter {

    private final PythonExpressionEvaluator expressionEvaluator;

    public PythonInterpreter(
            PythonExpressionEvaluator expressionEvaluator
    ) {
        this.expressionEvaluator =
                Objects.requireNonNull(expressionEvaluator);
    }

    /**
     * Executes module-level statements into the persistent
     * module environment.
     */
    public void executeModule(
            Program program,
            PythonEnvironment moduleEnvironment
    ) {
        Objects.requireNonNull(program);
        Objects.requireNonNull(moduleEnvironment);

        for (Statement statement : program.statements) {
            executeStatement(
                    statement,
                    moduleEnvironment
            );
        }
    }

    private void executeStatement(
            Statement statement,
            PythonEnvironment environment
    ) {
        switch (statement) {
            case SimpleStatement simpleStatement ->
                    executeSimpleStatement(
                            simpleStatement,
                            environment
                    );

            default ->
                    throw new UnsupportedOperationException(
                            "Python statement is not supported yet: "
                                    + statement.getSimpleName()
                                    + " at line "
                                    + statement.getLine()
                    );
        }
    }

    private void executeSimpleStatement(
            SimpleStatement statement,
            PythonEnvironment environment
    ) {
        for (SmallStatement smallStatement
                : statement.smallStatementList) {

            executeSmallStatement(
                    smallStatement,
                    environment
            );
        }
    }

    private void executeSmallStatement(
            SmallStatement statement,
            PythonEnvironment environment
    ) {
        switch (statement) {
            case ExpressionStatement expressionStatement ->
                    executeExpressionStatement(
                            expressionStatement,
                            environment
                    );
            case ImportStatement ignored -> {
            }

            default ->
                    throw new UnsupportedOperationException(
                            "Python small statement is not supported yet: "
                                    + statement.getSimpleName()
                                    + " at line "
                                    + statement.getLine()
                    );
        }
    }

    private void executeExpressionStatement(
            ExpressionStatement statement,
            PythonEnvironment environment
    ) {
        /*
         * An expression statement without "=":
         *
         *     products.append(product)
         *
         * Calls are unsupported for now, but evaluating it here
         * prepares that behavior.
         */
        if (!statement.HaveEquals()) {
            for (Condition expression : statement.conditions) {
                expressionEvaluator.evaluate(
                        expression,
                        environment
                );
            }

            return;
        }

        if (statement.assigns == null) {
            throw new IllegalStateException(
                    "Assignment has no right-hand value at line "
                            + statement.getLine()
            );
        }

        if (statement.conditions.size()
                != statement.assigns.size()) {

            throw new UnsupportedOperationException(
                    "Python assignment unpacking is not supported yet"
                            + " at line "
                            + statement.getLine()
            );
        }

        /*
         * Evaluate every right side before assigning any target.
         * This preserves parallel-assignment behavior such as:
         *
         *     first, second = second, first
         */
        List<Object> resolvedValues =
                new ArrayList<>();

        for (Condition valueExpression
                : statement.assigns) {

            resolvedValues.add(
                    expressionEvaluator.evaluate(
                            valueExpression,
                            environment
                    )
            );
        }

        for (int index = 0;
             index < statement.conditions.size();
             index++) {

            assignTarget(
                    statement.conditions.get(index),
                    resolvedValues.get(index),
                    environment
            );
        }
    }

    private void assignTarget(
            Condition target,
            Object value,
            PythonEnvironment environment
    ) {
        if (target instanceof ID identifier) {
            environment.assign(
                    identifier.name,
                    value
            );

            return;
        }

        if (target instanceof IDTrailer identifierTrailer
                && (identifierTrailer.trailers == null
                || identifierTrailer.trailers.isEmpty())) {

            environment.assign(
                    identifierTrailer.id.name,
                    value
            );

            return;
        }

        throw new UnsupportedOperationException(
                "Python assignment target is not supported yet: "
                        + target.getSimpleName()
                        + " at line "
                        + target.getLine()
        );
    }
}