package jinja2.renderer;

import jinja2.models.content.ContentNode;
import jinja2.models.content.HtmlTextNode;
import jinja2.models.content.OutputNode;
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

            if (value != null) {
                output.append(value);
            }

            return;
        }

        throw new UnsupportedOperationException(
                "Template node is not supported yet: "
                        + node.getClass().getSimpleName()
                        + " at line "
                        + node.getLineNumber()
        );
    }
}