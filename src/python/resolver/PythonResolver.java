package python.resolver;

import python.models.ASTNode;
import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
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
import python.symbol_table.CompilerError;
import python.symbol_table.Scope;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;
import python.symbol_table.SymbolTable;

import resolver.ConstantValue;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Binds every identifier read in the Python AST to the {@link Symbol} it
 * refers to, tracks best-effort compile-time constant values, and — as a
 * consequence of actually walking expressions — detects genuinely undefined
 * variables, something the existing {@code SymbolTableBuilder} cannot do
 * (it only ever visits declaration sites, never the conditions of an
 * {@code if}/{@code while}/{@code for}, a function's default values, or the
 * right-hand side of a plain expression statement).
 *
 * <p><b>Why a second, separate pass instead of extending the builder:</b> the
 * builder already owns declaration bookkeeping and six semantic checks
 * (duplicate function, duplicate parameter, return/break/continue misuse,
 * stray {@code global}); folding resolution into it would blur two different
 * responsibilities and risk regressing tested code. A resolver is a
 * standard, separate compiler phase, so it gets its own class and its own
 * (fresh) {@link SymbolTable} — re-declaring symbols as it walks is cheap and
 * keeps the two passes fully independent.</p>
 *
 * <p><b>What "constant value" means here:</b> this is a best-effort, single
 * forward pass over the source text — not a fixed-point dataflow analysis.
 * A symbol's value is "the most recently observed literal assignment", and
 * is invalidated the moment a non-literal assignment, an augmented
 * assignment, or a call that could mutate it (e.g. {@code list.append(...)})
 * is seen anywhere in the program, including inside a function body that
 * merely *might* run later. That is deliberately conservative: it never
 * claims a value is constant unless the source proves it, but it can still
 * under-report (miss a value that dataflow analysis would catch) — an
 * acceptable trade-off for an educational compiler.</p>
 */
public class PythonResolver {

    /**
     * Names available without an explicit `import`, so they are never flagged as
     * undefined. Includes "True"/"False"/"None": this grammar's lexer only
     * recognizes lowercase `true`/`false` as {@code BoolAtom} tokens (see
     * PythonLexer.g4), so the capitalized Python keywords actually used in
     * source (`debug=True`) lex as plain identifiers instead — a pre-existing
     * grammar quirk this resolver works around rather than "fixes" by flagging
     * perfectly valid code as undefined.
     */
    private static final List<String> BUILTINS = List.of(
            "print", "len", "range", "str", "int", "float", "bool", "list", "dict", "set",
            "tuple", "enumerate", "zip", "sorted", "sum", "min", "max", "abs", "isinstance",
            "type", "open", "input", "map", "filter", "any", "all", "round", "repr",
            "__name__", "__file__", "__doc__", "True", "False", "None");

    private final SymbolTable symbolTable = new SymbolTable();
    private final List<CompilerError> errors = new ArrayList<>();
    private final Map<ASTNode, Symbol> bindings = new IdentityHashMap<>();
    private int loopDepth = 0;
    private int functionDepth = 0;

    public PythonResolver() {
        for (String builtin : BUILTINS)
            symbolTable.defineOrGet(new Symbol(builtin, SymbolKind.VARIABLE, -1));
    }

    public void resolve(Program program) {
        if (program.statements == null) return;
        // Real Python effectively lets one module-level function call another
        // regardless of which is defined first in the file (the call only runs
        // once every top-level statement has already executed). A single
        // textual-order walk can't know that, so top-level function *names*
        // are hoisted first — otherwise a route defined above the helper it
        // calls would be flagged as a false "undefined variable". Module-level
        // variables are deliberately NOT hoisted: `x = y` before `y` exists is
        // a real NameError in Python too, so leaving that unhoisted matches
        // actual behavior instead of hiding a genuine class of bug.
        hoistTopLevelFunctions(program.statements);
        for (Statement st : program.statements)
            visitStatement(st);
    }

    private void hoistTopLevelFunctions(List<Statement> statements) {
        for (Statement st : statements)
            if (st instanceof DecoratorStatement ds && ds.function != null && ds.function.id != null)
                declare(ds.function.id.name, SymbolKind.FUNCTION, ds.function.getLine());
    }

    public SymbolTable getSymbolTable()      { return symbolTable; }
    public List<CompilerError> getErrors()   { return errors; }
    public Map<ASTNode, Symbol> getBindings(){ return bindings; }

    /** A readable dump of every scope, its symbols, their resolved values and usage lines. */
    public String report() {
        StringBuilder sb = new StringBuilder();
        sb.append("Python Resolver Report\n");
        sb.append("=======================\n");
        for (Scope scope : symbolTable.getAllScopes()) {
            sb.append("Scope: ").append(scope.getName()).append('\n');
            for (Symbol sym : scope.getSymbols())
                if (!BUILTINS.contains(sym.getName()))
                    sb.append("  - ").append(sym).append('\n');
        }
        if (!errors.isEmpty()) {
            sb.append("\nUndefined variables:\n");
            for (CompilerError e : errors)
                sb.append("  ").append(e).append('\n');
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // STATEMENTS  (mirrors SymbolTableBuilder's traversal shape)
    // ─────────────────────────────────────────────────────────────

    private void visitStatement(Statement st) {
        if (st instanceof CompoundStatement cs) visitCompoundStatement(cs);
        else if (st instanceof SimpleStatement ss) visitSimpleStatement(ss);
    }

    private void visitCompoundStatement(CompoundStatement cs) {
        if (cs instanceof WhileStatement ws) visitWhileStatement(ws);
        else if (cs instanceof ForStatement fs) visitForStatement(fs);
        else if (cs instanceof IfStatement is) visitIfStatement(is);
        else if (cs instanceof DecoratorStatement ds) visitDecoratorStatement(ds);
    }

    private void visitWhileStatement(WhileStatement ws) {
        resolveCondition(ws.condition);
        symbolTable.enterScope("while");
        loopDepth++;
        visitBody(ws.body);
        loopDepth--;
        symbolTable.exitScope();
        if (ws.last != null) {
            symbolTable.enterScope("else");
            visitBody(ws.last);
            symbolTable.exitScope();
        }
    }

    private void visitForStatement(ForStatement fs) {
        resolveExpression(fs.iterable);
        symbolTable.enterScope("for");
        if (fs.iterators != null)
            for (ID id : fs.iterators) {
                Symbol sym = declare(id.name, SymbolKind.VARIABLE, id.getLine());
                bindings.put(id, sym);
                sym.invalidateValue(); // a loop variable changes every iteration
            }
        loopDepth++;
        visitBody(fs.body);
        loopDepth--;
        symbolTable.exitScope();
        if (fs.last != null) {
            symbolTable.enterScope("else");
            visitBody(fs.last);
            symbolTable.exitScope();
        }
    }

    private void visitIfStatement(IfStatement is) {
        for (int i = 0; i < is.conditions.size(); i++) {
            resolveCondition(is.conditions.get(i));
            symbolTable.enterScope(i == 0 ? "if" : "elif");
            visitBody(is.bodies.get(i));
            symbolTable.exitScope();
        }
        if (is.last != null) {
            symbolTable.enterScope("else");
            visitBody(is.last);
            symbolTable.exitScope();
        }
    }

    private void visitDecoratorStatement(DecoratorStatement ds) {
        if (ds.decorators != null)
            for (Decorator dec : ds.decorators)
                visitDecorator(dec);
        visitFunctionDef(ds.function);
    }

    private void visitDecorator(Decorator dec) {
        // only the first segment of `@app.route(...)` is an actual variable read;
        // `route` is an attribute name, not a separate identifier to resolve
        if (dec.dottedName != null && !dec.dottedName.isEmpty())
            resolveIdRead(dec.dottedName.get(0));
        if (dec.arguments != null)
            for (Argument arg : dec.arguments) {
                // `arg.arg` is the keyword NAME (e.g. `methods` in methods=['GET']),
                // not a variable read, whenever the argument is a keyword argument
                if (arg.isAssigned()) resolveCondition(arg.assign);
                else resolveCondition(arg.arg);
            }
    }

    private void visitFunctionDef(FunctionDef fd) {
        // parameter defaults/annotations are evaluated in the *enclosing* scope,
        // before the function's own scope (and its parameters) exist
        if (fd.parameters != null)
            for (Parameter param : fd.parameters) {
                if (param.type != null) resolveCondition(param.type);
                if (param.defaultValue != null) resolveCondition(param.defaultValue);
            }
        if (fd.returnType != null) resolveCondition(fd.returnType);

        Symbol funcSym = declare(fd.id.name, SymbolKind.FUNCTION, fd.getLine());
        bindings.put(fd.id, funcSym);
        funcSym.invalidateValue(); // functions aren't constant-value-tracked

        symbolTable.enterScope("function " + fd.id.name);
        if (fd.parameters != null)
            for (Parameter param : fd.parameters) {
                Symbol paramSym = declare(param.id.name, SymbolKind.PARAMETER, param.getLine());
                bindings.put(param.id, paramSym);
                paramSym.invalidateValue(); // caller-supplied — never statically known here
            }

        functionDepth++;
        int savedLoopDepth = loopDepth;
        loopDepth = 0;
        visitBody(fd.body);
        loopDepth = savedLoopDepth;
        functionDepth--;
        symbolTable.exitScope();
    }

    private void visitBody(Body body) {
        if (body == null || body.statements == null) return;
        for (Statement st : body.statements)
            visitStatement(st);
    }

    private void visitSimpleStatement(SimpleStatement ss) {
        for (SmallStatement sm : ss.smallStatementList)
            visitSmallStatement(sm);
    }

    private void visitSmallStatement(SmallStatement sm) {
        if (sm instanceof ExpressionStatement es) {
            resolveExpressionStatement(es);
        } else if (sm instanceof AugAssignStatement aas) {
            Symbol sym = declare(aas.id.name, SymbolKind.VARIABLE, aas.getLine());
            bindings.put(aas.id, sym);
            sym.addUsage(aas.getLine());
            sym.invalidateValue(); // `x += e` depends on x's prior runtime value
            resolveExpression(aas.expression);
        } else if (sm instanceof ReturnStatement rs) {
            if (rs.conditions != null)
                for (Condition c : rs.conditions) resolveCondition(c);
        } else if (sm instanceof ImportStatement is) {
            visitImportStatement(is);
        } else if (sm instanceof GlobalStatement gs) {
            for (ID id : gs.names) symbolTable.declareGlobal(id.name);
        }
        // Pass/Break/Continue: nothing to resolve
    }

    private void visitImportStatement(ImportStatement is) {
        if (is instanceof FromImportStatement fis) {
            if (fis.targets != null)
                for (ID id : fis.targets) {
                    Symbol sym = declare(id.name, SymbolKind.VARIABLE, id.getLine());
                    bindings.put(id, sym);
                    sym.invalidateValue(); // value lives in another module
                }
        } else if (is instanceof SimpleImportStatement sis) {
            ID last = sis.dottedName.get(sis.dottedName.size() - 1);
            Symbol sym = declare(last.name, SymbolKind.VARIABLE, last.getLine());
            bindings.put(last, sym);
            sym.invalidateValue();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // ASSIGNMENT + CONSTANT PROPAGATION
    // ─────────────────────────────────────────────────────────────

    private void resolveExpressionStatement(ExpressionStatement es) {
        if (es.haveEquals == Operation.EQUALS) {
            for (Condition rhs : es.assigns) resolveCondition(rhs);
            for (int i = 0; i < es.conditions.size(); i++) {
                Condition value = i < es.assigns.size() ? es.assigns.get(i) : null;
                assignTarget(es.conditions.get(i), value);
            }
        } else {
            for (Condition cd : es.conditions) resolveCondition(cd);
        }
    }

    private void assignTarget(Condition target, Condition value) {
        if (!(target instanceof IDTrailer idt)) return;

        if (idt.trailers == null || idt.trailers.isEmpty()) {
            // simple `name = value`
            Symbol sym = declare(idt.id.name, SymbolKind.VARIABLE, idt.getLine());
            bindings.put(idt.id, sym);
            ConstantValue cv = value != null ? resolver.PythonLiteralEvaluator.evaluate(value) : ConstantValue.unknown();
            if (cv.isKnown()) sym.setValue(cv);
            else sym.invalidateValue();
        } else {
            // attribute/subscript/call target, e.g. `app.secret_key = ...`, `d[k] = v`
            resolveIdTrailer(idt, true);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS  (identifier-read resolution)
    // ─────────────────────────────────────────────────────────────

    private void resolveCondition(Condition cond) {
        if (cond == null) return;
        if (cond instanceof CompoundCondition cc) {
            resolveCondition(cc.first);
            if (cc.second != null) resolveCondition(cc.second);
        } else if (cond instanceof RelationalComparison rc) {
            resolveExpression(rc.left);
            resolveExpression(rc.right);
        } else if (cond instanceof Expression ex) {
            resolveExpression(ex);
        }
    }

    private void resolveExpression(Expression ex) {
        if (ex == null) return;
        if (ex instanceof BinaryExpression be) {
            resolveExpression(be.left);
            resolveExpression(be.right);
        } else if (ex instanceof UnaryExpression ue) {
            resolveExpression(ue.expression);
        } else if (ex instanceof IDTrailer idt) {
            resolveIdTrailer(idt, false);
        } else if (ex instanceof ID id) {
            resolveIdRead(id); // defensive: this grammar alternative is effectively unreachable
        } else if (ex instanceof ParenAtom pa) {
            resolveExpression(pa.inner);
        } else if (ex instanceof python.models.atom_statement.List la) {
            if (la.content != null) for (Expression e : la.content) resolveExpression(e);
        } else if (ex instanceof Set se) {
            if (se.content != null) for (Expression e : se.content) resolveExpression(e);
        } else if (ex instanceof Dictionary dict) {
            if (dict.keys != null) for (Expression e : dict.keys) resolveExpression(e);
            if (dict.values != null) for (Expression e : dict.values) resolveExpression(e);
        }
        // IntegerAtom/FloatAtom/BoolAtom/StringAtom/None: pure literals, nothing to resolve
    }

    private void resolveIdTrailer(IDTrailer idt, boolean isAssignTarget) {
        Symbol sym = symbolTable.resolve(idt.id.name);
        boolean bareTarget = isAssignTarget && (idt.trailers == null || idt.trailers.isEmpty());

        if (sym == null) {
            if (!bareTarget)
                errors.add(new CompilerError(CompilerError.Kind.UNDEFINED_VARIABLE,
                        "Undefined variable '" + idt.id.name + "'", idt.getLine()));
        } else {
            bindings.put(idt.id, sym);
            sym.addUsage(idt.getLine());

            boolean mutatesViaCall = idt.trailers != null && idt.trailers.stream()
                    .anyMatch(t -> t.arguments instanceof CallArguments);
            boolean mutatesViaTarget = isAssignTarget && idt.trailers != null && !idt.trailers.isEmpty();
            if (mutatesViaCall || mutatesViaTarget)
                sym.invalidateValue();
        }

        if (idt.trailers != null)
            for (Trailer tr : idt.trailers)
                resolveTrailerArguments(tr);
    }

    private void resolveTrailerArguments(Trailer tr) {
        if (tr.arguments instanceof CallArguments ca) {
            if (ca.args != null)
                for (Argument arg : ca.args) {
                    // same rule as visitDecorator: a keyword argument's `arg.arg` is
                    // its parameter name, not a value to resolve as a variable read
                    if (arg.isAssigned()) resolveCondition(arg.assign);
                    else resolveCondition(arg.arg);
                }
        } else if (tr.arguments instanceof SubscriptArguments sa) {
            if (sa.conditions != null)
                for (Condition c : sa.conditions) resolveCondition(c);
        }
    }

    private void resolveIdRead(ID id) {
        Symbol sym = symbolTable.resolve(id.name);
        if (sym == null) {
            errors.add(new CompilerError(CompilerError.Kind.UNDEFINED_VARIABLE,
                    "Undefined variable '" + id.name + "'", id.getLine()));
        } else {
            bindings.put(id, sym);
            sym.addUsage(id.getLine());
        }
    }

    private Symbol declare(String name, SymbolKind kind, int line) {
        return symbolTable.defineOrGet(new Symbol(name, kind, line));
    }
}
