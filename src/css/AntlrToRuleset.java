package css;

import antlr.css.CSSParser;
import antlr.css.CSSParserBaseVisitor;
import css.models.Node;
import css.models.declarations.Declaration;
import css.models.declarations.Property;
import css.models.declarations.Value;
import css.models.declarations.valueparts.*;

import java.util.ArrayList;
import java.util.List;

public class AntlrToRuleset extends CSSParserBaseVisitor<Node> {
    @Override
    public ValuePart visitValuePart(CSSParser.ValuePartContext ctx) {
        int line = ctx.getStart().getLine();
        if(ctx.IDENT() != null)
            return new IdentifierValue(ctx.IDENT().getText(),line);
        if (ctx.NUMBER() != null) {
            return new NumberValue(ctx.NUMBER().getText(), line);
        }
        if (ctx.DIMENSION() != null) {
            String raw = ctx.DIMENSION().getText();

            // split number + unit
            int i = 0;
            while (i < raw.length() &&
                    (Character.isDigit(raw.charAt(i)) || raw.charAt(i) == '.')) {
                i++;
            }

            String numberPart = raw.substring(0, i);
            String unitPart = raw.substring(i);

            return new DimensionValue(
                    new NumberValue(numberPart,line),
                    unitPart,
                    line
            );
        }
        if (ctx.PERCENT() != null) {
            String raw = ctx.PERCENT().getText(); // e.g. "50%"
            String numberPart = raw.substring(0, raw.length() - 1);

            return new PercentValue(
                    new NumberValue(numberPart,line),
                    line
            );
        }
        if (ctx.HEX() != null) {
            return new HexColorValue(ctx.HEX().getText(),line);
        }
        if (ctx.STRING() != null) {
            return new StringValue(ctx.STRING().getText(),line);
        }
        if(ctx.function_() != null){
            return visitFunction_(ctx.function_());
        }
        return null;
    }

    @Override
    public FunctionValue visitFunction_(CSSParser.Function_Context ctx) {
        int line = ctx.getStart().getLine();
        IdentifierValue name = new IdentifierValue(ctx.getText(),line);
        List<ValuePart> valueParts = List.of();
        if(ctx.value()!= null){
            valueParts = visitValue(ctx.value()).getValueParts();
        }
        return new FunctionValue(name,valueParts,line);
    }

    @Override
    public Value visitValue(CSSParser.ValueContext ctx) {
        int line = ctx.getStart().getLine();
        List<ValuePart> valueParts = new ArrayList<>();
        for(var part : ctx.valuePart()){
            valueParts.add(visitValuePart(part));
        }
        return new Value(valueParts,line);
    }

    @Override
    public Property visitProperty(CSSParser.PropertyContext ctx) {
        int line = ctx.getStart().getLine();

        String name = ctx.IDENT().getText();
        return new Property(name,line);
    }

    @Override
    public Declaration visitDeclaration(CSSParser.DeclarationContext ctx) {
        int line = ctx.getStart().getLine();
        Property property = visitProperty(ctx.property());
        Value value = visitValue(ctx.value());
        boolean important = ctx.IMPORTANT() != null;
        return new Declaration(property,value,important,line);
    }
}
