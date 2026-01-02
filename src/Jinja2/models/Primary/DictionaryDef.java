package Jinja2.models.Primary;

import java.util.ArrayList;
import java.util.List;

import Jinja2.models.Root;
import Jinja2.models.expression.Expression;

public class DictionaryDef extends Primary{
    ArrayList<Expression> keys, values;
    public DictionaryDef(int lineNumber) {
        super("DictionaryDef", lineNumber);
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void addKeyValue(Expression key, Expression value) {
        keys.add(key);
        values.add(value);
    }

    @Override
    public String toString() {
        return "(Key|Value)*: ";
    }

    public List<Root> getChildren() {
        ArrayList<Root> res = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            res.add(keys.get(i));
            res.add(values.get(i));
        }
        return res;
    }

}
