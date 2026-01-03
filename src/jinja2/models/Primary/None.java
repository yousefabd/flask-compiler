package jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import jinja2.models.Root;

public class None extends Primary {
    public None(int lineNumber){
        super("None", lineNumber);
    }

    public String toString(){
        return "";
    }

    public List<Root> getChildren()
    {
        return new ArrayList<>();
    }
}
