package css.models;

import java.util.List;

public class Stylesheet {
    private final List<Ruleset> rulesets;

    public Stylesheet(List<Ruleset> rulesets) {
        this.rulesets = rulesets;
    }
    public List<Ruleset> getRulesets() {
        return rulesets;
    }
}
