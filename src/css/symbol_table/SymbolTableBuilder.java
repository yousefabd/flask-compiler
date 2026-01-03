package css.symbol_table;

import css.models.Ruleset;
import css.models.Stylesheet;
import css.models.declarations.Declaration;

public class SymbolTableBuilder {
    private final SymbolTable table;

    public SymbolTableBuilder(SymbolTable table) {
        this.table = table;
    }

    public void build(Stylesheet stylesheet) {
        for (Ruleset ruleset : stylesheet.getRulesets()) {
            visitRuleset(ruleset);
        }
    }

    private void visitRuleset(Ruleset ruleset) {
        table.pushScope();

        for (Declaration declaration : ruleset.getDeclarations()) {
            visitDeclaration(declaration);
        }

        table.popScope();
    }

    private void visitDeclaration(Declaration declaration) {
        System.out.println("Visit decl");
        if (declaration.isVariable()) {
            System.out.println("Define variable");
            table.define(
                    declaration.getProperty().getName().substring(2),
                    declaration.getValue()
            );
        }
    }
    public SymbolTable getSymbolTable(){
        return table;
    }
}
