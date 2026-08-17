package jinja2.semantic;

import jinja2.dependency.TemplateDependencyFinder;
import jinja2.models.TemplateNode;
import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
import jinja2.models.content.html.HTMLNormalElementNode;
import jinja2.models.content.html.HTMLVoidElementNode;
import jinja2.models.expression.ArgumentNode;
import jinja2.models.expression.BinaryExpressionNode;
import jinja2.models.expression.CallExpressionNode;
import jinja2.models.expression.DictionaryExpressionNode;
import jinja2.models.expression.ExpressionNode;
import jinja2.models.expression.FilterExpressionNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.expression.IndexAccessNode;
import jinja2.models.expression.ListExpressionNode;
import jinja2.models.expression.PropertyAccessNode;
import jinja2.models.expression.TestExpressionNode;
import jinja2.models.expression.UnaryExpressionNode;
import jinja2.models.expression.literal.LiteralExpressionNode;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.BlockStatementNode;
import jinja2.models.statement.ExtendsStatementNode;
import jinja2.models.statement.ForStatementNode;
import jinja2.models.statement.IfBranchNode;
import jinja2.models.statement.IfStatementNode;
import jinja2.models.statement.IncludeStatementNode;
import jinja2.models.statement.MacroStatementNode;
import jinja2.models.statement.ParameterNode;
import jinja2.models.statement.SetStatementNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Sequentially classifies every name occurrence in the supported Jinja AST.
 *
 * <p>This pass deliberately does not use Flask render contexts. A name which
 * is neither a template local nor a Jinja builtin is an external requirement
 * for the later cross-language validation pass.</p>
 */
public final class JinjaFreeVariableCollector {

    private final Set<String> builtins;

    public JinjaFreeVariableCollector() {
        this(JinjaBuiltinCatalog.names());
    }

    public JinjaFreeVariableCollector(
            Collection<String> builtins
    ) {
        Objects.requireNonNull(builtins);
        this.builtins = Set.copyOf(
                new LinkedHashSet<>(builtins)
        );
    }

    public JinjaFreeVariableResult collect(
            TemplateFile template
    ) {
        Objects.requireNonNull(template);
        return new Traversal(builtins).collect(template);
    }

    private static final class Traversal {

        private final Set<String> builtins;
        private final Deque<Map<String, Integer>> scopes =
                new ArrayDeque<>();
        private final List<JinjaNameUse> nameUses =
                new ArrayList<>();
        private final Map<String, Integer> externalVariables =
                new LinkedHashMap<>();
        private final Map<String, Integer> localDeclarations =
                new LinkedHashMap<>();
        private final Deque<Set<String>> definitionGuardScopes =
                new ArrayDeque<>();
        private final List<JinjaIncludeSite> includeSites =
                new ArrayList<>();

        private Traversal(Set<String> builtins) {
            this.builtins = builtins;
        }

        private JinjaFreeVariableResult collect(
                TemplateFile template
        ) {
            enterScope();

            for (ContentNode child : template.getContentChildren()) {
                visitContent(child);
            }

            exitScope();

            return new JinjaFreeVariableResult(
                    nameUses,
                    externalVariables,
                    localDeclarations,
                    includeSites
            );
        }

        private void visitContent(ContentNode node) {
            if (node instanceof ForStatementNode statement) {
                visitForStatement(statement);
                return;
            }

            if (node instanceof IfStatementNode statement) {
                visitIfStatement(statement);
                return;
            }

            if (node instanceof SetStatementNode statement) {
                visitSetStatement(statement);
                return;
            }

            if (node instanceof MacroStatementNode statement) {
                visitMacroStatement(statement);
                return;
            }

            if (node instanceof BlockStatementNode statement) {
                enterScope();
                visitContents(statement.getBody());
                exitScope();
                return;
            }

            if (node instanceof IncludeStatementNode statement) {
                visitExpression(statement.getTemplateExpression());

                TemplateDependencyFinder
                        .findStaticIncludeName(statement)
                        .ifPresent(templateName ->
                                includeSites.add(
                                        new JinjaIncludeSite(
                                                templateName,
                                                statement.getLineNumber(),
                                                visibleLocalNames()
                                        )
                                )
                        );

                return;
            }

            if (node instanceof OutputNode output) {
                visitExpression(output.getExpression());
                return;
            }

            if (node instanceof HTMLNormalElementNode element) {
                visitAttributes(element.getAttributes());
                visitContents(element.getChildren());
                return;
            }

            if (node instanceof HTMLVoidElementNode element) {
                visitAttributes(element.getAttributes());
                return;
            }

            if (node instanceof HtmlTextNode
                    || node instanceof ExtendsStatementNode) {
                return;
            }

            visitChildren(node);
        }
        private Set<String> visibleLocalNames() {
            Set<String> names =
                    new LinkedHashSet<>();

            for (Map<String, Integer> scope : scopes) {
                names.addAll(scope.keySet());
            }

            return names;
        }

        private void visitForStatement(
                ForStatementNode statement
        ) {
            visitExpression(statement.getIterable());

            enterScope();
            declareLocal("loop", statement.getLineNumber());

            for (IdentifierNode variable : statement.getVariables()) {
                declareLocal(
                        variable.getName(),
                        variable.getLineNumber()
                );
            }

            visitContents(statement.getBody());
            exitScope();
        }

        private void visitIfStatement(
                IfStatementNode statement
        ) {
            for (IfBranchNode branch :
                    statement.getBranches()) {

                ExpressionNode condition =
                        branch.getCondition();

                if (condition != null) {
                    visitExpression(condition);
                }

                Set<String> guardedNames =
                        condition == null
                                ? Set.of()
                                : JinjaConditionFacts
                                .definedWhenTrue(condition);

                definitionGuardScopes.addLast(
                        guardedNames
                );

                try {
                    visitContents(branch.getBody());
                } finally {
                    definitionGuardScopes.removeLast();
                }
            }
        }
        private void visitSetStatement(
                SetStatementNode statement
        ) {
            if (statement.isBlock()) {
                visitContents(statement.getBody());
            } else {
                visitExpression(statement.getValue());
            }

            for (IdentifierNode target : statement.getTargets()) {
                declareLocal(
                        target.getName(),
                        target.getLineNumber()
                );
            }
        }

        private void visitMacroStatement(
                MacroStatementNode statement
        ) {
            declareLocal(
                    statement.getMacroName(),
                    statement.getLineNumber()
            );

            for (ParameterNode parameter : statement.getParameters()) {
                if (parameter.hasDefault()) {
                    visitExpression(parameter.getDefaultValue());
                }
            }

            enterScope();

            for (ParameterNode parameter : statement.getParameters()) {
                declareLocal(
                        parameter.getName(),
                        parameter.getLineNumber()
                );
            }

            visitContents(statement.getBody());
            exitScope();
        }

        private void visitAttributes(
                List<HtmlAttributeNode> attributes
        ) {
            for (HtmlAttributeNode attribute : attributes) {
                for (AttributeValuePartNode part
                        : attribute.getValueParts()) {
                    if (part instanceof AttributeExpressionNode expression) {
                        visitExpression(expression.getExpression());
                    }
                }
            }
        }

        private void visitExpression(ExpressionNode expression) {
            if (expression instanceof IdentifierNode identifier) {
                visitIdentifier(identifier, false);
                return;
            }

            if (expression instanceof BinaryExpressionNode binary) {
                visitExpression(binary.getLeft());
                visitExpression(binary.getRight());
                return;
            }

            if (expression instanceof UnaryExpressionNode unary) {
                visitExpression(unary.getExpression());
                return;
            }

            if (expression instanceof PropertyAccessNode property) {
                visitExpression(property.getTarget());
                recordProperty(property.getProperty());
                return;
            }

            if (expression instanceof IndexAccessNode index) {
                visitExpression(index.getTarget());
                visitExpression(index.getIndex());
                return;
            }

            if (expression instanceof CallExpressionNode call) {
                visitExpression(call.getCallee());
                visitArguments(call.getArguments());
                return;
            }

            if (expression instanceof FilterExpressionNode filter) {
                visitExpression(filter.getTarget());
                visitArguments(filter.getArguments());
                return;
            }

            if (expression instanceof ListExpressionNode list) {
                for (ExpressionNode element : list.getElements()) {
                    visitExpression(element);
                }
                return;
            }

            if (expression instanceof DictionaryExpressionNode dictionary) {
                for (int index = 0;
                     index < dictionary.getKeys().size();
                     index++) {
                    visitExpression(dictionary.getKeys().get(index));
                    visitExpression(dictionary.getValues().get(index));
                }
                return;
            }

            if (expression instanceof TestExpressionNode test) {
                visitTestExpression(test);
                return;
            }

            if (expression instanceof LiteralExpressionNode) {
                return;
            }

            visitChildren(expression);
        }

        private void visitArguments(List<ArgumentNode> arguments) {
            for (ArgumentNode argument : arguments) {
                visitExpression(argument.getValue());
            }
        }

        private void visitTestExpression(
                TestExpressionNode test
        ) {
            if (isDefinitionTest(test)) {
                visitPotentiallyUndefined(test.getValue());
            } else {
                visitExpression(test.getValue());
            }

            visitArguments(test.getArguments());
        }

        private boolean isDefinitionTest(
                TestExpressionNode test
        ) {
            return "defined".equals(test.getTestName())
                    || "undefined".equals(test.getTestName());
        }

        private void visitPotentiallyUndefined(
                ExpressionNode expression
        ) {
            if (expression instanceof IdentifierNode identifier) {
                visitIdentifier(identifier, true);
                return;
            }

            if (expression instanceof PropertyAccessNode property) {
                visitPotentiallyUndefined(property.getTarget());
                recordProperty(property.getProperty());
                return;
            }

            if (expression instanceof IndexAccessNode index) {
                visitPotentiallyUndefined(index.getTarget());
                visitExpression(index.getIndex());
                return;
            }

            visitExpression(expression);
        }

        private void visitIdentifier(
                IdentifierNode identifier,
                boolean definitionGuard
        ) {
            JinjaNameUse.Kind kind;

            if (isLocal(identifier.getName())) {
                kind = JinjaNameUse.Kind.TEMPLATE_LOCAL;
            } else if (builtins.contains(identifier.getName())) {
                kind = JinjaNameUse.Kind.BUILTIN;
            } else if (definitionGuard
                    || isGuardedAsDefined(identifier.getName())) {
                kind = JinjaNameUse.Kind.DEFINITION_GUARD;
            } else {
                kind = JinjaNameUse.Kind.EXTERNAL;
                externalVariables.putIfAbsent(
                        identifier.getName(),
                        identifier.getLineNumber()
                );
            }

            nameUses.add(
                    new JinjaNameUse(
                            identifier.getName(),
                            identifier.getLineNumber(),
                            kind
                    )
            );
        }


        private void recordProperty(IdentifierNode property) {
            nameUses.add(
                    new JinjaNameUse(
                            property.getName(),
                            property.getLineNumber(),
                            JinjaNameUse.Kind.PROPERTY_NAME
                    )
            );
        }

        private void declareLocal(String name, int line) {
            scopes.getLast().put(name, line);
            localDeclarations.putIfAbsent(name, line);
        }

        private boolean isLocal(String name) {
            Iterator<Map<String, Integer>> iterator =
                    scopes.descendingIterator();

            while (iterator.hasNext()) {
                if (iterator.next().containsKey(name)) {
                    return true;
                }
            }

            return false;
        }

        private void visitContents(List<ContentNode> contents) {
            for (ContentNode content : contents) {
                visitContent(content);
            }
        }

        private void visitChildren(TemplateNode node) {
            for (TemplateNode child : node.getChildren()) {
                if (child instanceof ContentNode content) {
                    visitContent(content);
                } else if (child instanceof ExpressionNode expression) {
                    visitExpression(expression);
                } else {
                    visitChildren(child);
                }
            }
        }

        private void enterScope() {
            scopes.addLast(new LinkedHashMap<>());
        }

        private void exitScope() {
            if (scopes.isEmpty()) {
                throw new IllegalStateException(
                        "Cannot exit a Jinja scope when none is active"
                );
            }

            scopes.removeLast();
        }
        private boolean isGuardedAsDefined(
                String name
        ) {
            for (Set<String> guardedNames :
                    definitionGuardScopes) {

                if (guardedNames.contains(name)) {
                    return true;
                }
            }

            return false;
        }
    }

}
