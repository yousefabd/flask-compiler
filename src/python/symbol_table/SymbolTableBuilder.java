package python.symbol_table;

import python.models.root.CompoundStatement;
import python.models.root.Program;
import python.models.root.SimpleStatement;
import python.models.root.Statement;
import python.models.atom_statement.ID; // added: used to read names out of GlobalStatement
import python.models.small_statement.AugAssignStatement;
import python.models.small_statement.GlobalStatement; // added: handle `global x, y` statements
import python.models.small_statement.SmallStatement;

import python.models.Import_statement.FromImportStatement;
import python.models.Import_statement.ImportStatement;
import python.models.Import_statement.SimpleImportStatement;
import python.models.compound_statement.*;
import python.models.enums.Operation;
import python.models.expr_statement.Condition;
import python.models.expr_statement.ExpressionStatement;
import python.models.expr_statement.IDTrailer;
import python.models.funcdef.FunctionDef;
import python.models.funcdef.Parameter;


public class SymbolTableBuilder {

    private final SymbolTable symbolTable;
    // added: semantic errors are collected here instead of crashing/ignoring,
    // mirroring how the jinja2 SymbolTableBuilder reports CompilerErrors
    private final java.util.List<CompilerError> errors;
    private int loopDepth = 0;      // > 0 while visiting a for/while body
    private int functionDepth = 0;  // > 0 while visiting a function body

    public SymbolTableBuilder(SymbolTable symbolTable) {
        this(symbolTable, new java.util.ArrayList<>());
    }

    public SymbolTableBuilder(SymbolTable symbolTable, java.util.List<CompilerError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void build(Program program) {
        visitProgram(program);
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

    // fixed: if/for/while do not open a Python scope — a name assigned inside
    // one stays visible in the surrounding function/module scope. This table's
    // result is unused by the pipeline (python.semantic.NameResolver owns real
    // scoping now), but enterScope/exitScope here previously modeled Python
    // incorrectly, which is worth not doing even in dead code.
    private void visitWhileStatement(WhileStatement ws)
    {
        loopDepth++;
        visitBody(ws.body);
        loopDepth--;
        if(ws.last != null) {
            visitBody(ws.last);
        }
    }

    private void visitForStatement(ForStatement fs)
    {
        fs.iterators.forEach(id -> {
            Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE);
            symbolTable.define(sym);
        });
        loopDepth++;
        visitBody(fs.body);
        loopDepth--;
        if(fs.last != null)
        {
            visitBody(fs.last);
        }
    }

    private void visitIfStatement(IfStatement is)
    {
        visitBody(is.bodies.get(0));

        for (int i = 1; i < is.bodies.size(); i++) {
            visitBody(is.bodies.get(i));
        }
        if(is.last != null)
        {
            visitBody(is.last);
        }
    }

    private void visitDecoratorStatement(DecoratorStatement ds) 
    {
        visitFunctionDef(ds.function);
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
        // fixed: redefining a function is ordinary, legal Python name rebinding
        // (the second `def` simply replaces the first at runtime) — CPython
        // raises nothing for it, so this is no longer reported as an error.
        // `define()` is still called so the name occupies this scope for the
        // duplicate-parameter check below; its boolean result is unused.
        Symbol funcSym = new Symbol(fd.id.name, SymbolKind.FUNCTION);
        this.symbolTable.define(funcSym);

        this.symbolTable.enterScope("function " + fd.id.name);

        for (Parameter param : fd.parameters) {
            Symbol paramSym = new Symbol(param.id.name, SymbolKind.PARAMETER);
            // added: duplicate parameter names are a semantic error
            if (!this.symbolTable.define(paramSym)) {
                errors.add(new CompilerError(
                        CompilerError.Kind.DUPLICATE_PARAMETER,
                        "Duplicate parameter '" + param.id.name
                                + "' in function '" + fd.id.name + "'",
                        param.getLine()));
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

    private void visitSmallStatement(SmallStatement sm) {
        if (sm instanceof AugAssignStatement aas) {
            Symbol sym = new Symbol(aas.id.name, SymbolKind.VARIABLE);
            symbolTable.define(sym);
        } else if (sm instanceof ImportStatement is) {
            visitImportStatement(is);
        } else if (sm instanceof ExpressionStatement es) {
            visitExpressionStatement(es);

        } else if (sm instanceof python.models.small_statement.ReturnStatement rs) {
            // added: `return` only makes sense inside a function body
            if (functionDepth == 0)
                errors.add(new CompilerError(
                        CompilerError.Kind.RETURN_OUTSIDE_FUNCTION,
                        "'return' outside of a function", rs.getLine()));
        } else if (sm instanceof python.models.small_statement.BreakStatement bs) {
            if (loopDepth == 0)
                errors.add(new CompilerError(
                        CompilerError.Kind.BREAK_OUTSIDE_LOOP,
                        "'break' outside of a loop", bs.getLine()));
        } else if (sm instanceof python.models.small_statement.ContinueStatement cs) {
            if (loopDepth == 0)
                errors.add(new CompilerError(
                        CompilerError.Kind.CONTINUE_OUTSIDE_LOOP,
                        "'continue' outside of a loop", cs.getLine()));
        } else if (sm instanceof GlobalStatement gs) {
            // fixed: `global` at module level compiles and runs fine in CPython
            // (the module namespace already is the global namespace) — it is
            // redundant, not an error, so this no longer reports one.
            // `global x, y` - mark each name as global in the current scope so that
            // any later assignment to it (handled above/in visitExpressionStatement) is
            // defined in the module/global scope instead of this local scope
            for (ID id : gs.names)
                symbolTable.declareGlobal(id.name);
        }
    }

    private void visitImportStatement(ImportStatement is) {
        if(is instanceof FromImportStatement fis)
        {
            // fixed: `from math import *` parses with targets == null (see
            // PythonVisitor.visitFromImport) — this used to NullPointerException.
            if (fis.targets != null) {
                fis.targets.forEach(id -> {
                    Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE);
                    symbolTable.define(sym);
                });
            }
        }
        else if (is instanceof SimpleImportStatement sis)
        {
            // fixed: `import os.path` binds the name `os`, not `path` — Python
            // only binds the first component of a dotted import.
            String name = sis.dottedName.get(0).name;
            Symbol sym = new Symbol(name, SymbolKind.VARIABLE);
            symbolTable.define(sym);
        }
    }

    private void visitExpressionStatement(ExpressionStatement exs)
    {
        if(exs.haveEquals == Operation.EQUALS) {
            for(Condition cd : exs.conditions) {
                if(cd instanceof IDTrailer idt) {
                    if(idt.trailers == null || idt.trailers.size() == 0)
                    {
                        Symbol sym = new Symbol(idt.id.name, SymbolKind.VARIABLE);
                        symbolTable.define(sym);
                    }
                }
            }
        }
    }
}