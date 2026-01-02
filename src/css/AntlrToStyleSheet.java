package css;

import antlr.css.CSSParser;
import antlr.css.CSSParserBaseVisitor;
import css.models.Ruleset;
import css.models.Stylesheet;

import java.util.ArrayList;
import java.util.List;

public class AntlrToStyleSheet extends CSSParserBaseVisitor<Stylesheet> {
    private Stylesheet stylesheet;

    @Override
    public Stylesheet visitStylesheet(CSSParser.StylesheetContext ctx) {
        List<Ruleset> rulesets = new ArrayList<>();
        AntlrToRuleset rulesetVisitor = new AntlrToRuleset();
        for (var rulesetCtx : ctx.ruleset()) {
            var ruleset = rulesetVisitor.visitRuleset(rulesetCtx);
            rulesets.add(ruleset);
        }
        return new Stylesheet(rulesets);
    }
}
