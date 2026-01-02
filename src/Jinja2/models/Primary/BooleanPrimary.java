package Jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import Jinja2.models.Root;

public class BooleanPrimary extends Primary {
    public boolean value;
    public BooleanPrimary(boolean value,int lineNumber){
        super("BooleanPrimary", lineNumber);
        this.value=value;
    }

    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public List<Root> getChildren() {
        return new ArrayList<>();
    }
}
