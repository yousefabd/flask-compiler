package html.formatting;

import java.util.Objects;

@FunctionalInterface
public interface HtmlFormatter {

    String format(String html);

    static HtmlFormatter unchanged() {
        return html ->
                Objects.requireNonNull(html);
    }
}