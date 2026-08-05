package jinja2.renderer;

import jinja2.functions.JinjaCallArguments;
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
import jinja2.models.statement.*;

import java.lang.reflect.Array;
import java.util.*;

public final class TemplateRenderer {

    private final ExpressionEvaluator expressionEvaluator;
    private static final class RenderState {

        private final Map<String, TemplateFile> templates;
        private final Deque<String> activeTemplates;

        private RenderState(
                Map<String, TemplateFile> templates
        ) {
            this.templates =
                    Map.copyOf(
                            Objects.requireNonNull(templates)
                    );

            this.activeTemplates =
                    new ArrayDeque<>();
        }
    }
    public TemplateRenderer(
            ExpressionEvaluator expressionEvaluator
    ) {
        this.expressionEvaluator = expressionEvaluator;
    }

    public String render(
            String templateName,
            Map<String, TemplateFile> templates,
            RenderContext context
    ) {
        Objects.requireNonNull(templateName);
        Objects.requireNonNull(context);

        RenderState state =
                new RenderState(templates);

        StringBuilder output =
                new StringBuilder();

        renderTemplate(
                templateName,
                context,
                output,
                state
        );

        return output.toString();
    }
    private void renderTemplate(
            String templateName,
            RenderContext context,
            StringBuilder output,
            RenderState state
    ) {
        TemplateFile template =
                state.templates.get(templateName);

        if (template == null) {
            throw new IllegalStateException(
                    "Template '"
                            + templateName
                            + "' was not parsed"
            );
        }

        if (state.activeTemplates.contains(templateName)) {
            throw new IllegalStateException(
                    "Circular template include detected: "
                            + String.join(
                            " -> ",
                            state.activeTemplates
                    )
                            + " -> "
                            + templateName
            );
        }

        state.activeTemplates.addLast(templateName);

        try {
            renderContents(
                    template.getContentChildren(),
                    context,
                    output,
                    state
            );
        } finally {
            state.activeTemplates.removeLast();
        }
    }
    private void renderContents(
            List<ContentNode> nodes,
            RenderContext context,
            StringBuilder output,
            RenderState state
    ) {
        for (ContentNode node : nodes) {
            renderContent(
                    node,
                    context,
                    output,
                    state
            );
        }
    }

    private void renderContent(
            ContentNode node,
            RenderContext context,
            StringBuilder output,
            RenderState state
    ){
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
                    output,
                    state
            );

            return;
        }

        if (node instanceof HTMLVoidElementNode voidElement) {
            renderVoidElement(
                    voidElement,
                    context,
                    output,
                    state
            );

            return;
        }
        if (node instanceof ForStatementNode forStatement) {
            renderForStatement(
                    forStatement,
                    context,
                    output,
                    state
            );

            return;
        }
        if (node instanceof IfStatementNode ifStatement) {
            renderIfStatement(
                    ifStatement,
                    context,
                    output,
                    state
            );

            return;
        }
        if (node instanceof SetStatementNode setStatement) {
            renderSetStatement(
                    setStatement,
                    context,
                    state
            );

            return;
        }
        if (node instanceof MacroStatementNode macroStatement) {
            registerMacro(
                    macroStatement,
                    context,
                    state
            );

            return;
        }
        if (node instanceof IncludeStatementNode includeStatement) {
            renderIncludeStatement(
                    includeStatement,
                    context,
                    output,
                    state
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
            StringBuilder output,
            RenderState state
    ) {
        output
                .append('<')
                .append(element.getTagName());

        renderAttributes(
                element.getAttributes(),
                context,
                output,
                state
        );

        output.append('>');

        renderContents(
                element.getChildren(),
                context,
                output,
                state
        );

        output
                .append("</")
                .append(element.getTagName())
                .append('>');
    }

    private void renderVoidElement(
            HTMLVoidElementNode element,
            RenderContext context,
            StringBuilder output,
            RenderState state
    ) {
        output
                .append('<')
                .append(element.getTagName());

        renderAttributes(
                element.getAttributes(),
                context,
                output,
                state
        );

        output.append("/>");
    }

    private void renderAttributes(
            List<HtmlAttributeNode> attributes,
            RenderContext context,
            StringBuilder output,
            RenderState state
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
                        output,
                        state
                );
            }

            output.append('"');
        }
    }

    private void renderAttributeValuePart(
            AttributeValuePartNode part,
            RenderContext context,
            StringBuilder output,
            RenderState state
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
            StringBuilder output,
            RenderState state
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
                    output,
                    state
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
        bindTargets(
                statement.getVariables(),
                item,
                context,
                statement.getLineNumber()
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
            StringBuilder output,
            RenderState state
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


            renderContents(
                    branch.getBody(),
                    context,
                    output,
                    state
            );

            /*
             * Only the first matching branch is rendered.
             */
            return;
        }
    }
    //region set statement
    private void renderSetStatement(
            SetStatementNode statement,
            RenderContext context,
            RenderState state
    ) {
        if (statement.isBlock()) {
            renderBlockSet(
                    statement,
                    context,
                    state
            );

            return;
        }

        Object value =
                expressionEvaluator.evaluate(
                        statement.getValue(),
                        context
                );

        bindTargets(
                statement.getTargets(),
                value,
                context,
                statement.getLineNumber()
        );
    }
    private void renderBlockSet(
            SetStatementNode statement,
            RenderContext context,
            RenderState state
    ) {
        if (statement.getTargets().size() != 1) {
            throw new IllegalStateException(
                    "Block set requires exactly one target at line "
                            + statement.getLineNumber()
            );
        }

        StringBuilder capturedOutput =
                new StringBuilder();

        renderContents(
                statement.getBody(),
                context,
                capturedOutput,
                state
        );

        context.setLocal(
                statement.getTargets().getFirst().getName(),
                capturedOutput.toString()
        );
    }
    private void bindTargets(
            List<IdentifierNode> targets,
            Object value,
            RenderContext context,
            int lineNumber
    ) {
        if (targets.size() == 1) {
            context.setLocal(
                    targets.getFirst().getName(),
                    value
            );

            return;
        }

        List<Object> unpackedValues =
                unpackValue(
                        value,
                        lineNumber
                );

        if (unpackedValues.size() != targets.size()) {
            throw new IllegalStateException(
                    "Cannot unpack "
                            + unpackedValues.size()
                            + " values into "
                            + targets.size()
                            + " targets at line "
                            + lineNumber
            );
        }

        for (int index = 0; index < targets.size(); index++) {
            context.setLocal(
                    targets.get(index).getName(),
                    unpackedValues.get(index)
            );
        }
    }
    //endregion

    //region macro statement
    private void registerMacro(
            MacroStatementNode statement,
            RenderContext definitionContext,
            RenderState state
    ) {
        TemplateCallable macro = arguments ->
                invokeMacro(
                        statement,
                        arguments,
                        definitionContext,
                        state
                );

        definitionContext.setLocal(
                statement.getMacroName(),
                macro
        );
    }

    private String invokeMacro(
            MacroStatementNode statement,
            JinjaCallArguments arguments,
            RenderContext definitionContext,
            RenderState state
    ) {
        RenderContext macroContext =
                definitionContext.child();

        bindMacroArguments(
                statement,
                arguments,
                definitionContext,
                macroContext
        );

        StringBuilder macroOutput =
                new StringBuilder();

        renderContents(
                statement.getBody(),
                macroContext,
                macroOutput,
                state
        );

        return macroOutput.toString();
    }

    private void bindMacroArguments(
            MacroStatementNode statement,
            JinjaCallArguments arguments,
            RenderContext definitionContext,
            RenderContext macroContext
    ) {
        List<ParameterNode> parameters =
                statement.getParameters();

        if (arguments.positional().size() > parameters.size()) {
            throw new IllegalStateException(
                    "Macro '"
                            + statement.getMacroName()
                            + "' received too many positional arguments at line "
                            + statement.getLineNumber()
            );
        }

        Map<String, ParameterNode> parametersByName =
                new LinkedHashMap<>();

        for (ParameterNode parameter : parameters) {
            parametersByName.put(
                    parameter.getName(),
                    parameter
            );
        }

        Set<String> assignedParameters =
                new HashSet<>();

        for (int index = 0;
             index < arguments.positional().size();
             index++) {

            ParameterNode parameter =
                    parameters.get(index);

            macroContext.setLocal(
                    parameter.getName(),
                    arguments.positional().get(index)
            );

            assignedParameters.add(
                    parameter.getName()
            );
        }

        for (Map.Entry<String, Object> keywordArgument
                : arguments.keyword().entrySet()) {

            String parameterName =
                    keywordArgument.getKey();

            ParameterNode parameter =
                    parametersByName.get(parameterName);

            if (parameter == null) {
                throw new IllegalStateException(
                        "Macro '"
                                + statement.getMacroName()
                                + "' received an unknown keyword argument '"
                                + parameterName
                                + "' at line "
                                + statement.getLineNumber()
                );
            }

            if (!assignedParameters.add(parameterName)) {
                throw new IllegalStateException(
                        "Macro '"
                                + statement.getMacroName()
                                + "' received multiple values for argument '"
                                + parameterName
                                + "' at line "
                                + statement.getLineNumber()
                );
            }

            macroContext.setLocal(
                    parameter.getName(),
                    keywordArgument.getValue()
            );
        }

        for (ParameterNode parameter : parameters) {
            if (assignedParameters.contains(
                    parameter.getName()
            )) {
                continue;
            }

            if (!parameter.hasDefault()) {
                throw new IllegalStateException(
                        "Macro '"
                                + statement.getMacroName()
                                + "' is missing required argument '"
                                + parameter.getName()
                                + "' at line "
                                + statement.getLineNumber()
                );
            }

            Object defaultValue =
                    expressionEvaluator.evaluate(
                            parameter.getDefaultValue(),
                            definitionContext
                    );

            macroContext.setLocal(
                    parameter.getName(),
                    defaultValue
            );
        }
    }
    //endregion

    private void appendValue(
            Object value,
            StringBuilder output
    ) {
        if (value != null) {
            output.append(value);
        }
    }
    private List<Object> unpackValue(
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
    private void renderIncludeStatement(
            IncludeStatementNode statement,
            RenderContext context,
            StringBuilder output,
            RenderState state
    ) {
        Object templateNameValue =
                expressionEvaluator.evaluate(
                        statement.getTemplateExpression(),
                        context
                );

        if (!(templateNameValue instanceof String templateName)
                || templateName.isBlank()) {

            throw new IllegalStateException(
                    "Include expression must resolve to a "
                            + "non-empty template name at line "
                            + statement.getLineNumber()
            );
        }

        if (!state.templates.containsKey(templateName)) {
            throw new IllegalStateException(
                    "Included template '"
                            + templateName
                            + "' was not parsed at line "
                            + statement.getLineNumber()
            );
        }

        renderTemplate(
                templateName,
                context.child(),
                output,
                state
        );
    }
}
