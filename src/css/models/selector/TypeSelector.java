package css.models.selector;

public final class TypeSelector extends ElementSelector {
    public final String tagName;

    public TypeSelector(String tagName,int line)
    {
        this.tagName = tagName;
        this.line = line;
    }
}
