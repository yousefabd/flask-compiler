package compiler.semantic;

import jinja2.TemplateFrontend;
import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
import jinja2.models.content.ContentNode;
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
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.BlockStatementNode;
import jinja2.models.statement.ForStatementNode;
import jinja2.models.statement.IfBranchNode;
import jinja2.models.statement.IfStatementNode;
import jinja2.models.statement.MacroStatementNode;
import jinja2.models.statement.ParameterNode;
import jinja2.models.statement.SetStatementNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collects the <em>free</em> variables of a Jinja template — the names the
 * template reads but does not itself declare, i.e. exactly the names a Flask
 * route has to pass through {@code render_template(...)}.
 *
 * <p>Read-only: it walks the already-parsed Jinja AST and neither modifies it
 * nor touches rendering, expression evaluation or code generation. Names are
 * taken from AST nodes, never parsed out of error message text.</p>
 *
 * <p>Excluded from the result:</p>
 * <ul>
 *   <li>Jinja builtins ({@code url_for}, {@code request}, … — the list is
 *       {@link TemplateFrontend#TEMPLATE_BUILTINS})</li>
 *   <li>loop variables, plus the implicit {@code loop} object</li>
 *   <li>macro names and macro parameters</li>
 *   <li>variables created by {@code {% set %}}</li>
 *   <li>the subject of {@code is defined} / {@code is undefined}, which is
 *       allowed to be absent by design</li>
 * </ul>
 */
public final class TemplateFreeVariableCollector {

    private final Deque<Set<String>> scopes = new ArrayDeque<>();

    /** Free variable name -> first line it is read on. */
    private final Map<String, Integer> freeVariables = new LinkedHashMap<>();

    private TemplateFreeVariableCollector() {
        Set<String> root = new HashSet<>(TemplateFrontend.TEMPLATE_BUILTINS);
        scopes.push(root);
    }

    public static Map<String, Integer> collect(TemplateFile template) {
        TemplateFreeVariableCollector collector = new TemplateFreeVariableCollector();
        if (template != null)
            for (ContentNode child : template.getContentChildren()) collector.visitContent(child);
        return collector.freeVariables;
    }

    // ─────────────────────────────────────────────────────────────
    // SCOPES
    // ─────────────────────────────────────────────────────────────

    private void push() { scopes.push(new HashSet<>()); }

    private void pop() { scopes.pop(); }

    private void bind(String name) { scopes.peek().add(name); }

    private boolean isBound(String name) {
        for (Set<String> scope : scopes) if (scope.contains(name)) return true;
        return false;
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT
    // ─────────────────────────────────────────────────────────────

    private void visitContent(ContentNode node) {
        if (node instanceof ForStatementNode forStatement) {
            visitExpression(forStatement.getIterable());   // evaluated outside the loop
            push();
            bind("loop");
            for (IdentifierNode variable : forStatement.getVariables()) bind(variable.getName());
            for (ContentNode child : forStatement.getBody()) visitContent(child);
            pop();
        }
        else if (node instanceof IfStatementNode ifStatement) {
            // {% if %} does not create a Jinja scope, but a branch guarded by
            // `x is defined` is exactly how a template declares that x is
            // optional — so x is not required inside that branch.
            for (IfBranchNode branch : ifStatement.getBranches()) {
                if (branch.getCondition() != null) visitExpression(branch.getCondition());

                Set<String> guarded = guardedNames(branch.getCondition());
                if (!guarded.isEmpty()) {
                    push();
                    guarded.forEach(this::bind);
                }

                for (ContentNode child : branch.getBody()) visitContent(child);

                if (!guarded.isEmpty()) pop();
            }
        }
        else if (node instanceof SetStatementNode setStatement) {
            // The value is read before the new name exists.
            if (setStatement.isBlock())
                for (ContentNode child : setStatement.getBody()) visitContent(child);
            else
                visitExpression(setStatement.getValue());

            for (IdentifierNode target : setStatement.getTargets()) bind(target.getName());
        }
        else if (node instanceof MacroStatementNode macro) {
            bind(macro.getMacroName());
            for (ParameterNode parameter : macro.getParameters())
                if (parameter.hasDefault()) visitExpression(parameter.getDefaultValue());

            push();
            for (ParameterNode parameter : macro.getParameters()) bind(parameter.getName());
            for (ContentNode child : macro.getBody()) visitContent(child);
            pop();
        }
        else if (node instanceof BlockStatementNode block) {
            push();
            for (ContentNode child : block.getBody()) visitContent(child);
            pop();
        }
        else if (node instanceof OutputNode output) {
            visitExpression(output.getExpression());
        }
        else if (node instanceof HTMLNormalElementNode element) {
            visitAttributes(element.getAttributes());
            for (ContentNode child : element.getChildren()) visitContent(child);
        }
        else if (node instanceof HTMLVoidElementNode element) {
            visitAttributes(element.getAttributes());
        }
        // text, extends, include — nothing to read
    }

    private void visitAttributes(List<HtmlAttributeNode> attributes) {
        for (HtmlAttributeNode attribute : attributes)
            for (AttributeValuePartNode part : attribute.getValueParts())
                if (part instanceof AttributeExpressionNode expressionPart)
                    visitExpression(expressionPart.getExpression());
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS
    // ─────────────────────────────────────────────────────────────

    private void visitExpression(ExpressionNode expression) {
        if (expression == null) return;

        if (expression instanceof IdentifierNode identifier) {
            recordIfFree(identifier);
        }
        else if (expression instanceof BinaryExpressionNode binary) {
            visitExpression(binary.getLeft());
            visitExpression(binary.getRight());
        }
        else if (expression instanceof UnaryExpressionNode unary) {
            visitExpression(unary.getExpression());
        }
        else if (expression instanceof PropertyAccessNode property) {
            // user.name — only `user` is a context variable
            visitExpression(property.getTarget());
        }
        else if (expression instanceof IndexAccessNode index) {
            visitExpression(index.getTarget());
            visitExpression(index.getIndex());
        }
        else if (expression instanceof CallExpressionNode call) {
            visitExpression(call.getCallee());
            for (ArgumentNode argument : call.getArguments()) visitExpression(argument.getValue());
        }
        else if (expression instanceof FilterExpressionNode filter) {
            visitExpression(filter.getTarget());
            for (ArgumentNode argument : filter.getArguments()) visitExpression(argument.getValue());
        }
        else if (expression instanceof ListExpressionNode list) {
            for (ExpressionNode element : list.getElements()) visitExpression(element);
        }
        else if (expression instanceof DictionaryExpressionNode dictionary) {
            for (ExpressionNode key : dictionary.getKeys()) visitExpression(key);
            for (ExpressionNode value : dictionary.getValues()) visitExpression(value);
        }
        else if (expression instanceof TestExpressionNode test) {
            visitTest(test);
        }
        // literals — nothing to read
    }

    /**
     * {@code x is defined} deliberately asks about a name that may be absent,
     * so its subject is not a required context variable.
     */
    private void visitTest(TestExpressionNode test) {
        boolean definitionTest = test.getTestName().equals("defined")
                || test.getTestName().equals("undefined");

        if (definitionTest) visitOptional(test.getValue());
        else visitExpression(test.getValue());

        for (ArgumentNode argument : test.getArguments()) visitExpression(argument.getValue());
    }

    private void visitOptional(ExpressionNode expression) {
        if (expression instanceof IdentifierNode) return;

        if (expression instanceof PropertyAccessNode property) {
            visitOptional(property.getTarget());
            return;
        }
        if (expression instanceof IndexAccessNode index) {
            visitOptional(index.getTarget());
            visitExpression(index.getIndex());   // the index itself must exist
            return;
        }
        visitExpression(expression);
    }

    /**
     * Names a branch condition proves are present, so reading them inside that
     * branch does not require the route to supply them:
     * {@code {% if note is defined %}{{ note }}{% endif %}}.
     */
    private static Set<String> guardedNames(ExpressionNode condition) {
        Set<String> names = new HashSet<>();
        collectGuardedNames(condition, names);
        return names;
    }

    private static void collectGuardedNames(ExpressionNode condition, Set<String> names) {
        if (condition instanceof TestExpressionNode test) {
            if (test.getTestName().equals("defined")
                    && test.getValue() instanceof IdentifierNode identifier)
                names.add(identifier.getName());
            return;
        }
        // `a is defined and b is defined` guards both.
        if (condition instanceof BinaryExpressionNode binary
                && binary.getOperation() == jinja2.models.expression.Operation.AND) {
            collectGuardedNames(binary.getLeft(), names);
            collectGuardedNames(binary.getRight(), names);
        }
    }

    private void recordIfFree(IdentifierNode identifier) {
        String name = identifier.getName();
        if (isBound(name)) return;
        freeVariables.putIfAbsent(name, identifier.getLineNumber());
    }
}
