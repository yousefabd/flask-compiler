package css.models.declarations.valueparts;

import css.models.Node;

import java.util.List;

public class HexColorValue extends ValuePart {
    private final String hex; // normalized without '#'

    public HexColorValue(String hex,int line) {
        this.hex = normalize(hex);
        this.line = line;
    }

    private String normalize(String raw) {
        String h = raw.startsWith("#") ? raw.substring(1) : raw;

        // Expand shorthand #fff → #ffffff
        if (h.length() == 3) {
            h = "" + h.charAt(0) + h.charAt(0)
                    + h.charAt(1) + h.charAt(1)
                    + h.charAt(2) + h.charAt(2);
        }

        return h.toLowerCase();
    }

    public String getHex() {
        return hex;
    }

    public int red() {
        return Integer.parseInt(hex.substring(0, 2), 16);
    }

    public int green() {
        return Integer.parseInt(hex.substring(2, 4), 16);
    }

    public int blue() {
        return Integer.parseInt(hex.substring(4, 6), 16);
    }

    @Override
    public String toString() {
        return "#" + hex;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
