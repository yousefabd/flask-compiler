package html.models;

public class TextNode implements Node {
    private String textContent;

    public TextNode(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    public String toString() {
        return textContent + "\n";
    }
}
