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
import jinja2.models.file.TemplateFile;

import java.util.List;

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

    private void appendValue(
            Object value,
            StringBuilder output
    ) {
        if (value != null) {
            output.append(value);
        }
    }
}