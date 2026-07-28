package jinja2.renderer;

import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.AttributeExpressionNode;
import jinja2.models.attribute.valuepart.AttributeTextNode;
import jinja2.models.attribute.valuepart.AttributeValuePartNode;
import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
import jinja2.models.content.html.HTMLNormalElementNode;
import jinja2.models.content.html.HTMLVoidElementNode;
import jinja2.models.expression.IdentifierNode;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.ForStatementNode;
import jinja2.models.statement.IfBranchNode;
import jinja2.models.statement.IfStatementNode;
import jinja2.models.statement.SetStatementNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TemplateRenderer {

    private final ExpressionEvaluator expressionEvaluator;

    public TemplateRenderer(
            ExpressionEvaluator expressionEvaluator
    ) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public String render(
            TemplateFile template,
            RenderContext context
    ) {
        StringBuilder output = new StringBuilder();

        renderContents(
                template.getContentChildren(),
                context,
                output
        );

        return output.toString();
    }

    private void renderContents(
            List<ContentNode> nodes,
            RenderContext context,
            StringBuilder output
    ) {
        for (ContentNode node : nodes) {
            renderContent(node, context, output);
        }
    }

    private void renderContent(
            ContentNode node,
            RenderContext context,
            StringBuilder output
    ) {
        if (node instanceof HtmlTextNode textNode) {
            output.append(textNode.getText());
            return;
        }

        if (node instanceof OutputNode outputNode) {
            Object value = expressionEvaluator.evaluate(
                    outputNode.getExpression(),
                    context
            );

            appendValue(value, output);
            return;
        }

        /*
         * Must be checked before HTMLVoidElementNode because
         * HTMLNormalElementNode extends HTMLVoidElementNode.
         */
        if (node instanceof HTMLNormalElementNode normalElement) {
            renderNormalElement(
                    normalElement,
                    context,
                    output
            );

            return;
        }

        if (node instanceof HTMLVoidElementNode voidElement) {
            renderVoidElement(
                    voidElement,
                    context,
                    output
            );

            return;
        }
        if (node instanceof ForStatementNode forStatement) {
            renderForStatement(
                    forStatement,
                    context,
                    output
            );

            return;
        }
        if (node instanceof IfStatementNode ifStatement) {
            renderIfStatement(
                    ifStatement,
                    context,
                    output
            );

            return;
        }
        if (node instanceof SetStatementNode setStatement) {
            renderSetStatement(
                    setStatement,
                    context
            );

            return;
        }
        throw new UnsupportedOperationException(
                "Template node is not supported yet: "
                        + node.getClass().getSimpleName()
                        + " at line "
                        + node.getLineNumber()
        );
    }

    private void renderNormalElement(
            HTMLNormalElementNode element,
            RenderContext context,
            StringBuilder output
    ) {
        output
                .append('<')
                .append(element.getTagName());

        renderAttributes(
                element.getAttributes(),
                context,
                output
        );

        output.append('>');

        renderContents(
                element.getChildren(),
                context,
                output
        );

        output
                .append("</")
                .append(element.getTagName())
                .append('>');
    }

    private void renderVoidElement(
            HTMLVoidElementNode element,
            RenderContext context,
            StringBuilder output
    ) {
        output
                .append('<')
                .append(element.getTagName());

        renderAttributes(
                element.getAttributes(),
                context,
                output
        );

        output.append("/>");
    }

    private void renderAttributes(
            List<HtmlAttributeNode> attributes,
            RenderContext context,
            StringBuilder output
    ) {
        for (HtmlAttributeNode attribute : attributes) {
            output
                    .append(' ')
                    .append(attribute.getName());

            /*
             * Empty valueParts represents an attribute without
             * content, such as disabled.
             */
            if (attribute.getValueParts().isEmpty()) {
                continue;
            }

            output.append("=\"");

            for (AttributeValuePartNode part :
                    attribute.getValueParts()) {

                renderAttributeValuePart(
                        part,
                        context,
                        output
                );
            }

            output.append('"');
        }
    }

    private void renderAttributeValuePart(
            AttributeValuePartNode part,
            RenderContext context,
            StringBuilder output
    ) {
        if (part instanceof AttributeTextNode textPart) {
            output.append(textPart.getText());
            return;
        }

        if (part instanceof AttributeExpressionNode expressionPart) {
            Object value = expressionEvaluator.evaluate(
                    expressionPart.getExpression(),
                    context
            );

            appendValue(value, output);
            return;
        }

        throw new UnsupportedOperationException(
                "Attribute value part is not supported yet: "
                        + part.getClass().getSimpleName()
                        + " at line "
                        + part.getLineNumber()
        );
    }
    //region for statement
    private void renderForStatement(
            ForStatementNode forStatement,
            RenderContext context,
            StringBuilder output
    ) {
        Object iterableValue =
                expressionEvaluator.evaluate(
                        forStatement.getIterable(),
                        context
                );

        List<Object> items = materializeIterable(
                iterableValue,
                forStatement.getLineNumber()
        );

        for (int index = 0; index < items.size(); index++) {
            Object item = items.get(index);

            RenderContext iterationContext = context.child();

            bindLoopVariables(
                    forStatement,
                    item,
                    iterationContext
            );

            iterationContext.setLocal(
                    "loop",
                    createLoopMetadata(index, items.size())
            );

            renderContents(
                    forStatement.getBody(),
                    iterationContext,
                    output
            );
        }
    }
    private List<Object> materializeIterable(
            Object value,
            int lineNumber
    ) {
        if (value == null) {
            throw new IllegalStateException(
                    "Cannot iterate over none at line " + lineNumber
            );
        }

        List<Object> items = new ArrayList<>();

        // Jinja/Python dictionary iteration produces keys.
        if (value instanceof Map<?, ?> map) {
            items.addAll(map.keySet());
            return items;
        }

        if (value instanceof CharSequence text) {
            for (int index = 0; index < text.length(); index++) {
                items.add(String.valueOf(text.charAt(index)));
            }

            return items;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);

            for (int index = 0; index < length; index++) {
                items.add(Array.get(value, index));
            }

            return items;
        }

        if (value instanceof Iterable<?> iterable) {
            for (Object item : iterable) {
                items.add(item);
            }

            return items;
        }

        throw new IllegalStateException(
                "For-loop value must be iterable, but received "
                        + value.getClass().getSimpleName()
                        + " at line "
                        + lineNumber
        );
    }
    private void bindLoopVariables(
            ForStatementNode statement,
            Object item,
            RenderContext context
    ) {
        List<IdentifierNode> variables =
                statement.getVariables();

        if (variables.size() == 1) {
            context.setLocal(
                    variables.getFirst().getName(),
                    item
            );

            return;
        }

        List<Object> unpackedValues =
                unpackLoopItem(
                        item,
                        statement.getLineNumber()
                );

        if (unpackedValues.size() != variables.size()) {
            throw new IllegalStateException(
                    "Cannot unpack "
                            + unpackedValues.size()
                            + " values into "
                            + variables.size()
                            + " loop variables at line "
                            + statement.getLineNumber()
            );
        }

        for (int index = 0; index < variables.size(); index++) {
            context.setLocal(
                    variables.get(index).getName(),
                    unpackedValues.get(index)
            );
        }
    }
    private List<Object> unpackLoopItem(
            Object item,
            int lineNumber
    ) {
        if (item == null) {
            throw new IllegalStateException(
                    "Cannot unpack none at line " + lineNumber
            );
        }

        List<Object> values = new ArrayList<>();

        // Supports future dict.items()-style iteration.
        switch (item) {
            case Map.Entry<?, ?> entry -> {
                values.add(entry.getKey());
                values.add(entry.getValue());
                return values;
            }
            case Map<?, ?> map -> {
                values.addAll(map.keySet());
                return values;
            }
            case CharSequence text -> {
                for (int index = 0; index < text.length(); index++) {
                    values.add(String.valueOf(text.charAt(index)));
                }

                return values;
            }
            default -> {
            }
        }

        if (item.getClass().isArray()) {
            int length = Array.getLength(item);

            for (int index = 0; index < length; index++) {
                values.add(Array.get(item, index));
            }

            return values;
        }

        if (item instanceof Iterable<?> iterable) {
            for (Object value : iterable) {
                values.add(value);
            }

            return values;
        }

        throw new IllegalStateException(
                "Cannot unpack value of type "
                        + item.getClass().getSimpleName()
                        + " at line "
                        + lineNumber
        );
    }
    private Map<String, Object> createLoopMetadata(
            int index,
            int length
    ) {
        Map<String, Object> loop = new LinkedHashMap<>();

        loop.put("index", index + 1);
        loop.put("index0", index);

        loop.put("revindex", length - index);
        loop.put("revindex0", length - index - 1);

        loop.put("first", index == 0);
        loop.put("last", index == length - 1);
        loop.put("length", length);

        return loop;
    }
    //endregion
    private void renderIfStatement(
            IfStatementNode ifStatement,
            RenderContext context,
            StringBuilder output
    ) {
        for (IfBranchNode branch :
                ifStatement.getBranches()) {

            boolean shouldRender =
                    branch.isElseBranch()
                            || expressionEvaluator.evaluateCondition(
                            branch.getCondition(),
                            context
                    );

            if (!shouldRender) {
                continue;
            }

            RenderContext branchContext =
                    context.child();

            renderContents(
                    branch.getBody(),
                    branchContext,
                    output
            );

            /*
             * Only the first matching branch is rendered.
             */
            return;
        }
    }
    private void appendValue(
            Object value,
            StringBuilder output
    ) {
        if (value != null) {
            output.append(value);
        }
    }
}