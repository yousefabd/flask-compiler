package python.semantic;

import errors.CompilerProblem;
import errors.CompilerStage;
import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
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
import python.models.small_statement.GlobalStatement;
import python.models.small_statement.ReturnStatement;
import python.models.small_statement.SmallStatement;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Validates Python's textual restriction on function-level {@code global}
 * declarations. Declaration collection still applies a global to the whole
 * function; this pass separately rejects a parameter, binding, or read which
 * appears before the declaration in source order.
 */
public final class PythonGlobalDeclarationValidator {
    private final String sourceFile;
    private final List<CompilerProblem> diagnostics;
    private final Set<ID> identifiersToSuppress =
            Collections.newSetFromMap(new IdentityHashMap<>());

    public PythonGlobalDeclarationValidator(
            String sourceFile,
            List<CompilerProblem> diagnostics
    ) {
        this.sourceFile = Objects.requireNonNull(sourceFile);
        this.diagnostics = Objects.requireNonNull(diagnostics);
    }

    public Result validate(Program program) {
        Objects.requireNonNull(program);
        findFunctions(program.statements);
        return new Result(identifiersToSuppress);
    }

    private void findFunctions(List<Statement> statements) {
        for (Statement statement : statements) {
            if (statement instanceof DecoratorStatement decorated) {
                validateFunction(decorated.function);
            } else if (statement instanceof IfStatement conditional) {
                for (Body body : conditional.bodies) {
                    findFunctions(body.statements);
                }
                if (conditional.last != null) {
                    findFunctions(conditional.last.statements);
                }
            } else if (statement instanceof ForStatement loop) {
                findFunctions(loop.body.statements);
                if (loop.last != null) {
                    findFunctions(loop.last.statements);
                }
            } else if (statement instanceof WhileStatement loop) {
                findFunctions(loop.body.statements);
                if (loop.last != null) {
                    findFunctions(loop.last.statements);
                }
            }
        }
    }

    private void validateFunction(FunctionDef function) {
        FunctionTraversal traversal = new FunctionTraversal();
        for (Parameter parameter : function.parameters) {
            traversal.recordPriorOccurrence(parameter.id);
        }
        traversal.visitStatements(function.body.statements);
    }

    public record Result(Set<ID> identifiersToSuppress) {
        public Result {
            Objects.requireNonNull(identifiersToSuppress);
            Set<ID> copy =
                    Collections.newSetFromMap(new IdentityHashMap<>());
            copy.addAll(identifiersToSuppress);
            identifiersToSuppress = Collections.unmodifiableSet(copy);
        }
    }

    private final class FunctionTraversal {
        private final Map<String, List<ID>> priorOccurrences =
                new LinkedHashMap<>();
        private final Set<String> invalidGlobalNames =
                new LinkedHashSet<>();

        private void visitStatements(List<Statement> statements) {
            for (Statement statement : statements) {
                visitStatement(statement);
            }
        }

        private void visitStatement(Statement statement) {
            if (statement instanceof SimpleStatement simple) {
                for (SmallStatement small : simple.smallStatementList) {
                    visitSmallStatement(small);
                }
            } else if (statement instanceof DecoratorStatement decorated) {
                visitNestedFunctionDefinition(decorated);
            } else if (statement instanceof IfStatement conditional) {
                for (int index = 0;
                     index < conditional.conditions.size();
                     index++) {
                    visitCondition(conditional.conditions.get(index));
                    visitStatements(conditional.bodies.get(index).statements);
                }
                if (conditional.last != null) {
                    visitStatements(conditional.last.statements);
                }
            } else if (statement instanceof ForStatement loop) {
                visitCondition(loop.iterable);
                for (ID iterator : loop.iterators) {
                    recordPriorOccurrence(iterator);
                }
                visitStatements(loop.body.statements);
                if (loop.last != null) {
                    visitStatements(loop.last.statements);
                }
            } else if (statement instanceof WhileStatement loop) {
                visitCondition(loop.condition);
                visitStatements(loop.body.statements);
                if (loop.last != null) {
                    visitStatements(loop.last.statements);
                }
            }
        }

        private void visitNestedFunctionDefinition(
                DecoratorStatement decorated
        ) {
            if (decorated.decorators != null) {
                for (Decorator decorator : decorated.decorators) {
                    visitDecorator(decorator);
                }
            }

            FunctionDef nested = decorated.function;
            for (Parameter parameter : nested.parameters) {
                visitCondition(parameter.type);
                visitCondition(parameter.defaultValue);
            }
            visitCondition(nested.returnType);
            recordPriorOccurrence(nested.id);

            // The nested body is a separate code block with independent
            // textual global rules; it must not affect the enclosing pass.
            validateFunction(nested);
        }

        private void visitDecorator(Decorator decorator) {
            if (decorator.dottedName != null
                    && !decorator.dottedName.isEmpty()) {
                recordPriorOccurrence(decorator.dottedName.getFirst());
            }
            if (decorator.arguments != null) {
                for (Argument argument : decorator.arguments) {
                    visitArgument(argument);
                }
            }
        }

        private void visitSmallStatement(SmallStatement statement) {
            if (statement instanceof ExpressionStatement expression) {
                if (expression.isAssignment()) {
                    for (Condition value : expression.getValues()) {
                        visitCondition(value);
                    }
                    for (Condition target : expression.getTargets()) {
                        visitAssignmentTarget(target);
                    }
                } else {
                    for (Condition value : expression.getExpressions()) {
                        visitCondition(value);
                    }
                }
            } else if (statement instanceof AugAssignStatement augmented) {
                recordPriorOccurrence(augmented.id);
                visitCondition(augmented.expression);
            } else if (statement instanceof ReturnStatement returned) {
                for (Condition value : returned.conditions) {
                    visitCondition(value);
                }
            } else if (statement instanceof GlobalStatement global) {
                visitGlobalStatement(global);
            } else if (statement instanceof ImportStatement imported) {
                visitImportBinding(imported);
            }
        }

        private void visitGlobalStatement(GlobalStatement global) {
            for (ID name : global.names) {
                List<ID> prior = priorOccurrences.get(name.name);
                if (prior == null || prior.isEmpty()) {
                    continue;
                }

                reportIllegalGlobal(name);
                invalidGlobalNames.add(name.name);
                identifiersToSuppress.addAll(prior);
            }
        }

        private void reportIllegalGlobal(ID name) {
            diagnostics.add(new CompilerProblem(
                    CompilerStage.SEMANTIC_ANALYSIS,
                    "SCOPE",
                    sourceFile,
                    name.getLine(),
                    "Name " + name.name
                            + " is used or assigned before its global declaration"
            ));
        }

        private void visitImportBinding(ImportStatement statement) {
            if (statement instanceof SimpleImportStatement simple) {
                ID boundName = simple.getBoundName();
                if (boundName != null) {
                    recordPriorOccurrence(boundName);
                }
            } else if (statement instanceof FromImportStatement from) {
                for (ID boundName : from.getBoundNames()) {
                    recordPriorOccurrence(boundName);
                }
            }
        }

        private void visitAssignmentTarget(Condition target) {
            if (target instanceof IDTrailer identifier) {
                if (identifier.trailers == null
                        || identifier.trailers.isEmpty()) {
                    recordPriorOccurrence(identifier.id);
                } else {
                    visitIdentifierExpression(identifier);
                }
            } else if (target instanceof ID identifier) {
                recordPriorOccurrence(identifier);
            } else if (target instanceof ParenAtom parenthesized) {
                visitAssignmentTarget(parenthesized.inner);
            } else if (target
                    instanceof python.models.atom_statement.List list) {
                for (Condition item : list.content) {
                    visitAssignmentTarget(item);
                }
            } else if (target
                    instanceof python.models.atom_statement.Set set) {
                for (Condition item : set.content) {
                    visitAssignmentTarget(item);
                }
            } else {
                visitCondition(target);
            }
        }

        private void visitCondition(Condition condition) {
            if (condition == null) {
                return;
            }

            if (condition instanceof IDTrailer identifier) {
                visitIdentifierExpression(identifier);
            } else if (condition instanceof ID identifier) {
                recordPriorOccurrence(identifier);
            } else if (condition instanceof BinaryExpression binary) {
                visitCondition(binary.left);
                visitCondition(binary.right);
            } else if (condition instanceof UnaryExpression unary) {
                visitCondition(unary.expression);
            } else if (condition
                    instanceof RelationalComparison comparison) {
                visitCondition(comparison.left);
                visitCondition(comparison.right);
            } else if (condition instanceof CompoundCondition compound) {
                visitCondition(compound.first);
                visitCondition(compound.second);
            } else if (condition instanceof ParenAtom parenthesized) {
                visitCondition(parenthesized.inner);
            } else if (condition
                    instanceof python.models.atom_statement.List list) {
                for (Condition item : list.content) {
                    visitCondition(item);
                }
            } else if (condition
                    instanceof python.models.atom_statement.Set set) {
                for (Condition item : set.content) {
                    visitCondition(item);
                }
            } else if (condition instanceof Dictionary dictionary) {
                for (Condition key : dictionary.keys) {
                    visitCondition(key);
                }
                for (Condition value : dictionary.values) {
                    visitCondition(value);
                }
            } else if (!(condition
                    instanceof python.models.atom_statement.Atom)) {
                visitExpressionChildren(condition);
            }
        }

        private void visitIdentifierExpression(IDTrailer expression) {
            recordPriorOccurrence(expression.id);
            if (expression.trailers == null) {
                return;
            }
            for (Trailer trailer : expression.trailers) {
                if (trailer.arguments instanceof CallArguments calls) {
                    for (Argument argument : calls.args) {
                        visitArgument(argument);
                    }
                } else if (trailer.arguments
                        instanceof SubscriptArguments subscripts) {
                    for (Condition subscript : subscripts.conditions) {
                        visitCondition(subscript);
                    }
                }
            }
        }

        private void visitArgument(Argument argument) {
            if (argument.isAssigned()) {
                visitCondition(argument.assign);
            } else {
                visitCondition(argument.arg);
            }
        }

        private void visitExpressionChildren(ASTNode node) {
            for (ASTNode child : node.getChildren()) {
                if (child instanceof Condition condition) {
                    visitCondition(condition);
                }
            }
        }

        private void recordPriorOccurrence(ID identifier) {
            priorOccurrences
                    .computeIfAbsent(
                            identifier.name,
                            ignored -> new ArrayList<>()
                    )
                    .add(identifier);

            if (invalidGlobalNames.contains(identifier.name)) {
                identifiersToSuppress.add(identifier);
            }
        }
    }
}
