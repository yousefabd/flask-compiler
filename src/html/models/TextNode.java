package html.models;

public class TextNode implements Node {
    private String textContent;
    private final int line;

    public TextNode(String textContent,int line) {
        this.textContent = textContent;
        this.line = line;
    }

    public String getTextContent() {
        return textContent;
    }

    public int getLine() {
        return line;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    public String toString() {
        return textContent + '\n';
    }
}
