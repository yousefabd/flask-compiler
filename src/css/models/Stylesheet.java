package css.models;

import java.util.ArrayList;
import java.util.List;

public class Stylesheet extends Node{
    private final List<Ruleset> rulesets;

    public Stylesheet(List<Ruleset> rulesets) {
        this.rulesets = rulesets;
    }
    public List<Ruleset> getRulesets() {
        return rulesets;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(rulesets);
    }
}
