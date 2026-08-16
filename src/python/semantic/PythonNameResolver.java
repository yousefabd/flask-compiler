package python.semantic;

import errors.CompilerProblem;
import errors.CompilerStage;
import python.models.ASTNode;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.atom_statement.Dictionary;
import python.models.atom_statement.ID;
import python.models.atom_statement.ParenAtom;
import python.models.compound_statement.Body;
import python.models.compound_statement.Decorator;
import python.models.compound_statement.DecoratorStatement;
import python.models.compound_statement.ForStatement;
import python.models.compound_statement.IfStatement;
import python.models.compound_statement.WhileStatement;
import python.models.expr_statement.BinaryExpression;
import python.models.expr_statement.CompoundCondition;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.expr_statement.RelationalComparison;
import python.models.expr_statement.UnaryExpression;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.BreakStatement;
import python.models.small_statement.ContinueStatement;
import python.models.small_statement.GlobalStatement;
import python.models.small_statement.PassStatement;
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
import java.util.Set;

/** Second semantic pass: resolves every supported identifier read and write. */
public final class PythonNameResolver {
    private final String sourceFile;
    private final SymbolTable symbolTable;
    private final Map<String, Symbol> builtins;
    private final List<CompilerProblem> diagnostics;
    private final Set<ID> identifiersToSuppress;
    private final Map<ID, Symbol> bindings = new IdentityHashMap<>();
    private final Set<ID> reportedIdentifiers =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<ASTNode> reportedUnsupported =
            Collections.newSetFromMap(new IdentityHashMap<>());

    public PythonNameResolver(
            String sourceFile,
            SymbolTable symbolTable,
            Map<String, Symbol> builtins,
            List<CompilerProblem> diagnostics,
            Set<ID> identifiersToSuppress
    ) {
        this.sourceFile = sourceFile;
        this.symbolTable = symbolTable;
        this.builtins = builtins;
        this.diagnostics = diagnostics;
        this.identifiersToSuppress = identifiersToSuppress;
    }

    public Map<ID, Symbol> resolve(Program program) {
        visitStatements(program.statements, symbolTable.getModuleScope(), 0, 0);
        return bindings;
    }

    private void visitStatements(
            List<Statement> statements, Scope scope, int functionDepth, int loopDepth) {
        for (Statement statement : statements) {
            visitStatement(statement, scope, functionDepth, loopDepth);
        }
    }

    private void visitStatement(
            Statement statement, Scope scope, int functionDepth, int loopDepth) {
        if (statement instanceof SimpleStatement simple) {
            for (SmallStatement small : simple.smallStatementList) {
                visitSmallStatement(small, scope, functionDepth, loopDepth);
            }
        } else if (statement instanceof DecoratorStatement decorated) {
            visitDecoratedFunction(decorated, scope, functionDepth);
        } else if (statement instanceof IfStatement conditional) {
            for (int index = 0; index < conditional.conditions.size(); index++) {
                visitCondition(conditional.conditions.get(index), scope);
                visitBody(conditional.bodies.get(index), scope, functionDepth, loopDepth);
            }
            if (conditional.last != null) {
                visitBody(conditional.last, scope, functionDepth, loopDepth);
            }
        } else if (statement instanceof ForStatement loop) {
            visitCondition(loop.iterable, scope);
            for (ID iterator : loop.iterators) bindWrite(iterator, scope);
            visitBody(loop.body, scope, functionDepth, loopDepth + 1);
            if (loop.last != null) visitBody(loop.last, scope, functionDepth, loopDepth);
        } else if (statement instanceof WhileStatement loop) {
            visitCondition(loop.condition, scope);
            visitBody(loop.body, scope, functionDepth, loopDepth + 1);
            if (loop.last != null) visitBody(loop.last, scope, functionDepth, loopDepth);
        } else {
            unsupported(statement, "Unsupported Python statement: " + statement.getSimpleName());
            visitGenericChildren(statement, scope);
        }
    }

    private void visitBody(Body body, Scope scope, int functionDepth, int loopDepth) {
        visitStatements(body.statements, scope, functionDepth, loopDepth);
    }

    private void visitDecoratedFunction(
            DecoratorStatement decorated, Scope enclosingScope, int functionDepth) {
        if (decorated.decorators != null) {
            for (Decorator decorator : decorated.decorators) {
                visitDecorator(decorator, enclosingScope);
            }
        }

        FunctionDef function = decorated.function;
        bindWrite(function.id, enclosingScope);
        for (Parameter parameter : function.parameters) {
            if (parameter.type != null) visitCondition(parameter.type, enclosingScope);
            if (parameter.defaultValue != null) visitCondition(parameter.defaultValue, enclosingScope);
        }
        if (function.returnType != null) visitCondition(function.returnType, enclosingScope);

        Scope functionScope = symbolTable.getFunctionScope(function);
        if (functionScope == null) {
            unsupported(function, "Function scope is unavailable for '" + function.id.name + "'");
            return;
        }
        for (Parameter parameter : function.parameters) bindWrite(parameter.id, functionScope);
        visitBody(function.body, functionScope, functionDepth + 1, 0);
    }

    private void visitDecorator(Decorator decorator, Scope scope) {
        if (decorator.dottedName == null || decorator.dottedName.isEmpty()) {
            unsupported(decorator, "Decorator has no target");
            return;
        }
        resolveRead(decorator.dottedName.getFirst(), scope);
        if (decorator.arguments != null) {
            for (Argument argument : decorator.arguments) visitArgument(argument, scope);
        }
    }

    private void visitSmallStatement(
            SmallStatement statement, Scope scope, int functionDepth, int loopDepth) {
        if (statement instanceof ExpressionStatement expression) {
            if (expression.isAssignment()) {
                for (Condition value : expression.getValues()) visitCondition(value, scope);
                for (Condition target : expression.getTargets()) visitAssignmentTarget(target, scope);
            } else {
                for (Condition value : expression.getExpressions()) visitCondition(value, scope);
            }
        } else if (statement instanceof AugAssignStatement augmented) {
            resolveRead(augmented.id, scope);
            visitCondition(augmented.expression, scope);
        } else if (statement instanceof ReturnStatement returned) {
            if (functionDepth == 0) {
                error("SCOPE", returned.getLine(), "'return' outside of a function");
            }
            for (Condition value : returned.conditions) visitCondition(value, scope);
        } else if (statement instanceof BreakStatement broken) {
            if (loopDepth == 0) error("SCOPE", broken.getLine(), "'break' outside of a loop");
        } else if (statement instanceof ContinueStatement continued) {
            if (loopDepth == 0) error("SCOPE", continued.getLine(), "'continue' outside of a loop");
        } else if (statement instanceof GlobalStatement global) {
            for (ID name : global.names) {
                Symbol symbol = symbolTable.getModuleScope().resolveLocal(name.name);
                if (symbol != null) bindings.put(name, symbol);
            }
        } else if (statement instanceof ImportStatement imported) {
            bindImport(imported, scope);
        } else if (!(statement instanceof PassStatement)) {
            unsupported(statement,
                    "Unsupported Python small statement: " + statement.getSimpleName());
            visitGenericChildren(statement, scope);
        }
    }

    private void bindImport(ImportStatement statement, Scope scope) {
        if (statement instanceof SimpleImportStatement simple
                && simple.dottedName != null && !simple.dottedName.isEmpty()) {
            bindWrite(simple.getBoundName(), scope);
        } else if (statement instanceof FromImportStatement from && from.targets != null) {
            for (ID target : from.getBoundNames()) bindWrite(target, scope);
        }
    }

    private void visitAssignmentTarget(Condition target, Scope scope) {
        if (target instanceof IDTrailer identifier) {
            if (identifier.trailers == null || identifier.trailers.isEmpty()) {
                bindWrite(identifier.id, scope);
            } else {
                visitIdentifierExpression(identifier, scope);
            }
        } else if (target instanceof ID identifier) {
            bindWrite(identifier, scope);
        } else if (target instanceof ParenAtom parenthesized) {
            visitAssignmentTarget(parenthesized.inner, scope);
        } else if (target instanceof python.models.atom_statement.List list) {
            for (var item : list.content) visitAssignmentTarget(item, scope);
        } else if (target instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) visitAssignmentTarget(item, scope);
        } else {
            visitCondition(target, scope);
        }
    }

    private void visitCondition(Condition condition, Scope scope) {
        if (condition == null) return;
        if (condition instanceof IDTrailer identifier) {
            visitIdentifierExpression(identifier, scope);
        } else if (condition instanceof ID identifier) {
            resolveRead(identifier, scope);
        } else if (condition instanceof BinaryExpression binary) {
            visitCondition(binary.left, scope);
            visitCondition(binary.right, scope);
        } else if (condition instanceof UnaryExpression unary) {
            visitCondition(unary.expression, scope);
        } else if (condition instanceof RelationalComparison comparison) {
            visitCondition(comparison.left, scope);
            visitCondition(comparison.right, scope);
        } else if (condition instanceof CompoundCondition compound) {
            visitCondition(compound.first, scope);
            visitCondition(compound.second, scope);
        } else if (condition instanceof ParenAtom parenthesized) {
            visitCondition(parenthesized.inner, scope);
        } else if (condition instanceof python.models.atom_statement.List list) {
            for (var item : list.content) visitCondition(item, scope);
        } else if (condition instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) visitCondition(item, scope);
        } else if (condition instanceof Dictionary dictionary) {
            for (var key : dictionary.keys) visitCondition(key, scope);
            for (var value : dictionary.values) visitCondition(value, scope);
        } else if (!(condition instanceof python.models.atom_statement.Atom)) {
            unsupported(condition,
                    "Unsupported Python expression: " + condition.getSimpleName());
            visitGenericChildren(condition, scope);
        }
    }

    private void visitIdentifierExpression(IDTrailer expression, Scope scope) {
        resolveRead(expression.id, scope);
        if (expression.trailers == null) return;
        for (Trailer trailer : expression.trailers) {
            if (trailer.arguments instanceof CallArguments calls) {
                for (Argument argument : calls.args) visitArgument(argument, scope);
            } else if (trailer.arguments instanceof SubscriptArguments subscripts) {
                for (Condition subscript : subscripts.conditions) visitCondition(subscript, scope);
            }
        }
    }

    private void visitArgument(Argument argument, Scope scope) {
        if (argument.isAssigned()) {
            visitCondition(argument.assign, scope);
        } else {
            visitCondition(argument.arg, scope);
        }
    }

    private void bindWrite(ID identifier, Scope scope) {
        Scope targetScope = symbolTable.assignmentScope(scope, identifier.name);
        Symbol symbol = targetScope.resolveLocal(identifier.name);
        if (symbol != null) bindings.put(identifier, symbol);
    }

    private void resolveRead(ID identifier, Scope scope) {
        if (bindings.containsKey(identifier) || reportedIdentifiers.contains(identifier)) return;
        Symbol symbol = symbolTable.resolve(scope, identifier.name);
        if (symbol == null) symbol = builtins.get(identifier.name);
        if (symbol != null) {
            bindings.put(identifier, symbol);
            symbol.addUsage(identifier.getLine());
            return;
        }

        if (identifiersToSuppress.contains(identifier)) {
            return;
        }

        String kind;
        String message;
        if (symbolTable.findDeclaredAnywhere(identifier.name) != null) {
            kind = "SCOPE";
            message = "Variable '" + identifier.name + "' is not visible in this scope";
        } else {
            kind = "UNDEFINED_VARIABLE";
            message = "Undefined variable '" + identifier.name + "'";
        }
        reportedIdentifiers.add(identifier);
        error(kind, identifier.getLine(), message);
    }

    private void visitGenericChildren(ASTNode node, Scope scope) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Condition condition) visitCondition(condition, scope);
            else if (child instanceof Statement statement) visitStatement(statement, scope, 0, 0);
        }
    }

    private void unsupported(ASTNode node, String message) {
        if (reportedUnsupported.add(node)) {
            error("UNSUPPORTED_SEMANTIC_CONSTRUCT", node.getLine(), message);
        }
    }

    private void error(String kind, int line, String message) {
        diagnostics.add(new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS, kind, sourceFile, line, message));
    }
}
