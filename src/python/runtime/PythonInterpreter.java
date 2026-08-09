package python.runtime;

import python.models.Import_statement.ImportStatement;
import python.models.atom_statement.ID;
import python.models.compound_statement.Body;
import python.models.compound_statement.DecoratorStatement;
import python.models.compound_statement.ForStatement;
import python.models.compound_statement.IfStatement;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.GlobalStatement;
import python.models.small_statement.ReturnStatement;
import python.models.small_statement.SmallStatement;

import java.lang.reflect.Array;
import java.util.*;

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

            case DecoratorStatement decoratorStatement ->
                    registerFunction(
                            decoratorStatement,
                            environment
                    );
            case IfStatement ifStatement ->
                    executeIfStatement(
                            ifStatement,
                            environment
                    );
            case ForStatement forStatement ->
                    executeForStatement(
                            forStatement,
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
            case ReturnStatement returnStatement ->
                    executeReturnStatement(
                            returnStatement,
                            environment
                    );
            case GlobalStatement globalStatement ->
                    executeGlobalStatement(
                            globalStatement,
                            environment
                    );

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
    private void registerFunction(
            DecoratorStatement statement,
            PythonEnvironment environment
    ) {
        FunctionDef definition =
                statement.function;

        if (definition == null) {
            throw new IllegalStateException(
                    "Function statement has no definition"
                            + " at line "
                            + statement.getLine()
            );
        }

        PythonFunction function =
                new PythonFunction(
                        definition,
                        environment,
                        this
                );

        environment.assign(
                function.name(),
                function
        );
    }
    private void executeBody(
            Body body,
            PythonEnvironment environment
    ) {
        if (body == null || body.statements == null) {
            return;
        }

        for (var statement : body.statements) {
            executeStatement(
                    statement,
                    environment
            );
        }
    }

    private void executeReturnStatement(
            ReturnStatement statement,
            PythonEnvironment environment
    ) {
        if (statement.conditions == null
                || statement.conditions.isEmpty()) {

            throw new PythonReturnSignal(null);
        }

        if (statement.conditions.size() != 1) {
            throw new UnsupportedOperationException(
                    "Returning multiple values is not supported yet"
                            + " at line "
                            + statement.getLine()
            );
        }

        Object value =
                expressionEvaluator.evaluate(
                        statement.conditions.getFirst(),
                        environment
                );

        throw new PythonReturnSignal(value);
    }

    Object invokeFunction(
            PythonFunction function,
            PythonCallArguments arguments
    ) {
        PythonEnvironment frame =
                function.definingEnvironment()
                        .createFunctionFrame();

        bindArguments(
                function,
                arguments,
                frame
        );

        try {
            executeBody(
                    function.definition().body,
                    frame
            );
        } catch (PythonReturnSignal signal) {
            return signal.value();
        }

        // A Python function without an explicit return returns None.
        return null;
    }

    private void bindArguments(
            PythonFunction function,
            PythonCallArguments arguments,
            PythonEnvironment frame
    ) {
        FunctionDef definition =
                function.definition();

        List<Parameter> parameters =
                definition.parameters == null
                        ? List.of()
                        : definition.parameters;

        List<Object> positional =
                arguments.positional();

        Map<String, Object> keywords =
                arguments.keywords();

        if (positional.size() > parameters.size()) {
            throw new IllegalArgumentException(
                    "Function '"
                            + function.name()
                            + "' received too many positional arguments"
                            + " at line "
                            + arguments.sourceLine()
            );
        }

        Set<String> boundNames =
                new LinkedHashSet<>();

        for (int index = 0;
             index < positional.size();
             index++) {

            String parameterName =
                    parameters.get(index).id.name;

            frame.defineLocal(
                    parameterName,
                    positional.get(index)
            );

            boundNames.add(parameterName);
        }

        for (Map.Entry<String, Object> keyword
                : keywords.entrySet()) {

            Parameter parameter =
                    findParameter(
                            parameters,
                            keyword.getKey()
                    );

            if (parameter == null) {
                throw new IllegalArgumentException(
                        "Function '"
                                + function.name()
                                + "' has no parameter named '"
                                + keyword.getKey()
                                + "'"
                );
            }

            String parameterName =
                    parameter.id.name;

            if (!boundNames.add(parameterName)) {
                throw new IllegalArgumentException(
                        "Function '"
                                + function.name()
                                + "' received multiple values for '"
                                + parameterName
                                + "'"
                );
            }

            frame.defineLocal(
                    parameterName,
                    keyword.getValue()
            );
        }

        for (Parameter parameter : parameters) {
            String parameterName =
                    parameter.id.name;

            if (boundNames.contains(parameterName)) {
                continue;
            }

            if (parameter.hasDefaultValue()) {
                throw new UnsupportedOperationException(
                        "Default parameter values are not supported yet"
                                + " for function '"
                                + function.name()
                                + "'"
                );
            }

            throw new IllegalArgumentException(
                    "Function '"
                            + function.name()
                            + "' is missing argument '"
                            + parameterName
                            + "'"
            );
        }
    }

    private Parameter findParameter(
            List<Parameter> parameters,
            String name
    ) {
        for (Parameter parameter : parameters) {
            if (parameter.id.name.equals(name)) {
                return parameter;
            }
        }

        return null;
    }
    private void executeIfStatement(
            IfStatement statement,
            PythonEnvironment environment
    ) {
        if (statement.conditions == null
                || statement.bodies == null
                || statement.conditions.size()
                != statement.bodies.size()) {

            throw new IllegalStateException(
                    "Malformed Python if statement"
                            + " at line "
                            + statement.getLine()
            );
        }

        for (int index = 0;
             index < statement.conditions.size();
             index++) {

            Object conditionValue =
                    expressionEvaluator.evaluate(
                            statement.conditions.get(index),
                            environment
                    );

            if (PythonTruthiness.isTruthy(
                    conditionValue
            )) {
                executeBody(
                        statement.bodies.get(index),
                        environment
                );

                // Only the first true branch executes.
                return;
            }
        }

        if (statement.last != null) {
            executeBody(
                    statement.last,
                    environment
            );
        }
    }
    private void executeForStatement(
            ForStatement statement,
            PythonEnvironment environment
    ) {
        if (statement.iterators == null
                || statement.iterators.isEmpty()) {

            throw new IllegalStateException(
                    "Python for statement has no loop variable"
                            + " at line "
                            + statement.getLine()
            );
        }

        if (statement.iterable == null
                || statement.body == null) {

            throw new IllegalStateException(
                    "Malformed Python for statement"
                            + " at line "
                            + statement.getLine()
            );
        }

        Object iterableValue =
                expressionEvaluator.evaluate(
                        statement.iterable,
                        environment
                );

        List<Object> iterationValues =
                materializeIterationValues(
                        iterableValue,
                        statement.getLine()
                );

        for (Object iterationValue
                : iterationValues) {

            assignLoopVariables(
                    statement.iterators,
                    iterationValue,
                    environment,
                    statement.getLine()
            );

            executeBody(
                    statement.body,
                    environment
            );
        }

        /*
         * Until break is implemented, a normally completed
         * for loop always executes its else body.
         *
         * A return automatically skips this because
         * PythonReturnSignal escapes the method.
         */
        if (statement.last != null) {
            executeBody(
                    statement.last,
                    environment
            );
        }
    }
    private List<Object> materializeIterationValues(
            Object value,
            int line
    ) {
        List<Object> result =
                new ArrayList<>();

        if (value instanceof Map<?, ?> map) {
            // Python dictionary iteration produces keys.
            result.addAll(map.keySet());
            return result;
        }

        if (value instanceof String string) {
            string.codePoints()
                    .mapToObj(codePoint ->
                            new String(
                                    Character.toChars(codePoint)
                            )
                    )
                    .forEach(result::add);

            return result;
        }

        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                result.add(element);
            }

            return result;
        }

        if (value != null
                && value.getClass().isArray()) {

            int length =
                    Array.getLength(value);

            for (int index = 0;
                 index < length;
                 index++) {

                result.add(
                        Array.get(value, index)
                );
            }

            return result;
        }

        throw new UnsupportedOperationException(
                "Python value of type "
                        + runtimeTypeName(value)
                        + " is not iterable"
                        + " at line "
                        + line
        );
    }
    private void assignLoopVariables(
            List<ID> iterators,
            Object iterationValue,
            PythonEnvironment environment,
            int line
    ) {
        if (iterators.size() == 1) {
            environment.assign(
                    iterators.getFirst().name,
                    iterationValue
            );

            return;
        }

        List<Object> unpackedValues =
                unpackIterationValue(
                        iterationValue,
                        line
                );

        if (unpackedValues.size()
                != iterators.size()) {

            throw new IllegalArgumentException(
                    "Cannot unpack "
                            + unpackedValues.size()
                            + " value(s) into "
                            + iterators.size()
                            + " loop variable(s)"
                            + " at line "
                            + line
            );
        }

        for (int index = 0;
             index < iterators.size();
             index++) {

            environment.assign(
                    iterators.get(index).name,
                    unpackedValues.get(index)
            );
        }
    }

    private List<Object> unpackIterationValue(
            Object value,
            int line
    ) {
        if (value instanceof Map.Entry<?, ?> entry) {
            List<Object> pair =
                    new ArrayList<>();

            pair.add(entry.getKey());
            pair.add(entry.getValue());

            return pair;
        }

        return materializeIterationValues(
                value,
                line
        );
    }

    private String runtimeTypeName(Object value) {
        return value == null
                ? "None"
                : value.getClass().getSimpleName();
    }
    private void executeGlobalStatement(
            GlobalStatement statement,
            PythonEnvironment environment
    ) {
        if (statement.names == null
                || statement.names.isEmpty()) {

            throw new IllegalStateException(
                    "Python global statement has no names"
                            + " at line "
                            + statement.getLine()
            );
        }

        for (var name : statement.names) {
            environment.declareGlobal(
                    name.name
            );
        }
    }
}