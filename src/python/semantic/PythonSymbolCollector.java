package python.semantic;

import errors.CompilerProblem;
import errors.CompilerStage;
import python.models.atom_statement.ID;
import python.models.atom_statement.ParenAtom;
import python.models.compound_statement.Body;
import python.models.compound_statement.DecoratorStatement;
import python.models.compound_statement.ForStatement;
import python.models.compound_statement.IfStatement;
import python.models.compound_statement.WhileStatement;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.root.CompoundStatement;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.GlobalStatement;
import python.models.small_statement.SmallStatement;
import python.symbol_table.Scope;
import python.symbol_table.ScopeKind;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;
import python.symbol_table.SymbolTable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** First semantic pass: pre-collects declarations in every lexical scope. */
public final class PythonSymbolCollector {
    private final String sourceFile;
    private final List<CompilerProblem> diagnostics;
    private final SymbolTable symbolTable = new SymbolTable();

    public PythonSymbolCollector(String sourceFile, List<CompilerProblem> diagnostics) {
        this.sourceFile = sourceFile;
        this.diagnostics = diagnostics;
    }

    public SymbolTable collect(Program program) {
        collectDeclarations(program.statements, symbolTable.getModuleScope());
        collectNestedFunctions(program.statements);
        return symbolTable;
    }

    private void collectNestedFunctions(List<Statement> statements) {
        for (Statement statement : statements) {
            if (statement instanceof DecoratorStatement decorated) {
                collectFunction(decorated.function);
            } else if (statement instanceof IfStatement conditional) {
                for (Body body : conditional.bodies) collectNestedFunctions(body.statements);
                if (conditional.last != null) collectNestedFunctions(conditional.last.statements);
            } else if (statement instanceof ForStatement loop) {
                collectNestedFunctions(loop.body.statements);
                if (loop.last != null) collectNestedFunctions(loop.last.statements);
            } else if (statement instanceof WhileStatement loop) {
                collectNestedFunctions(loop.body.statements);
                if (loop.last != null) collectNestedFunctions(loop.last.statements);
            }
        }
    }

    private void collectFunction(FunctionDef function) {
        Scope scope = symbolTable.enterFunctionScope(function);
        collectGlobals(function.body.statements, scope);

        Set<String> parameterNames = new LinkedHashSet<>();
        for (Parameter parameter : function.parameters) {
            String name = parameter.id.name;
            if (!parameterNames.add(name)) {
                error("SCOPE", parameter.getLine(),
                        "Duplicate parameter '" + name + "' in function '" + function.id.name + "'");
                continue;
            }
            if (scope.isGlobal(name)) {
                // The source-order validator reports this at the global
                // declaration, once for each conflicting name.
                continue;
            }
            symbolTable.declareOrGet(scope,
                    new Symbol(name, SymbolKind.PARAMETER, parameter.id.getLine()));
        }

        collectDeclarations(function.body.statements, scope);
        collectNestedFunctions(function.body.statements);
        symbolTable.exitScope();
    }

    private void collectGlobals(List<Statement> statements, Scope scope) {
        for (Statement statement : statements) {
            if (statement instanceof SimpleStatement simple) {
                for (SmallStatement small : simple.smallStatementList) {
                    if (small instanceof GlobalStatement global) {
                        for (ID name : global.names) scope.declareGlobal(name.name);
                    }
                }
            } else if (statement instanceof IfStatement conditional) {
                for (Body body : conditional.bodies) collectGlobals(body.statements, scope);
                if (conditional.last != null) collectGlobals(conditional.last.statements, scope);
            } else if (statement instanceof ForStatement loop) {
                collectGlobals(loop.body.statements, scope);
                if (loop.last != null) collectGlobals(loop.last.statements, scope);
            } else if (statement instanceof WhileStatement loop) {
                collectGlobals(loop.body.statements, scope);
                if (loop.last != null) collectGlobals(loop.last.statements, scope);
            }
        }
    }

    private void collectDeclarations(List<Statement> statements, Scope scope) {
        for (Statement statement : statements) {
            if (statement instanceof SimpleStatement simple) {
                for (SmallStatement small : simple.smallStatementList) {
                    collectSmallStatement(small, scope);
                }
            } else if (statement instanceof DecoratorStatement decorated) {
                declare(scope, decorated.function.id, SymbolKind.FUNCTION);
            } else if (statement instanceof IfStatement conditional) {
                for (Body body : conditional.bodies) collectDeclarations(body.statements, scope);
                if (conditional.last != null) collectDeclarations(conditional.last.statements, scope);
            } else if (statement instanceof ForStatement loop) {
                for (ID iterator : loop.iterators) declare(scope, iterator, SymbolKind.VARIABLE);
                collectDeclarations(loop.body.statements, scope);
                if (loop.last != null) collectDeclarations(loop.last.statements, scope);
            } else if (statement instanceof WhileStatement loop) {
                collectDeclarations(loop.body.statements, scope);
                if (loop.last != null) collectDeclarations(loop.last.statements, scope);
            } else if (statement instanceof CompoundStatement) {
                error("UNSUPPORTED_SEMANTIC_CONSTRUCT", statement.getLine(),
                        "Unsupported Python compound statement: " + statement.getSimpleName());
            }
        }
    }

    private void collectSmallStatement(SmallStatement statement, Scope scope) {
        if (statement instanceof ExpressionStatement expression && expression.isAssignment()) {
            for (Condition target : expression.getTargets()) collectTarget(target, scope);
        } else if (statement instanceof ImportStatement importStatement) {
            collectImport(importStatement, scope);
        }
    }

    private void collectImport(ImportStatement statement, Scope scope) {
        if (statement instanceof SimpleImportStatement simple && simple.dottedName != null
                && !simple.dottedName.isEmpty()) {
            declare(scope, simple.getBoundName(), SymbolKind.IMPORT);
        } else if (statement instanceof FromImportStatement from) {
            if (from.hasStar()) {
                error("UNSUPPORTED_SEMANTIC_CONSTRUCT", from.getLine(),
                        "Star imports cannot be resolved statically");
            } else if (from.targets != null) {
                for (ID target : from.getBoundNames()) declare(scope, target, SymbolKind.IMPORT);
            }
        }
    }

    private void collectTarget(Condition target, Scope scope) {
        if (target instanceof IDTrailer identifier
                && (identifier.trailers == null || identifier.trailers.isEmpty())) {
            declare(scope, identifier.id, SymbolKind.VARIABLE);
        } else if (target instanceof ID identifier) {
            declare(scope, identifier, SymbolKind.VARIABLE);
        } else if (target instanceof ParenAtom parenthesized && parenthesized.inner != null) {
            collectTarget(parenthesized.inner, scope);
        } else if (target instanceof python.models.atom_statement.List list) {
            for (var item : list.content) collectTarget(item, scope);
        } else if (target instanceof python.models.atom_statement.Set set) {
            for (var item : set.content) collectTarget(item, scope);
        }
    }

    private void declare(Scope scope, ID identifier, SymbolKind kind) {
        Scope targetScope = symbolTable.assignmentScope(scope, identifier.name);
        symbolTable.declareOrGet(targetScope,
                new Symbol(identifier.name, kind, identifier.getLine()));
    }

    private void error(String kind, int line, String message) {
        diagnostics.add(new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS, kind, sourceFile, line, message));
    }
}
