package jinja2.symbol_table;

import jinja2.models.attribute.HtmlAttributeNode;
import jinja2.models.attribute.valuepart.*;
import jinja2.models.content.*;
import jinja2.models.content.html.*;
import jinja2.models.expression.*;
import jinja2.models.expression.literal.*;
import jinja2.models.file.TemplateFile;
import jinja2.models.statement.*;
import jinja2.symbol_table.semantic_rules.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTableBuilder {
    private final SymbolTable  symbolTable;
    private final List<CompilerError> errors;
    private final List<ISemanticRule> semanticRules;

    public SymbolTableBuilder(SymbolTable symbolTable, List<CompilerError> errors,
                              List<ISemanticRule> semanticRules) {
        this.symbolTable   = symbolTable;
        this.errors        = errors;
        this.semanticRules = semanticRules;
    }

    public void build(TemplateFile template) {
        visitTemplateFile(template);
        SemanticContext semanticContext = new SemanticContext(template, symbolTable, errors);
        for (ISemanticRule rule : semanticRules)
            rule.validate(semanticContext);
    }

    // ─────────────────────────────────────────────────────────────
    // TEMPLATE
    // ─────────────────────────────────────────────────────────────

    private void visitTemplateFile(TemplateFile template) {
        for (ContentNode child : template.getContentChildren())
            visitContent(child);
    }

    // ─────────────────────────────────────────────────────────────
    // CONTENT DISPATCH
    // ─────────────────────────────────────────────────────────────

    private void visitContent(ContentNode node) {
        if (node instanceof ForStatementNode fs)
            visitForStatement(fs);
        else if (node instanceof IfStatementNode is)
            visitIfStatement(is);
        else if (node instanceof SetStatementNode ss)
            visitSetStatement(ss);
        else if (node instanceof MacroStatementNode ms)
            visitMacroStatement(ms);
        else if (node instanceof BlockStatementNode bs)
            visitBlockStatement(bs);
        else if (node instanceof OutputNode out)
            visitExpression(out.getExpression());
        else if (node instanceof HTMLNormalElementNode el)
            visitNormalElement(el);
        else if (node instanceof HTMLVoidElementNode el)
            visitVoidElement(el);
        // HtmlTextNode, ExtendsStatementNode, IncludeStatementNode
        // — nothing to define or resolve
    }

    // ─────────────────────────────────────────────────────────────
    // STATEMENTS
    // ─────────────────────────────────────────────────────────────

    private void visitForStatement(ForStatementNode statement) {
        // The iterable belongs to the outer scope.
        visitExpression(statement.getIterable());

        symbolTable.enterScope("for", ScopeKind.FOR);

        symbolTable.define(
                new Symbol(
                        "loop",
                        SymbolKind.LOOP_VAR,
                        statement.getLineNumber(),
                        null
                )
        );

        for (IdentifierNode variable : statement.getVariables()) {
            Symbol loopVariable = new Symbol(
                    variable.getName(),
                    SymbolKind.LOOP_VAR,
                    variable.getLineNumber(),
                    null
            );

            if (!symbolTable.define(loopVariable)) {
                errors.add(
                        new CompilerError(
                                CompilerError.Kind.DUPLICATE_VARIABLE,
                                "Duplicate loop variable '"
                                        + loopVariable.getName()
                                        + "'",
                                variable.getLineNumber()
                        )
                );
            }
        }

        for (ContentNode child : statement.getBody()) {
            visitContent(child);
        }

        symbolTable.exitScope();
    }

    private void visitIfStatement(IfStatementNode is) {
        // {% if %} does not create a scope in Jinja2
        for (IfBranchNode branch : is.getBranches()) {
            if (branch.getCondition() != null)
                visitExpression(branch.getCondition());

            for (ContentNode child : branch.getBody())
                visitContent(child);
        }
    }

    private void visitSetStatement(SetStatementNode statement) {
        /*
         * With one inline target, the complete expression is that variable's
         * value. With multiple targets, we cannot assign the same complete
         * expression to every variable because it must eventually be unpacked.
         */
        ExpressionNode directlyAssignedValue =
                !statement.isBlock()
                        && statement.getTargets().size() == 1
                        ? statement.getValue()
                        : null;

        for (IdentifierNode target : statement.getTargets()) {
            Symbol symbol = new Symbol(
                    target.getName(),
                    SymbolKind.VARIABLE,
                    target.getLineNumber(),
                    directlyAssignedValue
            );

            symbolTable.overwrite(symbol);
        }

        if (statement.isBlock()) {
            for (ContentNode child : statement.getBody()) {
                visitContent(child);
            }
        } else {
            visitExpression(statement.getValue());
        }
    }

//    /** Best-effort literal evaluation for {% set %}, mirroring python.resolver's approach. */
//    private static ConstantValue literalConstant(ExpressionNode expr) {
//        if (expr instanceof StringLiteralNode s)  return ConstantValue.ofString(stripJinjaQuotes(s.getValue()));
//        if (expr instanceof NumberLiteralNode n)
//            return n.getValue().contains(".")
//                    ? ConstantValue.ofFloat(Double.parseDouble(n.getValue()))
//                    : ConstantValue.ofInt(Integer.parseInt(n.getValue()));
//        if (expr instanceof BooleanLiteralNode b) return ConstantValue.ofBool(b.getValue());
//        if (expr instanceof NoneLiteralNode)      return ConstantValue.none();
//        if (expr instanceof ListExpressionNode list) {
//            java.util.List<ConstantValue> items = new java.util.ArrayList<>();
//            for (ExpressionNode el : list.getElements()) {
//                ConstantValue v = literalConstant(el);
//                if (!v.isKnown()) return ConstantValue.unknown();
//                items.add(v);
//            }
//            return ConstantValue.ofList(items);
//        }
//        if (expr instanceof DictionaryExpressionNode dict) {
//            Map<String, ConstantValue> map = new LinkedHashMap<>();
//            for (int i = 0; i < dict.getKeys().size(); i++) {
//                ConstantValue k = literalConstant(dict.getKeys().get(i));
//                ConstantValue v = literalConstant(dict.getValues().get(i));
//                if (!v.isKnown() || k.getKind() != ConstantValue.Kind.STRING) return ConstantValue.unknown();
//                map.put(k.asString(), v);
//            }
//            return ConstantValue.ofDict(map);
//        }
//        return ConstantValue.unknown(); // identifiers, calls, filters, binary ops, ... not provable here
//    }

    private static String stripJinjaQuotes(String raw) {
        if (raw.length() >= 2 && (raw.startsWith("'") || raw.startsWith("\""))
                && raw.endsWith(raw.substring(0, 1)))
            return raw.substring(1, raw.length() - 1);
        return raw;
    }

    private void visitMacroStatement(MacroStatementNode ms) {
        // macro name is visible in the scope it's defined in
        Symbol macroSym = new Symbol(
                ms.getMacroName(),
                ms.getLineNumber(),
                ms.getParameters()         // uses the MACRO constructor
        );
        if (!symbolTable.define(macroSym))
            errors.add(new CompilerError(
                    CompilerError.Kind.DUPLICATE_MACRO,
                    "Duplicate macro '" + ms.getMacroName() + "'",
                    ms.getLineNumber()));

        // Default values are evaluated in the CALLER's scope, so visit them
        // before entering the macro scope.
        for (ParameterNode param : ms.getParameters())
            if (param.hasDefault())
                visitExpression(param.getDefaultValue());

        symbolTable.enterScope("macro " + ms.getMacroName(), ScopeKind.MACRO);

        for (ParameterNode param : ms.getParameters()) {
            Symbol paramSym = new Symbol(
                    param.getName(),
                    SymbolKind.PARAMETER,
                    param.getLineNumber(),
                    param.hasDefault() ? param.getDefaultValue() : null
            );
            if (!symbolTable.define(paramSym))
                errors.add(new CompilerError(
                        CompilerError.Kind.DUPLICATE_PARAMETER,
                        "Duplicate parameter '" + param.getName()
                                + "' in macro '" + ms.getMacroName() + "'",
                        param.getLineNumber()));
        }

        for (ContentNode child : ms.getBody())
            visitContent(child);

        symbolTable.exitScope();
    }

    private void visitBlockStatement(BlockStatementNode bs) {
        // Blocks are always template-level names regardless of nesting depth.
        Symbol blockSym = new Symbol(
                bs.getBlockName(),
                SymbolKind.BLOCK,
                bs.getLineNumber(),
                null  // blocks have no initializer expression
        );
        if (!symbolTable.defineInTemplateScope(blockSym))
            errors.add(new CompilerError(
                    CompilerError.Kind.DUPLICATE_BLOCK,
                    "Duplicate block '" + bs.getBlockName() + "'",
                    bs.getLineNumber()));

        symbolTable.enterScope("block " + bs.getBlockName(), ScopeKind.BLOCK);

        for (ContentNode child : bs.getBody())
            visitContent(child);

        symbolTable.exitScope();
    }

    // ─────────────────────────────────────────────────────────────
    // HTML ELEMENTS — traverse to find Jinja inside attributes/children
    // ─────────────────────────────────────────────────────────────

    private void visitNormalElement(HTMLNormalElementNode element) {
        visitAttributes(element.getAttributes());
        for (ContentNode child : element.getChildren())
            visitContent(child);
    }

    private void visitVoidElement(HTMLVoidElementNode element) {
        visitAttributes(element.getAttributes());
    }

    private void visitAttributes(List<HtmlAttributeNode> attributes) {
        for (HtmlAttributeNode attr : attributes)
            for (AttributeValuePartNode part : attr.getValueParts())
                if (part instanceof AttributeExpressionNode exprPart)
                    visitExpression(exprPart.getExpression());
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS
    // ─────────────────────────────────────────────────────────────

    private void visitExpression(ExpressionNode expr) {
        if (expr instanceof IdentifierNode id)
            visitIdentifier(id);
        else if (expr instanceof BinaryExpressionNode bin) {
            visitExpression(bin.getLeft());
            visitExpression(bin.getRight());
        }
        else if (expr instanceof UnaryExpressionNode un)
            visitExpression(un.getExpression());
        else if (expr instanceof PropertyAccessNode prop)
            // only resolve the root object — the property is a field name, not a variable
            visitExpression(prop.getTarget());
        else if (expr instanceof IndexAccessNode idx) {
            visitExpression(idx.getTarget());
            visitExpression(idx.getIndex());
        }
        else if (expr instanceof CallExpressionNode call) {
            visitExpression(call.getCallee());
            for (ArgumentNode arg : call.getArguments())
                visitExpression(arg.getValue());
        }
        else if (expr instanceof FilterExpressionNode filter) {
            visitExpression(filter.getTarget());
            for (ArgumentNode arg : filter.getArguments())
                visitExpression(arg.getValue());
        }
        else if (expr instanceof ListExpressionNode list)
            for (ExpressionNode el : list.getElements())
                visitExpression(el);
        else if (expr instanceof DictionaryExpressionNode dict) {
            for (ExpressionNode key : dict.getKeys())   visitExpression(key);
            for (ExpressionNode val : dict.getValues()) visitExpression(val);
        }
        // LiteralExpressionNode subtypes — nothing to resolve
    }

    private void visitIdentifier(IdentifierNode id) {

        Symbol visible = symbolTable.resolve(id.getName());

        if (visible != null) {
            // added: this is the resolution step the builder already had the
            // information for (it just discarded it) — record which declaration
            // this identifier node refers to, and that it was read here, so
            // jinja2.resolver.TemplateResolver doesn't need a full second AST
            // walk just to capture what visitIdentifier already knows.
            symbolTable.recordBinding(id, visible);
            visible.addUsage(id.getLineNumber());
            return;
        }

        Symbol declaredSomewhere =
                symbolTable.resolveGlobal(id.getName());

        if (declaredSomewhere != null) {
            errors.add(new CompilerError(
                    CompilerError.Kind.SCOPE,
                    "Variable '" + id.getName() + "' is not visible in this scope",
                    id.getLineNumber()));
        }
        else {
            errors.add(new CompilerError(
                    CompilerError.Kind.UNDEFINED_VARIABLE,
                    "Undefined variable '" + id.getName() + "'",
                    id.getLineNumber()));
        }
    }
}
