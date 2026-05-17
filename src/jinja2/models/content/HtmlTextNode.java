package jinja2.models.content;

import jinja2.models.TemplateNode;

import java.util.Collections;
import java.util.List;

public class HtmlTextNode extends ContentNode {

    private final String text;

    public HtmlTextNode(
            String text,
            int lineNumber) {

        super(lineNumber);

        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public List<? extends TemplateNode> getChildren() {
        return Collections.emptyList();
    }

}