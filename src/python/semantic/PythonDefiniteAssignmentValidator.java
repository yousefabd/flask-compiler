package python.semantic;

import errors.CompilerProblem;
import errors.CompilerStage;
import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.atom_statement.ID;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.compound_statement.IfStatement;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.ReturnStatement;
import python.models.small_statement.SmallStatement;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.symbol_table.Scope;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolTable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Reports reads of local symbols before execution assigns them a value.
 */
public final class PythonDefiniteAssignmentValidator {

    private final String sourceFile;
    private final SymbolTable symbolTable;
    private final Map<ID, Symbol> bindings;
    private final List<CompilerProblem> diagnostics;

    private final Set<ID> reported =
            Collections.newSetFromMap(
                    new IdentityHashMap<>()
            );

    public PythonDefiniteAssignmentValidator(
            String sourceFile,
            SymbolTable symbolTable,
            Map<ID, Symbol> bindings,
            List<CompilerProblem> diagnostics
    ) {
        this.sourceFile =
                Objects.requireNonNull(sourceFile);

        this.symbolTable =
                Objects.requireNonNull(symbolTable);

        this.bindings =
                Objects.requireNonNull(bindings);

        this.diagnostics =
                Objects.requireNonNull(diagnostics);
    }

    public void validate(Program program) {
        Objects.requireNonNull(program);

        visitStatements(
                program.statements,
                symbolTable.getModuleScope(),
                newInitializedSet()
        );
    }

    private void visitStatements(
            List<Statement> statements,
            Scope scope,
            Set<Symbol> initialized
    ) {
        for (Statement statement : statements) {
            visitStatement(
                    statement,
                    scope,
                    initialized
            );
        }
    }

    private void visitStatement(
            Statement statement,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (statement instanceof SimpleStatement simple) {
            for (SmallStatement small :
                    simple.smallStatementList) {

                visitSmallStatement(
                        small,
                        scope,
                        initialized
                );
            }

            return;
        }

        if (statement
                instanceof DecoratorStatement decorated) {

            visitFunction(
                    decorated,
                    scope,
                    initialized
            );

            return;
        }

        if (statement instanceof IfStatement conditional) {
            visitIfStatement(
                    conditional,
                    scope,
                    initialized
            );
        }

        // For and while are added next.
    }

    private void visitSmallStatement(
            SmallStatement statement,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (statement
                instanceof ExpressionStatement expression) {

            visitExpressionStatement(
                    expression,
                    scope,
                    initialized
            );

            return;
        }

        if (statement
                instanceof AugAssignStatement augmented) {

            validateRead(
                    augmented.id,
                    scope,
                    initialized
            );

            visitCondition(
                    augmented.expression,
                    scope,
                    initialized
            );

            markInitialized(
                    augmented.id,
                    scope,
                    initialized
            );

            return;
        }

        if (statement
                instanceof ReturnStatement returned) {

            for (Condition value : returned.conditions) {
                visitCondition(
                        value,
                        scope,
                        initialized
                );
            }

            return;
        }

        if (statement instanceof ImportStatement imported) {
            markImport(
                    imported,
                    scope,
                    initialized
            );
        }
    }

    private void visitExpressionStatement(
            ExpressionStatement expression,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (!expression.isAssignment()) {
            for (Condition value :
                    expression.getExpressions()) {

                visitCondition(
                        value,
                        scope,
                        initialized
                );
            }

            return;
        }

        /*
         * Python evaluates the right-hand sides before
         * assigning anything to the targets.
         */
        for (Condition value : expression.getValues()) {
            visitCondition(
                    value,
                    scope,
                    initialized
            );
        }

        for (Condition target : expression.getTargets()) {
            visitTarget(
                    target,
                    scope,
                    initialized
            );
        }
    }

    private void visitFunction(
            DecoratorStatement decorated,
            Scope enclosingScope,
            Set<Symbol> enclosingInitialized
    ) {
        if (decorated.decorators != null) {
            for (Decorator decorator :
                    decorated.decorators) {

                visitDecorator(
                        decorator,
                        enclosingScope,
                        enclosingInitialized
                );
            }
        }

        FunctionDef function =
                decorated.function;

        /*
         * Defaults and annotations execute before the
         * function name itself becomes initialized.
         */
        for (Parameter parameter :
                function.parameters) {

            visitCondition(
                    parameter.type,
                    enclosingScope,
                    enclosingInitialized
            );

            visitCondition(
                    parameter.defaultValue,
                    enclosingScope,
                    enclosingInitialized
            );
        }

        visitCondition(
                function.returnType,
                enclosingScope,
                enclosingInitialized
        );

        markInitialized(
                function.id,
                enclosingScope,
                enclosingInitialized
        );

        Scope functionScope =
                symbolTable.getFunctionScope(function);

        if (functionScope == null) {
            /*
             * Name resolution already reports the missing
             * function scope as an internal semantic problem.
             */
            return;
        }

        Set<Symbol> functionInitialized =
                newInitializedSet();

        /*
         * Parameters already contain values whenever the
         * function body begins executing.
         */
        for (Parameter parameter :
                function.parameters) {

            markInitialized(
                    parameter.id,
                    functionScope,
                    functionInitialized
            );
        }

        visitStatements(
                function.body.statements,
                functionScope,
                functionInitialized
        );
    }

    private void visitDecorator(
            Decorator decorator,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (decorator.dottedName != null
                && !decorator.dottedName.isEmpty()) {

            /*
             * For @app.route(...), app is a variable read.
             * route is an attribute name, not another variable.
             */
            validateRead(
                    decorator.dottedName.getFirst(),
                    scope,
                    initialized
            );
        }

        if (decorator.arguments != null) {
            for (Argument argument :
                    decorator.arguments) {

                visitArgument(
                        argument,
                        scope,
                        initialized
                );
            }
        }
    }

    private void markImport(
            ImportStatement statement,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (statement
                instanceof SimpleImportStatement simple) {

            ID name =
                    simple.getBoundName();

            if (name != null) {
                markInitialized(
                        name,
                        scope,
                        initialized
                );
            }

            return;
        }

        if (statement
                instanceof FromImportStatement from) {

            for (ID name : from.getBoundNames()) {
                markInitialized(
                        name,
                        scope,
                        initialized
                );
            }
        }
    }

    private void visitTarget(
            Condition target,
            Scope scope,
            Set<Symbol> initialized
    ) {
        if (target instanceof IDTrailer expression) {
            if (expression.trailers == null
                    || expression.trailers.isEmpty()) {

                markInitialized(
                        expression.id,
                        scope,
                        initialized
                );
            } else {
                /*
                 * items[index] = value reads items and index.
                 * It does not create a new variable named items.
                 */
                visitIdentifierExpression(
                        expression,
                        scope,
                        initialized
                );
            }

            return;
        }

        if (target instanceof ID identifier) {
            markInitialized(
                    identifier,
                    scope,
                    initialized
            );

            return;
        }

        /*
         * Handles parenthesized and unpacking targets using
         * their existing AST children.
         */
        for (ASTNode child : target.getChildren()) {
            if (child instanceof Condition condition) {
                visitTarget(
                        condition,
                        scope,
                        initialized
                );
            }
        }
    }

    private void visitCondition(
            Condition condition,
            Scope scope,
            Set<Symbol> initialized
    ) {
        switch (condition) {
            case null -> {
                return;
            }
            case IDTrailer expression -> {
                visitIdentifierExpression(
                        expression,
                        scope,
                        initialized
                );

                return;
            }
            case ID identifier -> {
                validateRead(
                        identifier,
                        scope,
                        initialized
                );

                return;
            }
            default -> {
            }
        }

        /*
         * Binary, unary, comparison, list, dictionary,
         * set and parenthesized expressions expose their
         * nested expressions through getChildren().
         */
        for (ASTNode child : condition.getChildren()) {
            if (child instanceof Condition childCondition) {
                visitCondition(
                        childCondition,
                        scope,
                        initialized
                );
            }
        }
    }

    private void visitIdentifierExpression(
            IDTrailer expression,
            Scope scope,
            Set<Symbol> initialized
    ) {
        validateRead(
                expression.id,
                scope,
                initialized
        );

        if (expression.trailers == null) {
            return;
        }

        for (Trailer trailer : expression.trailers) {
            if (trailer.arguments
                    instanceof CallArguments calls) {

                for (Argument argument : calls.args) {
                    visitArgument(
                            argument,
                            scope,
                            initialized
                    );
                }
            } else if (trailer.arguments
                    instanceof SubscriptArguments subscripts) {

                for (Condition subscript :
                        subscripts.conditions) {

                    visitCondition(
                            subscript,
                            scope,
                            initialized
                    );
                }
            }
        }
    }

    private void visitArgument(
            Argument argument,
            Scope scope,
            Set<Symbol> initialized
    ) {
        /*
         * In page=value, page is a keyword label.
         * Only value is a variable expression.
         */
        Condition value =
                argument.isAssigned()
                        ? argument.assign
                        : argument.arg;

        visitCondition(
                value,
                scope,
                initialized
        );
    }

    private void validateRead(
            ID identifier,
            Scope scope,
            Set<Symbol> initialized
    ) {
        Symbol symbol =
                bindings.get(identifier);

        /*
         * NameResolver already reports completely missing
         * names and names inaccessible from this scope.
         */
        if (symbol == null) {
            return;
        }

        /*
         * Only values owned by the current execution frame
         * are checked here.
         *
         * A module value referenced by a function may become
         * initialized before that function is called.
         */
        boolean belongsToCurrentScope =
                scope.resolveLocal(identifier.name)
                        == symbol;

        if (!belongsToCurrentScope
                || initialized.contains(symbol)) {

            return;
        }

        if (reported.add(identifier)) {
            diagnostics.add(
                    new CompilerProblem(
                            CompilerStage.SEMANTIC_ANALYSIS,
                            "UNDEFINED_VARIABLE",
                            sourceFile,
                            identifier.getLine(),
                            "Variable '"
                                    + identifier.name
                                    + "' is read before assignment"
                    )
            );
        }
    }

    private void markInitialized(
            ID identifier,
            Scope scope,
            Set<Symbol> initialized
    ) {
        Symbol symbol =
                bindings.get(identifier);

        if (symbol != null
                && scope.resolveLocal(identifier.name)
                == symbol) {

            initialized.add(symbol);
        }
    }

    private static Set<Symbol> newInitializedSet() {
        return Collections.newSetFromMap(
                new IdentityHashMap<>()
        );
    }
    private void visitIfStatement(
            IfStatement conditional,
            Scope scope,
            Set<Symbol> initialized
    ) {
        /*
         * Every branch begins with exactly the state that
         * existed before entering the if statement.
         */
        Set<Symbol> incoming =
                copyInitializedSet(initialized);

        Set<Symbol> branchIntersection =
                null;

        for (int index = 0;
             index < conditional.conditions.size();
             index++) {

            /*
             * An elif condition cannot rely on assignments made
             * by a previous branch because that branch only runs
             * when its own condition is true.
             */
            visitCondition(
                    conditional.conditions.get(index),
                    scope,
                    incoming
            );

            Set<Symbol> branchState =
                    copyInitializedSet(incoming);

            visitStatements(
                    conditional.bodies.get(index).statements,
                    scope,
                    branchState
            );

            branchIntersection =
                    intersectBranchStates(
                            branchIntersection,
                            branchState
                    );
        }

        if (conditional.last != null) {
            Set<Symbol> elseState =
                    copyInitializedSet(incoming);

            visitStatements(
                    conditional.last.statements,
                    scope,
                    elseState
            );

            branchIntersection =
                    intersectBranchStates(
                            branchIntersection,
                            elseState
                    );
        } else {
            /*
             * With no else, none of the conditions may be true.
             * Therefore, the original incoming state is also
             * one possible path.
             */
            branchIntersection =
                    intersectBranchStates(
                            branchIntersection,
                            incoming
                    );
        }

        initialized.clear();

        if (branchIntersection != null) {
            initialized.addAll(branchIntersection);
        }
    }

    private Set<Symbol> intersectBranchStates(
            Set<Symbol> currentIntersection,
            Set<Symbol> branchState
    ) {
        if (currentIntersection == null) {
            return copyInitializedSet(branchState);
        }

        currentIntersection.retainAll(branchState);
        return currentIntersection;
    }

    private static Set<Symbol> copyInitializedSet(
            Set<Symbol> source
    ) {
        Set<Symbol> copy =
                newInitializedSet();

        copy.addAll(source);
        return copy;
    }
}