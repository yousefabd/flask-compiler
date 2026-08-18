package css.visitors;

import antlr.css.CSSParser;
import antlr.css.CSSParserBaseVisitor;
import css.models.Node;
import css.models.Ruleset;
import css.models.declarations.*;
import css.models.declarations.valueparts.*;
import css.models.enums.AttributeOperator;
import css.models.selector.Combinator;
import css.models.selector.*;
import css.models.selector.filter.*;
import css.models.selector.filter.pseudoargs.*;

import java.util.ArrayList;
import java.util.List;

public class AntlrToRuleset extends CSSParserBaseVisitor<Node> {



    //Value parts  border : 1px 1px 1px (1px) <---
    @Override
    public ValuePart visitValuePart(CSSParser.ValuePartContext ctx) {
        int line = ctx.getStart().getLine();

        if (ctx.IDENT() != null)
            return new IdentifierValue(ctx.IDENT().getText(), line);

        if (ctx.NUMBER() != null)
            return new NumberValue(ctx.NUMBER().getText(), line);

        if (ctx.DIMENSION() != null) {
            String raw = ctx.DIMENSION().getText();
            int i = 0;
            while (i < raw.length() &&
                    (Character.isDigit(raw.charAt(i)) || raw.charAt(i) == '.')) {
                i++;
            }

            return new DimensionValue(
                    new NumberValue(raw.substring(0, i), line),
                    raw.substring(i),
                    line
            );
        }

        if (ctx.PERCENT() != null) {
            String raw = ctx.PERCENT().getText();
            return new PercentValue(
                    new NumberValue(raw.substring(0, raw.length() - 1), line),
                    line
            );
        }

        if (ctx.HEX() != null)
            return new HexColorValue(ctx.HEX().getText(), line);

        if (ctx.STRING() != null)
            return new StringValue(ctx.STRING().getText(), line);

        if (ctx.variable() != null)
            return new VariableValue(
                    ctx.variable().IDENT().getText(),
                    line
            );

        if (ctx.function_() != null)
            return visitFunction_(ctx.function_());

        throw new IllegalStateException("Unknown valuePart at line " + line);
    }

    @Override
    public FunctionValue visitFunction_(CSSParser.Function_Context ctx) {
        int line = ctx.getStart().getLine();

        IdentifierValue name =
                new IdentifierValue(ctx.IDENT().getText(), line);

        List<ValuePart> arguments = List.of();

        if (ctx.value() != null) {
            arguments = visitValue(ctx.value()).getValueParts();
        }

        return new FunctionValue(name, arguments, line);
    }

    //Value border: (1px circular red) <---
    @Override
    public Value visitValue(CSSParser.ValueContext ctx) {
        int line = ctx.getStart().getLine();
        List<ValuePart> valueParts = new ArrayList<>();
        for(var part : ctx.valuePart()){
            valueParts.add(visitValuePart(part));
        }
        return new Value(valueParts,line);
    }

    //font-family border-radius flex etc
    @Override
    public Property visitProperty(CSSParser.PropertyContext ctx) {
        int line = ctx.getStart().getLine();

        String name = ctx.IDENT().getText();
        return new Property(name,line);
    }

    // attr: value
    @Override
    public Declaration visitDeclaration(CSSParser.DeclarationContext ctx) {
        int line = ctx.getStart().getLine();
        Property property = visitProperty(ctx.property());
        Value value = visitValue(ctx.value());
        boolean important = ctx.IMPORTANT() != null;
        return new Declaration(property,value,important,line);
    }
    //Selection
    //#id1
    @Override
    public SelectorFilter visitIdSelector(CSSParser.IdSelectorContext ctx) {
        int line = ctx.getStart().getLine();
        String id = ctx.IDENT().getText();
        return new IdFilter(id, line);
    }
    //.topBar
    @Override
    public SelectorFilter visitClassSelector(CSSParser.ClassSelectorContext ctx) {
        int line = ctx.getStart().getLine();
        String className = ctx.IDENT().getText();
        return new ClassFilter(className, line);
    }
    //[attr=value]
    @Override
    public SelectorFilter visitAttributeSelector(CSSParser.AttributeSelectorContext ctx) {
        int line = ctx.getStart().getLine();

        String attrName = ctx.IDENT(0).getText();

        AttributeOperator operator = AttributeOperator.BOOLEAN;
        String value = null;

        if (ctx.EQUAL() != null || ctx.PREFIX() != null
                || ctx.SUFFIX() != null || ctx.SUBSTR() != null) {

            if (ctx.EQUAL() != null) {
                operator = AttributeOperator.EQUALS;
            } else if (ctx.PREFIX() != null) {
                operator = AttributeOperator.PREFIX;
            } else if (ctx.SUFFIX() != null) {
                operator = AttributeOperator.SUFFIX;
            } else if (ctx.SUBSTR() != null) {
                operator = AttributeOperator.SUBSTRING;
            }

            // value: IDENT or STRING
            if (ctx.IDENT().size() > 1) {
                value = ctx.IDENT(1).getText();
            } else {
                value = ctx.STRING().getText();
            }
        }

        return new AttributeFilter(attrName, operator, value, line);
    }
    //class::active
    @Override
    public SelectorFilter visitPseudoClass(CSSParser.PseudoClassContext ctx) {
        int line = ctx.getStart().getLine();
        String name = ctx.IDENT().getText();

        return new PseudoClass(name, null, line);
    }
    //class:active(1)
    @Override
    public SelectorFilter visitPseudoClassWithArgs(CSSParser.PseudoClassWithArgsContext ctx) {

        int line = ctx.getStart().getLine();
        String name = ctx.IDENT().getText();

        PseudoArgument argument = visitPseudoArgument(ctx.pseudoArgument());

        return new PseudoClass(name, argument, line);
    }
    //class:before
    @Override
    public SelectorFilter visitPseudoElement(CSSParser.PseudoElementContext ctx) {
        int line = ctx.getStart().getLine();
        String name = ctx.IDENT().getText();

        return new PseudoElement(name, line);
    }
    @Override
    public PseudoArgument visitPseudoArgument(CSSParser.PseudoArgumentContext ctx) {
        int line = ctx.getStart().getLine();

        if (ctx.IDENT() != null) {
            return new IdentifierArg(ctx.IDENT().getText(), line);
        }

        if (ctx.NUMBER() != null) {
            return new NumberArg(
                    Double.parseDouble(ctx.NUMBER().getText()),
                    line
            );
        }

        if (ctx.STRING() != null) {
            // strip quotes
            String raw = ctx.STRING().getText();
            String value = raw.substring(1, raw.length() - 1);
            return new StringArg(value, line);
        }

        if (ctx.selector() != null) {
            throw new UnsupportedOperationException(
                    "Selector arguments in pseudo-classes are not supported yet"
            );
        }
        return null;
    }

    @Override
    public SimpleSelector visitSelectorSequence(CSSParser.SelectorSequenceContext ctx) {
        int line = ctx.getStart().getLine();

        ElementSelector elementSelector = null;
        List<SelectorFilter> filters = new ArrayList<>();

        if (ctx.typeSelector() != null) {
            elementSelector = visitTypeSelector(ctx.typeSelector());
        }

        for (var suffix : ctx.selectorSuffix()) {
            filters.add((SelectorFilter) visit(suffix));
        }

        return new SimpleSelector(elementSelector, filters, line);
    }

    @Override
    public ElementSelector visitTypeSelector(CSSParser.TypeSelectorContext ctx) {
        int line = ctx.getStart().getLine();

        if (ctx.IDENT() != null) {
            return new TypeSelector(ctx.IDENT().getText(), line);
        }

        // STAR
        return new UniversalSelector(line);
    }
    @Override
    public Selector visitSelector(CSSParser.SelectorContext ctx) {
        List<SelectorPart> parts = new ArrayList<>();
        int line = ctx.getStart().getLine();

        // 1️⃣ First selectorSequence (no combinator)
        SimpleSelector first = visitSelectorSequence(ctx.selectorSequence(0));
        parts.add(new SelectorPart(null, first, first.getLine()));

        // 2️⃣ Remaining selectorSequence(s) with combinators
        for (int i = 1; i < ctx.selectorSequence().size(); i++) {
            CSSParser.CombinatorContext combCtx = ctx.combinator(i - 1);
            Combinator combinator = (Combinator) visit(combCtx);

            SimpleSelector selector = visitSelectorSequence(ctx.selectorSequence(i));
            parts.add(new SelectorPart(combinator, selector, selector.getLine()));
        }

        return new Selector(parts, line);
    }
    @Override
    public Combinator visitDescendant(CSSParser.DescendantContext ctx) {
        int line = ctx.getStart().getLine();
        return new Combinator(Combinator.Type.DESCENDANT,line);
    }

    @Override
    public Combinator visitChild(CSSParser.ChildContext ctx) {
        int line = ctx.getStart().getLine();
        return new Combinator(Combinator.Type.CHILD,line);
    }

    @Override
    public Combinator visitAdjacent(CSSParser.AdjacentContext ctx) {
        int line = ctx.getStart().getLine();
        return new Combinator(Combinator.Type.ADJACENT,line);
    }

    @Override
    public Combinator visitSibling(CSSParser.SiblingContext ctx) {
        int line = ctx.getStart().getLine();
        return new Combinator(Combinator.Type.SIBLING,line);
    }

    @Override
    public SelectorGroup visitSelectorGroup(CSSParser.SelectorGroupContext ctx) {
        List<Selector> selectors = new ArrayList<>();

        for (var selectorCtx : ctx.selector()) {
            selectors.add(visitSelector(selectorCtx));
        }

        int line = ctx.start.getLine();
        return new SelectorGroup(selectors, line);
    }

    //Rule set

    @Override
    public Ruleset visitRuleset(CSSParser.RulesetContext ctx) {
        SelectorGroup selectorGroup = visitSelectorGroup(ctx.selectorGroup());

        List<Declaration> declarations = new ArrayList<>();
        if (ctx.declarationList() != null) {
            for (var declCtx : ctx.declarationList().declaration()) {
                declarations.add(visitDeclaration(declCtx));
            }
        }

        return new Ruleset(
                selectorGroup,
                declarations,
                ctx.start.getLine()
        );
    }

}
