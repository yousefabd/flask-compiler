package css.models.selector.filter;
//#id
public class IdFilter extends SelectorFilter{
    private final String id;

    public IdFilter(String id,int line) {
        this.id = id;
        this.line = line;
    }

    public String getId() {
        return id;
    }
}
