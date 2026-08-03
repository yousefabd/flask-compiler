package python.symbol_table;

import python.models.root.CompoundStatement;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.atom_statement.*; // added: literals are the basis of type inference
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.GlobalStatement; // added: handle `global x, y` statements
import python.models.small_statement.SmallStatement;

import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.compound_statement.*;
import python.models.enums.Operation;
import python.models.expr_statement.*;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.SubscriptArguments;
import python.models.trailer.Trailer;
import python.symbol_table.semantic_rules.ISemanticRule;
import python.symbol_table.semantic_rules.SemanticContext;

import java.util.ArrayList;
import java.util.List;


/**
 * Builds the Python symbol table and reports the semantic errors that can be
 * decided from declarations and name resolution alone.
 *
 * <p>Runs in the same three phases the Jinja2 builder uses:</p>
 * <ol>
 *   <li><b>declare</b> — walk the AST, open/close scopes, declare every name and
 *       remember every name that is <i>read</i> (see {@code PendingReference});</li>
 *   <li><b>resolve</b> — once the whole table exists, bind each recorded read to
 *       its declaration and report UndefinedError / ScopeError / NameError;</li>
 *   <li><b>rules</b> — run the {@link ISemanticRule}s (type checking, Flask rules)
 *       against the finished table.</li>
 * </ol>
 *
 * <p>Reads are resolved in phase 2 rather than inline, because "used before it was
 * declared" can only be distinguished from "declared in an enclosing scope further
 * down the file" after the entire module has been seen.</p>
 */
public class SymbolTableBuilder {

    private final SymbolTable symbolTable;
    // added: semantic errors are collected here instead of crashing/ignoring,
    // mirroring how the jinja2 SymbolTableBuilder reports CompilerErrors
    private final java.util.List<CompilerError> errors;
    // added: the Jinja2 builder's rule pipeline, mirrored for Python
    private final List<ISemanticRule> semanticRules;
    // added: every identifier read during the walk, resolved after the walk finishes
    private final List<PendingReference> references = new ArrayList<>();
    private int loopDepth = 0;      // > 0 while visiting a for/while body
    private int functionDepth = 0;  // > 0 while visiting a function body

    /** One identifier read, plus the scope it was read in. Resolved by {@link #resolveReferences()}. */
    private record PendingReference(ID identifier, Scope scope) {}

    public SymbolTableBuilder(SymbolTable symbolTable) {
        this(symbolTable, new java.util.ArrayList<>());
    }

    public SymbolTableBuilder(SymbolTable symbolTable, java.util.List<CompilerError> errors) {
        this(symbolTable, errors, List.of());
    }

    public SymbolTableBuilder(SymbolTable symbolTable,
                              java.util.List<CompilerError> errors,
                              List<ISemanticRule> semanticRules) {
        this.symbolTable = symbolTable;
        this.errors = errors;
        this.semanticRules = semanticRules;
    }

    public void build(Program program) {
        visitProgram(program);

        // added: phase 2 — every declaration is now known, so reads can be bound
        resolveReferences();

        // added: phase 3 — same rule pipeline the Jinja2 builder runs
        SemanticContext semanticContext =
                new SemanticContext(program, symbolTable, errors);

        for (ISemanticRule rule : semanticRules)
            rule.validate(semanticContext);
    }

    private void visitProgram(Program program) {
        for (Statement st : program.statements) {
            visitStatement(st);
        }
    }

    private void visitStatement(Statement st) {
        if (st instanceof CompoundStatement cs) {

            visitCompoundStatement(cs);
        } else if (st instanceof SimpleStatement ss) {
            visitSimpleStatement(ss);

        }
    }

    private void visitCompoundStatement(CompoundStatement cs) {
        if(cs instanceof WhileStatement ws)
            visitWhileStatement(ws);
        else if (cs instanceof ForStatement fs)
            visitForStatement(fs);
        else if (cs instanceof IfStatement is)
            visitIfStatement(is);
        else if (cs instanceof DecoratorStatement ds)
            visitDecoratorStatement(ds);
    }

    private void visitWhileStatement(WhileStatement ws)
    {
        // added: the condition is evaluated in the enclosing scope, before the body opens one
        visitCondition(ws.condition);

        symbolTable.enterScope("while");
        loopDepth++;
        visitBody(ws.body);
        loopDepth--;
        symbolTable.exitScope();
        if(ws.last != null) {
            symbolTable.enterScope("else");
            visitBody(ws.last);
            symbolTable.exitScope();
        }
    }

    private void visitForStatement(ForStatement fs)
    {
        // added: `for x in items` — `items` belongs to the OUTER scope, so it has to be
        // read before the loop scope is opened (same order as the Jinja2 for-statement).
        visitCondition(fs.iterable);

        symbolTable.enterScope("for");
        fs.iterators.forEach(id -> {
            Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE, id.getLine());
            symbolTable.define(sym);
        });
        loopDepth++;
        visitBody(fs.body);
        loopDepth--;
        symbolTable.exitScope();
        if(fs.last != null)
        {
            symbolTable.enterScope("else");
            visitBody(fs.last);
            symbolTable.exitScope();
        }
    }

    private void visitIfStatement(IfStatement is)
    {
        // added: each branch condition is evaluated in the enclosing scope
        if (!is.conditions.isEmpty())
            visitCondition(is.conditions.get(0));

        symbolTable.enterScope("if");

        visitBody(is.bodies.get(0));

        symbolTable.exitScope();
        for (int i = 1; i < is.bodies.size(); i++) {
            if (i < is.conditions.size())
                visitCondition(is.conditions.get(i));
            symbolTable.enterScope("elif");
            visitBody(is.bodies.get(i));
            symbolTable.exitScope();
        }
        if(is.last != null)
        {
            symbolTable.enterScope("else");
            visitBody(is.last);
            symbolTable.exitScope();
        }
    }

    private void visitDecoratorStatement(DecoratorStatement ds)
    {
        // added: `@app.route('/x')` reads `app` and evaluates the decorator arguments
        // in the scope the decorated function is being defined in.
        if (ds.decorators != null)
            for (Decorator decorator : ds.decorators)
                visitDecorator(decorator);

        visitFunctionDef(ds.function);
    }

    private void visitDecorator(Decorator decorator) {
        // Only the root of a dotted name is a variable; `route` in `app.route` is an
        // attribute, exactly like the Jinja2 builder treats PropertyAccessNode.
        if (decorator.dottedName != null && !decorator.dottedName.isEmpty())
            reference(decorator.dottedName.get(0));

        if (decorator.arguments != null)
            for (Argument argument : decorator.arguments)
                visitArgument(argument);
    }

    private void visitSimpleStatement(SimpleStatement ss) {
        for(SmallStatement sm : ss.smallStatementList)
        {
            visitSmallStatement(sm);
        }
    }

    private void visitBody(Body body)
    {
        for(Statement st : body.statements)
        {
            visitStatement(st);

        }
    }

    private void visitFunctionDef(FunctionDef fd)
    {
        Symbol funcSym = new Symbol(fd.id.name, fd.getLine(), fd);
        // added: a function name may not be defined twice in the same scope
        if (!this.symbolTable.define(funcSym)) {
            error(CompilerError.Kind.DUPLICATE_FUNCTION,
                    "Function '" + fd.id.name + "' is already defined in this scope",
                    fd.getLine(), fd.id.name);
        }

        // added: annotations and default values are evaluated in the DEFINING scope,
        // before the function scope opens (mirrors the Jinja2 macro handling).
        for (Parameter param : fd.parameters) {
            if (param.type != null)         visitCondition(param.type);
            if (param.defaultValue != null) visitCondition(param.defaultValue);
        }
        if (fd.returnType != null) visitCondition(fd.returnType);

        this.symbolTable.enterScope("function " + fd.id.name);

        for (Parameter param : fd.parameters) {
            Symbol paramSym = new Symbol(
                    param.id.name,
                    SymbolKind.PARAMETER,
                    param.getLine(),
                    annotatedType(param),
                    param.defaultValue);
            // added: duplicate parameter names are a semantic error
            if (!this.symbolTable.define(paramSym)) {
                error(CompilerError.Kind.DUPLICATE_PARAMETER,
                        "Duplicate parameter '" + param.id.name
                                + "' in function '" + fd.id.name + "'",
                        param.getLine(), param.id.name);
            }
        }
        functionDepth++;
        // break/continue may not leak across a function boundary
        int savedLoopDepth = loopDepth;
        loopDepth = 0;
        visitBody(fd.body);
        loopDepth = savedLoopDepth;
        functionDepth--;
        this.symbolTable.exitScope();
    }

    /** {@code def f(a: int)} → INT; unannotated or unrecognized annotations stay ANY. */
    public static SymbolType annotatedType(Parameter parameter) {
        if (parameter.type == null) return SymbolType.ANY;
        if (parameter.type instanceof IDTrailer idTrailer
                && (idTrailer.trailers == null || idTrailer.trailers.isEmpty()))
            return SymbolType.fromAnnotation(idTrailer.id.name);
        if (parameter.type instanceof ID id)
            return SymbolType.fromAnnotation(id.name);
        return SymbolType.ANY;
    }

    private void visitSmallStatement(SmallStatement sm) {
        if (sm instanceof AugAssignStatement aas) {
            visitAugAssignStatement(aas);
        } else if (sm instanceof ImportStatement is) {
            visitImportStatement(is);
        } else if (sm instanceof ExpressionStatement es) {
            visitExpressionStatement(es);

        } else if (sm instanceof python.models.small_statement.ReturnStatement rs) {
            // added: `return` only makes sense inside a function body
            if (functionDepth == 0)
                error(CompilerError.Kind.RETURN_OUTSIDE_FUNCTION,
                        "'return' outside of a function", rs.getLine(), null);
            // added: the returned expressions are ordinary reads
            if (rs.conditions != null)
                for (Condition condition : rs.conditions)
                    visitCondition(condition);
        } else if (sm instanceof python.models.small_statement.BreakStatement bs) {
            if (loopDepth == 0)
                error(CompilerError.Kind.BREAK_OUTSIDE_LOOP,
                        "'break' outside of a loop", bs.getLine(), null);
        } else if (sm instanceof python.models.small_statement.ContinueStatement cs) {
            if (loopDepth == 0)
                error(CompilerError.Kind.CONTINUE_OUTSIDE_LOOP,
                        "'continue' outside of a loop", cs.getLine(), null);
        } else if (sm instanceof GlobalStatement gs) {
            // added: `global` at module level is meaningless — flag it
            if (functionDepth == 0)
                error(CompilerError.Kind.GLOBAL_AT_MODULE_LEVEL,
                        "'global' declaration at module level has no effect",
                        gs.getLine(), null);
            // added: `global x, y` - mark each name as global in the current scope so that
            // any later assignment to it (handled above/in visitExpressionStatement) is
            // defined in the module/global scope instead of this local scope
            for (ID id : gs.names)
                symbolTable.declareGlobal(id.name);
        }
    }

    // added: `x += 1` READS x before writing it, so an unknown name here is an error
    // immediately. This one is decided during the walk instead of in resolveReferences()
    // because augmented assignment is exactly the case where the read must already have
    // happened textually earlier — a later declaration cannot rescue it.
    private void visitAugAssignStatement(AugAssignStatement aas) {
        visitCondition(aas.expression);

        Symbol target = symbolTable.resolve(aas.id.name);

        if (target == null) {
            Symbol declaredElsewhere = symbolTable.resolveAnywhere(aas.id.name);

            error(declaredElsewhere != null
                            ? CompilerError.Kind.SCOPE
                            : CompilerError.Kind.UNDEFINED_VARIABLE,
                    declaredElsewhere != null
                            ? "Variable '" + aas.id.name + "' is out of scope here"
                            : "Variable '" + aas.id.name + "' is not defined",
                    aas.getLine(), aas.id.name);

            // recover so the rest of the function does not cascade into more errors
            symbolTable.define(new Symbol(aas.id.name, SymbolKind.VARIABLE, aas.getLine()));
            return;
        }

        symbolTable.recordBinding(aas.id, target);
        target.addUsage(aas.getLine());
    }

    private void visitImportStatement(ImportStatement is) {
        if(is instanceof FromImportStatement fis)
        {
            if (fis.targets == null) return;   // `from x import *` binds nothing we can name
            fis.targets.forEach(id -> {
                Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE, is.getLine());
                symbolTable.define(sym);
            });
        }
        else if (is instanceof SimpleImportStatement sis)
        {
            String name = sis.dottedName.get(sis.dottedName.size() - 1).name;
            Symbol sym = new Symbol(name, SymbolKind.VARIABLE, is.getLine());
            symbolTable.define(sym);
        }
    }

    private void visitExpressionStatement(ExpressionStatement exs)
    {
        if(exs.haveEquals == Operation.EQUALS) {
            /*
             * Analyze the right side before declaring the targets, so that
             * `total = total + 1` reads the PREVIOUS total instead of resolving
             * against the variable currently being declared. Same ordering rule
             * the Jinja2 builder applies to {% set %}.
             */
            if (exs.assigns != null)
                for (Condition assigned : exs.assigns)
                    visitCondition(assigned);

            for (int i = 0; i < exs.conditions.size(); i++) {
                Condition target = exs.conditions.get(i);

                Condition assignedValue =
                        exs.assigns != null
                                && exs.assigns.size() == exs.conditions.size()
                                ? exs.assigns.get(i)
                                : null;

                declareAssignmentTarget(target, assignedValue);
            }
            return;
        }

        // A bare expression statement — `print(x)`, `items.append(1)` — is all reads.
        for (Condition cd : exs.conditions)
            visitCondition(cd);
    }

    /**
     * Handles one left-hand side of an assignment.
     *
     * <p>A plain name declares a variable. Anything else — {@code app.secret_key = ...},
     * {@code items[0] = ...} — is not a declaration, it only <i>reads</i> the object
     * being mutated.</p>
     */
    private void declareAssignmentTarget(Condition target, Condition assignedValue) {
        ID name = plainTargetName(target);

        if (name == null) {
            visitCondition(target);   // attribute/index target: the base object is still read
            return;
        }

        declareVariable(name, assignedValue);
    }

    /** The ID of a bare-name target, or null when the target is not a bare name. */
    private static ID plainTargetName(Condition target) {
        if (target instanceof IDTrailer idt
                && (idt.trailers == null || idt.trailers.isEmpty()))
            return idt.id;
        if (target instanceof ID id)
            return id;
        return null;
    }

    /**
     * Declares (or re-declares) a variable in the current scope.
     *
     * <p>Re-declaring a name that already exists in the same scope is reported as</p>
     * <ul>
     *   <li>{@code TypeMismatchError} when the new value has a different, statically
     *       known type — {@code x = 10} then {@code x = "text"};</li>
     *   <li>{@code DuplicateDeclarationError} otherwise — {@code x = 1} then {@code x = 2}.</li>
     * </ul>
     *
     * <p>Assignments that are legitimate re-bindings rather than new declarations are
     * exempt: names pulled in with {@code global}, parameters reassigned inside their
     * own function, and shadowed builtins.</p>
     */
    private void declareVariable(ID target, Condition assignedValue) {
        String name = target.name;
        SymbolType type = inferType(assignedValue);

        Scope current = symbolTable.getCurrentScope();
        boolean redirectedToGlobal =
                current != symbolTable.getGlobalScope() && current.isGlobal(name);

        Scope owningScope = redirectedToGlobal ? symbolTable.getGlobalScope() : current;
        Symbol existing = owningScope.resolveLocal(name);

        if (existing == null) {
            symbolTable.define(new Symbol(name, SymbolKind.VARIABLE,
                    target.getLine(), type, assignedValue));
            return;
        }

        // `global x` followed by `x = ...` updates the module variable — not a redeclaration.
        if (redirectedToGlobal) {
            existing.addUsage(target.getLine());
            return;
        }

        // Rebinding a parameter or shadowing a builtin is normal Python.
        if (existing.getKind() == SymbolKind.PARAMETER
                || existing.getKind() == SymbolKind.BUILTIN) {
            existing.addUsage(target.getLine());
            return;
        }

        if (conflictingTypes(existing.getType(), type)) {
            error(CompilerError.Kind.TYPE_MISMATCH,
                    "Expected " + existing.getType() + ", got " + type
                            + " for '" + name + "' (declared at line "
                            + existing.getDeclarationLine() + ")",
                    target.getLine(), name);
            return;
        }

        error(CompilerError.Kind.DUPLICATE_VARIABLE,
                "Variable '" + name + "' is already declared in this scope at line "
                        + existing.getDeclarationLine(),
                target.getLine(), name);
    }

    /** Two statically known types that cannot describe the same variable. int/float/bool mix freely. */
    private static boolean conflictingTypes(SymbolType declared, SymbolType assigned) {
        if (!declared.isKnown() || !assigned.isKnown()) return false;
        if (declared == assigned) return false;
        if (declared.isNumeric() && assigned.isNumeric()) return false;
        // Assigning None is how Python clears a variable — never a mismatch.
        return assigned != SymbolType.NONE && declared != SymbolType.NONE;
    }

    // ─────────────────────────────────────────────────────────────
    // EXPRESSIONS — record every name that is read
    // ─────────────────────────────────────────────────────────────

    private void visitCondition(Condition condition) {
        if (condition == null) return;

        if (condition instanceof CompoundCondition cc) {
            visitCondition(cc.first);
            visitCondition(cc.second);
        }
        else if (condition instanceof RelationalComparison rc) {
            visitCondition(rc.left);
            visitCondition(rc.right);
        }
        else if (condition instanceof BinaryExpression be) {
            visitCondition(be.left);
            visitCondition(be.right);
        }
        else if (condition instanceof UnaryExpression ue) {
            visitCondition(ue.expression);
        }
        else if (condition instanceof IDTrailer idt) {
            visitIdTrailer(idt);
        }
        else if (condition instanceof ParenAtom pa) {
            visitCondition(pa.inner);
        }
        else if (condition instanceof python.models.atom_statement.List list) {
            for (Expression item : list.content) visitCondition(item);
        }
        else if (condition instanceof python.models.atom_statement.Set set) {
            for (Expression item : set.content) visitCondition(item);
        }
        else if (condition instanceof Dictionary dict) {
            for (Expression key : dict.keys)     visitCondition(key);
            for (Expression value : dict.values) visitCondition(value);
        }
        else if (condition instanceof ID id) {
            reference(id);
        }
        // literals (IntegerAtom, FloatAtom, StringAtom, BoolAtom, None) read nothing
    }

    private void visitIdTrailer(IDTrailer idTrailer) {
        reference(idTrailer.id);

        if (idTrailer.trailers == null) return;

        for (Trailer trailer : idTrailer.trailers) {
            // `.name` is an attribute, not a variable — only the root object is resolved,
            // exactly like the Jinja2 builder treats PropertyAccessNode.
            if (trailer.arguments instanceof CallArguments callArguments) {
                if (callArguments.args != null)
                    for (Argument argument : callArguments.args)
                        visitArgument(argument);
            }
            else if (trailer.arguments instanceof SubscriptArguments subscript) {
                if (subscript.conditions != null)
                    for (Condition index : subscript.conditions)
                        visitCondition(index);
            }
        }
    }

    /**
     * {@code f(value)} reads {@code value}; {@code f(page='home')} reads only
     * {@code 'home'} — {@code page} is the parameter's name, not a variable.
     */
    private void visitArgument(Argument argument) {
        if (argument == null) return;

        if (argument.isAssigned()) {
            visitCondition(argument.assign);
            return;
        }

        visitCondition(argument.arg);
    }

    private void reference(ID identifier) {
        if (identifier == null) return;
        references.add(new PendingReference(identifier, symbolTable.getCurrentScope()));
    }

    // ─────────────────────────────────────────────────────────────
    // PHASE 2 — resolve every recorded read
    // ─────────────────────────────────────────────────────────────

    /**
     * Binds each recorded identifier to its declaration and reports the three
     * name-resolution errors:
     *
     * <ul>
     *   <li><b>UndefinedError</b> — the name exists in no scope at all;</li>
     *   <li><b>ScopeError</b> — the name exists, but not on this scope chain;</li>
     *   <li><b>NameError</b> — the name is declared in this very scope, further down.</li>
     * </ul>
     */
    private void resolveReferences() {
        for (PendingReference reference : references) {
            ID identifier = reference.identifier();
            String name = identifier.name;
            int line = identifier.getLine();
            String context = reference.scope().getQualifiedName();

            Scope owner = reference.scope().findOwner(name);

            if (owner != null) {
                Symbol declaration = owner.resolveLocal(name);

                symbolTable.recordBinding(identifier, declaration);
                declaration.addUsage(line);

                /*
                 * Only a declaration in the SAME scope can be "used before declared".
                 * A function body referring to a module-level name defined later in the
                 * file is legal Python — the body does not run until it is called.
                 */
                if (owner == reference.scope()
                        && declaration.getDeclarationLine() > line) {
                    errors.add(new CompilerError(
                            CompilerError.Kind.USE_BEFORE_DECLARATION,
                            "Variable '" + name + "' is used before it is declared at line "
                                    + declaration.getDeclarationLine(),
                            line, context, name));
                }

                continue;
            }

            Symbol declaredElsewhere = symbolTable.resolveAnywhere(name);

            if (declaredElsewhere != null) {
                errors.add(new CompilerError(
                        CompilerError.Kind.SCOPE,
                        "Variable '" + name + "' is out of scope here (declared at line "
                                + declaredElsewhere.getDeclarationLine() + ")",
                        line, context, name));
            } else {
                errors.add(new CompilerError(
                        CompilerError.Kind.UNDEFINED_VARIABLE,
                        "Variable '" + name + "' is not defined",
                        line, context, name));
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TYPE INFERENCE  (shallow — mirrors jinja2 Symbol.inferType)
    // ─────────────────────────────────────────────────────────────

    /**
     * Best-effort static type of an expression. Returns ANY whenever the type can
     * only be known at run time, so no checker ever has to guess.
     */
    private SymbolType inferType(Condition expression) {
        if (expression == null)                        return SymbolType.ANY;
        if (expression instanceof IntegerAtom)         return SymbolType.INT;
        if (expression instanceof FloatAtom)           return SymbolType.FLOAT;
        if (expression instanceof StringAtom)          return SymbolType.STRING;
        if (expression instanceof BoolAtom)            return SymbolType.BOOLEAN;
        if (expression instanceof None)                return SymbolType.NONE;
        if (expression instanceof python.models.atom_statement.List) return SymbolType.LIST;
        if (expression instanceof Dictionary)          return SymbolType.DICT;
        if (expression instanceof python.models.atom_statement.Set)  return SymbolType.SET;
        if (expression instanceof ParenAtom pa)        return inferType(pa.inner);
        if (expression instanceof RelationalComparison) return SymbolType.BOOLEAN;
        if (expression instanceof CompoundCondition)   return SymbolType.BOOLEAN;

        if (expression instanceof UnaryExpression ue) {
            SymbolType operand = inferType(ue.expression);
            return operand.isNumeric() ? operand : SymbolType.ANY;
        }

        if (expression instanceof BinaryExpression be) {
            return binaryResultType(
                    inferType(be.left), inferType(be.right), be.operation);
        }

        if (expression instanceof IDTrailer idt
                && (idt.trailers == null || idt.trailers.isEmpty())) {
            // A plain name copies the type of whatever it currently refers to.
            Symbol symbol = symbolTable.resolve(idt.id.name);
            return symbol == null ? SymbolType.ANY : symbol.getType();
        }

        if (expression instanceof ID id) {
            Symbol symbol = symbolTable.resolve(id.name);
            return symbol == null ? SymbolType.ANY : symbol.getType();
        }

        // calls, attribute access, subscripts — value only known at run time
        return SymbolType.ANY;
    }

    /**
     * Result type of a binary operation. ANY both when an operand is unknown and
     * when the combination is invalid — the invalid case is reported by
     * {@code TypeCheckerRule}, and returning ANY stops one mistake from cascading.
     */
    public static SymbolType binaryResultType(SymbolType left, SymbolType right, Operation op) {
        if (!left.isKnown() || !right.isKnown()) return SymbolType.ANY;

        return switch (op) {
            case ADD -> {
                if (left.isNumeric() && right.isNumeric()) yield widen(left, right);
                if (left == SymbolType.STRING && right == SymbolType.STRING) yield SymbolType.STRING;
                if (left == SymbolType.LIST && right == SymbolType.LIST) yield SymbolType.LIST;
                yield SymbolType.ANY;
            }
            case SUB, MOD, POWER, IDIV -> {
                if (left.isNumeric() && right.isNumeric()) yield widen(left, right);
                yield SymbolType.ANY;
            }
            case DIV -> {
                if (left.isNumeric() && right.isNumeric()) yield SymbolType.FLOAT;
                yield SymbolType.ANY;
            }
            case MULT -> {
                if (left.isNumeric() && right.isNumeric()) yield widen(left, right);
                if (left == SymbolType.STRING && right.isNumeric()) yield SymbolType.STRING;
                if (left.isNumeric() && right == SymbolType.STRING) yield SymbolType.STRING;
                if (left == SymbolType.LIST && right.isNumeric()) yield SymbolType.LIST;
                yield SymbolType.ANY;
            }
            case AND, OR, XOR, LSHIFT, RSHIFT -> {
                if (left.isNumeric() && right.isNumeric()) yield SymbolType.INT;
                yield SymbolType.ANY;
            }
            default -> SymbolType.ANY;
        };
    }

    private static SymbolType widen(SymbolType left, SymbolType right) {
        if (left == SymbolType.FLOAT || right == SymbolType.FLOAT) return SymbolType.FLOAT;
        return SymbolType.INT;
    }

    // ─────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────

    private void error(CompilerError.Kind kind, String message, int line, String symbolName) {
        errors.add(new CompilerError(kind, message, line,
                symbolTable.getCurrentScope().getQualifiedName(), symbolName));
    }
}
