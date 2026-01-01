package css.models.declarations.valueparts;

import css.models.Node;

public class StringValue extends ValuePart {
    private final String value;

    public StringValue(String raw,int line) {
        this.value = unquote(raw);
        this.line = line;
    }

    private String unquote(String raw) {
        if (raw.length() >= 2) {
            char first = raw.charAt(0);
            char last  = raw.charAt(raw.length() - 1);

            if ((first == '"' && last == '"') ||
                    (first == '\'' && last == '\'')) {
                return raw.substring(1, raw.length() - 1);
            }
        }
        return raw;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
