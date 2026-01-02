package Jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import Jinja2.models.Root;

public  class Number extends Primary{
    public double number;
    public Number(double number,int lineNumber){
        super("Number", lineNumber);
        this.number=number;
    }

    public String toString() {
        return Double.toString(number);
    }

    public List<Root> getChildren() {
        return new ArrayList<>();
    }
}
