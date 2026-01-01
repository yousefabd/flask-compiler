package css.models.selector.filter;
// .className
public class ClassFilter extends SelectorFilter {
    private final String className;

    public ClassFilter(String className,int line)
    {
        this.className = className;
        this.line = line;
    }

    public String getClassName() {
        return className;
    }
}
