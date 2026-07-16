package codegen;

import errors.CodeGenError;

import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.atom_statement.*;
import python.models.compound_statement.*;
import python.models.enums.Operation;
import python.models.expr_statement.*;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.root.CompoundStatement;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.small_statement.*;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Walks the Python AST (the intermediate representation produced by
 * {@code PythonVisitor}) and emits executable Python source code.
 *
 * <p>The generator follows the same instanceof-dispatch style as the
 * existing {@code SymbolTableBuilder} visitors. Every unknown node or
 * unmapped operator raises a {@link CodeGenError} carrying the node's
 * source line, so failures are reported instead of producing broken
 * output silently.</p>
 */
public class PythonCodeGenerator {

    private static final String INDENT = "    ";

    private final String sourceFile;    // used only for error messages
    /** true once a decorator rooted at `app` (e.g. @app.route) has been generated. */
    private boolean usesFlaskApp = false;

    public PythonCodeGenerator(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public boolean usesFlaskApp() {
        return usesFlaskApp;
    }

    // ─────────────────────────────────────────────────────────────
    // PROGRAM
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates the whole module. When a Flask app is detected, a runtime
     * error-handling block (404 + 500 handlers) is inserted before the
     * trailing {@code if __name__ == "__main__"} guard so the generated
     * app never crashes with a bare traceback in the browser.
     */
    public String generate(Program program) {
        StringBuilder out = new StringBuilder();
        boolean handlersEmitted = false;

        List<Statement> statements = program.statements != null
                ? program.statements : new ArrayList<>();

        for (Statement st : statements) {
            if (!handlersEmitted && usesFlaskApp && isMainGuard(st)) {
                out.append(flaskErrorHandlerBlock());
                handlersEmitted = true;
            }
            out.append(generateStatement(st, 0));
            if (st instanceof CompoundStatement)
                out.append('\n'); // blank line after top-level compound statements
        }

        if (!handlersEmitted && usesFlaskApp)
            out.append(flaskErrorHandlerBlock());

        return out.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // STATEMENTS
    // ─────────────────────────────────────────────────────────────

    private String generateStatement(Statement st, int level) {
        if (st instanceof SimpleStatement ss)
            return generateSimpleStatement(ss, level);
        if (st instanceof IfStatement is)
            return generateIfStatement(is, level);
        if (st instanceof WhileStatement ws)
            return generateWhileStatement(ws, level);
        if (st instanceof ForStatement fs)
            return generateForStatement(fs, level);
        if (st instanceof DecoratorStatement ds)
            return generateDecoratorStatement(ds, level);
        throw unsupported(st);
    }

    private String generateSimpleStatement(SimpleStatement ss, int level) {
        List<String> parts = new ArrayList<>();
        for (SmallStatement sm : ss.smallStatementList)
            parts.add(generateSmallStatement(sm));
        return indent(level) + String.join("; ", parts) + "\n";
    }

    private String generateSmallStatement(SmallStatement sm) {
        if (sm instanceof ExpressionStatement es)
            return generateExpressionStatement(es);
        if (sm instanceof AugAssignStatement aas)
            return aas.id.name + " " + augAssignOperator(aas) + " " + expr(aas.expression);
        if (sm instanceof ReturnStatement rs)
            return generateReturn(rs);
        if (sm instanceof PassStatement)     return "pass";
        if (sm instanceof BreakStatement)    return "break";
        if (sm instanceof ContinueStatement) return "continue";
        if (sm instanceof GlobalStatement gs)
            return "global " + joinIds(gs.names);
        if (sm instanceof SimpleImportStatement sis)
            return "import " + dotted(sis.dottedName);
        if (sm instanceof FromImportStatement fis)
            return "from " + dotted(fis.dottedName) + " import "
                    + (fis.hasStar() ? "*" : joinIds(fis.targets));
        throw unsupported(sm);
    }

    private String generateExpressionStatement(ExpressionStatement es) {
        String targets = joinConditions(es.conditions);
        if (es.HaveEquals())
            return targets + " = " + joinConditions(es.assigns);
        return targets;
    }

    private String generateReturn(ReturnStatement rs) {
        if (rs.conditions == null || rs.conditions.isEmpty()) return "return";
        return "return " + joinConditions(rs.conditions);
    }

    private String generateIfStatement(IfStatement is, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < is.conditions.size(); i++) {
            String keyword = (i == 0) ? "if" : "elif";
            sb.append(indent(level)).append(keyword).append(' ')
              .append(condition(is.conditions.get(i))).append(":\n")
              .append(generateBody(is.bodies.get(i), level + 1));
        }
        if (is.last != null)
            sb.append(indent(level)).append("else:\n")
              .append(generateBody(is.last, level + 1));
        return sb.toString();
    }

    private String generateWhileStatement(WhileStatement ws, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(level)).append("while ")
          .append(condition(ws.condition)).append(":\n")
          .append(generateBody(ws.body, level + 1));
        if (ws.last != null)
            sb.append(indent(level)).append("else:\n")
              .append(generateBody(ws.last, level + 1));
        return sb.toString();
    }

    private String generateForStatement(ForStatement fs, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(level)).append("for ").append(joinIds(fs.iterators))
          .append(" in ").append(expr(fs.iterable)).append(":\n")
          .append(generateBody(fs.body, level + 1));
        if (fs.last != null)
            sb.append(indent(level)).append("else:\n")
              .append(generateBody(fs.last, level + 1));
        return sb.toString();
    }

    private String generateDecoratorStatement(DecoratorStatement ds, int level) {
        StringBuilder sb = new StringBuilder();
        if (ds.decorators != null)
            for (Decorator dec : ds.decorators)
                sb.append(generateDecorator(dec, level));
        sb.append(generateFunctionDef(ds.function, level));
        return sb.toString();
    }

    private String generateDecorator(Decorator dec, int level) {
        if (dec.dottedName != null && !dec.dottedName.isEmpty()
                && "app".equals(dec.dottedName.get(0).name))
            usesFlaskApp = true;

        StringBuilder sb = new StringBuilder();
        sb.append(indent(level)).append('@').append(dotted(dec.dottedName));
        if (dec.arguments != null && !dec.arguments.isEmpty()) {
            List<String> args = new ArrayList<>();
            for (Argument arg : dec.arguments)
                args.add(argument(arg));
            sb.append('(').append(String.join(", ", args)).append(')');
        }
        sb.append('\n');
        return sb.toString();
    }

    private String generateFunctionDef(FunctionDef fd, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(level)).append("def ").append(fd.id.name).append('(');
        List<String> params = new ArrayList<>();
        if (fd.parameters != null)
            for (Parameter p : fd.parameters)
                params.add(parameter(p));
        sb.append(String.join(", ", params)).append(')');
        if (fd.returnType != null)
            sb.append(" -> ").append(condition(fd.returnType));
        sb.append(":\n").append(generateBody(fd.body, level + 1));
        return sb.toString();
    }

    private String parameter(Parameter p) {
        StringBuilder sb = new StringBuilder(p.id.name);
        if (p.hasType())
            sb.append(": ").append(condition(p.type));
        if (p.hasDefaultValue())
            sb.append(p.hasType() ? " = " : "=").append(condition(p.defaultValue));
        return sb.toString();
    }

    private String generateBody(Body body, int level) {
        if (body == null || body.statements == null || body.statements.isEmpty())
            return indent(level) + "pass\n";
        StringBuilder sb = new StringBuilder();
        for (Statement st : body.statements)
            sb.append(generateStatement(st, level));
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // CONDITIONS  (and / or / not / comparisons)
    // ─────────────────────────────────────────────────────────────

    private String condition(Condition cond) {
        if (cond instanceof CompoundCondition cc)
            return compoundCondition(cc);
        if (cond instanceof RelationalComparison rc)
            return relational(rc);
        if (cond instanceof Expression ex)
            return expr(ex);
        throw unsupported(cond);
    }

    private String compoundCondition(CompoundCondition cc) {
        if (cc.operation == Operation.NOT)
            return "not " + wrapIfCompound(cc.first);
        String op = switch (cc.operation) {
            case AND -> "and";
            case OR  -> "or";
            default  -> throw unsupportedOp(cc, cc.operation);
        };
        return wrapIfCompound(cc.first) + " " + op + " " + wrapIfCompound(cc.second);
    }

    /**
     * The grammar keeps explicit parentheses only as ParenAtom nodes, so a
     * nested and/or/not operand is parenthesized here to guarantee the
     * generated code keeps the AST's grouping under real Python precedence.
     */
    private String wrapIfCompound(Condition cond) {
        String text = condition(cond);
        return (cond instanceof CompoundCondition) ? "(" + text + ")" : text;
    }

    private String relational(RelationalComparison rc) {
        String op = switch (rc.operation) {
            case EQUALS       -> "==";
            case NOT_EQ       -> "!=";
            case LESS_THAN    -> "<";
            case GREATER_THAN -> ">";
            case LT_EQ        -> "<=";
            case GT_EQ        -> ">=";
            case IN           -> "in";
            case NOTIN        -> "not in";
            case IS           -> "is";
            case ISNOT        -> "is not";
            default           -> throw unsupportedOp(rc, rc.operation);
        };
        return expr(rc.left) + " " + op + " " + expr(rc.right);
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS
    // ─────────────────────────────────────────────────────────────

    private String expr(Expression ex) {
        if (ex instanceof BinaryExpression be)
            return expr(be.left) + " " + binaryOperator(be) + " " + expr(be.right);
        if (ex instanceof UnaryExpression ue)
            return unaryOperator(ue) + expr(ue.expression);
        if (ex instanceof IDTrailer idt)
            return idTrailer(idt);
        if (ex instanceof Atom atom)
            return atom(atom);
        throw unsupported(ex);
    }

    private String idTrailer(IDTrailer idt) {
        StringBuilder sb = new StringBuilder(idt.id.name);
        if (idt.trailers != null)
            for (Trailer tr : idt.trailers)
                sb.append(trailer(tr));
        return sb.toString();
    }

    private String trailer(Trailer tr) {
        StringBuilder sb = new StringBuilder();
        if (tr.isDotIdTrailer())
            sb.append('.').append(tr.id.name);
        if (tr.hasArguments()) {
            if (tr.arguments instanceof CallArguments ca) {
                List<String> args = new ArrayList<>();
                if (ca.args != null)
                    for (Argument arg : ca.args)
                        args.add(argument(arg));
                sb.append('(').append(String.join(", ", args)).append(')');
            } else if (tr.arguments instanceof SubscriptArguments sa) {
                sb.append('[').append(joinConditions(sa.conditions)).append(']');
            } else {
                throw unsupported(tr.arguments);
            }
        }
        return sb.toString();
    }

    private String argument(Argument arg) {
        if (arg.isAssigned())
            return condition(arg.arg) + "=" + condition(arg.assign);
        return condition(arg.arg);
    }

    // ─────────────────────────────────────────────────────────────
    // ATOMS
    // ─────────────────────────────────────────────────────────────

    private String atom(Atom atom) {
        if (atom instanceof ID id)             return id.name;
        if (atom instanceof IntegerAtom ia)    return Integer.toString(ia.value);
        if (atom instanceof FloatAtom fa)      return Float.toString(fa.value);
        if (atom instanceof BoolAtom ba)       return ba.value ? "True" : "False";
        if (atom instanceof StringAtom sa)     return sa.value;   // raw token, quotes included
        if (atom instanceof None)              return "None";
        if (atom instanceof ParenAtom pa)      return "(" + expr(pa.inner) + ")";
        if (atom instanceof python.models.atom_statement.List la)
            return "[" + joinExpressions(la.content) + "]";
        if (atom instanceof Dictionary dict)   return dictionary(dict);
        if (atom instanceof Set set)
            return "{" + joinExpressions(set.content) + "}";
        throw unsupported(atom);
    }

    private String dictionary(Dictionary dict) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < dict.keys.size(); i++)
            items.add(expr(dict.keys.get(i)) + ": " + expr(dict.values.get(i)));
        return "{" + String.join(", ", items) + "}";
    }

    // ─────────────────────────────────────────────────────────────
    // OPERATOR MAPPINGS
    // ─────────────────────────────────────────────────────────────

    private String binaryOperator(BinaryExpression be) {
        return switch (be.operation) {
            case ADD    -> "+";
            case SUB    -> "-";
            case MULT   -> "*";
            case DIV    -> "/";
            case IDIV   -> "//";
            case MOD    -> "%";
            case POWER  -> "**";
            case AT     -> "@";
            case AND    -> "&";
            case OR     -> "|";
            case XOR    -> "^";
            case LSHIFT -> "<<";
            case RSHIFT -> ">>";
            default     -> throw unsupportedOp(be, be.operation);
        };
    }

    private String unaryOperator(UnaryExpression ue) {
        return switch (ue.operation) {
            case ADD    -> "+";
            case SUB    -> "-";
            case NOT_OP -> "~";
            default     -> throw unsupportedOp(ue, ue.operation);
        };
    }

    private String augAssignOperator(AugAssignStatement aas) {
        return switch (aas.operation) {
            case ADD    -> "+=";
            case SUB    -> "-=";
            case MULT   -> "*=";
            case DIV    -> "/=";
            case IDIV   -> "//=";
            case MOD    -> "%=";
            case POWER  -> "**=";
            case AND    -> "&=";
            case OR     -> "|=";
            case XOR    -> "^=";
            case LSHIFT -> "<<=";
            case RSHIFT -> ">>=";
            default     -> throw unsupportedOp(aas, aas.operation);
        };
    }

    // ─────────────────────────────────────────────────────────────
    // FLASK RUNTIME ERROR HANDLING (generated support code)
    // ─────────────────────────────────────────────────────────────

    private static String flaskErrorHandlerBlock() {
        return """

                # ---- generated by the miniFlask compiler: runtime error handling ----
                @app.errorhandler(404)
                def _mf_not_found(error):
                    return "<h1>404 - Page Not Found</h1><p>The requested URL was not found on the server.</p>", 404


                @app.errorhandler(Exception)
                def _mf_internal_error(error):
                    from werkzeug.exceptions import HTTPException
                    if isinstance(error, HTTPException):
                        return error
                    return "<h1>500 - Internal Server Error</h1><p>" + str(error) + "</p>", 500


                """;
    }

    /** Detects the trailing {@code if __name__ == "__main__":} guard. */
    private boolean isMainGuard(Statement st) {
        if (!(st instanceof IfStatement is) || is.conditions.isEmpty()) return false;
        return mentionsDunderName(is.conditions.get(0));
    }

    private boolean mentionsDunderName(ASTNode node) {
        if (node instanceof ID id && "__name__".equals(id.name)) return true;
        for (ASTNode child : node.getChildren())
            if (child != null && mentionsDunderName(child)) return true;
        return false;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    private String joinConditions(List<Condition> conditions) {
        List<String> parts = new ArrayList<>();
        if (conditions != null)
            for (Condition c : conditions)
                parts.add(condition(c));
        return String.join(", ", parts);
    }

    private String joinExpressions(List<Expression> expressions) {
        List<String> parts = new ArrayList<>();
        if (expressions != null)
            for (Expression e : expressions)
                parts.add(expr(e));
        return String.join(", ", parts);
    }

    private String joinIds(List<ID> ids) {
        List<String> parts = new ArrayList<>();
        if (ids != null)
            for (ID id : ids)
                parts.add(id.name);
        return String.join(", ", parts);
    }

    private String dotted(List<ID> ids) {
        List<String> parts = new ArrayList<>();
        if (ids != null)
            for (ID id : ids)
                parts.add(id.name);
        return String.join(".", parts);
    }

    private static String indent(int level) {
        return INDENT.repeat(level);
    }

    private CodeGenError unsupported(Object node) {
        if (node instanceof ASTNode ast)
            return new CodeGenError(sourceFile, ast.getLine(),
                    "No Python translation defined for AST node " + ast.describe());
        return new CodeGenError(sourceFile, -1,
                "No Python translation defined for " + (node == null ? "null" : node.getClass().getName()));
    }

    private CodeGenError unsupportedOp(ASTNode node, Operation op) {
        return new CodeGenError(sourceFile, node.getLine(),
                "Operator " + op + " has no Python mapping in " + node.describe());
    }
}
