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

    public SymbolTableBuilder(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
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

    private void visitWhileStatement(WhileStatement ws) 
    {
        symbolTable.enterScope("while");
        visitBody(ws.body);
        symbolTable.exitScope();
        if(ws.last != null) {
            symbolTable.enterScope("else");
            visitBody(ws.last);
            symbolTable.exitScope();
        }
    }

    private void visitForStatement(ForStatement fs) 
    {
        symbolTable.enterScope("for");
        fs.iterators.forEach(id -> {
            Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE);
            symbolTable.define(sym);
        });
        visitBody(fs.body);
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
        symbolTable.enterScope("if");

        

        visitBody(is.bodies.get(0));
        
        symbolTable.exitScope();
        for (int i = 1; i < is.bodies.size(); i++) {
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
        Symbol funcSym = new Symbol(fd.id.name, SymbolKind.FUNCTION);
        this.symbolTable.define(funcSym);

        this.symbolTable.enterScope("function " + fd.id.name);

        for (Parameter param : fd.parameters) {
            Symbol paramSym = new Symbol(param.id.name, SymbolKind.PARAMETER);
            this.symbolTable.define(paramSym);
        }
        visitBody(fd.body);
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

        } else if (sm instanceof GlobalStatement gs) {
            // added: `global x, y` - mark each name as global in the current scope so that
            // any later assignment to it (handled above/in visitExpressionStatement) is
            // defined in the module/global scope instead of this local scope
            for (ID id : gs.names)
                symbolTable.declareGlobal(id.name);
        }
    }

    private void visitImportStatement(ImportStatement is) {
        if(is instanceof FromImportStatement fis)
        {
            fis.targets.forEach(id -> {
                Symbol sym = new Symbol(id.name, SymbolKind.VARIABLE);
                symbolTable.define(sym);
            });
        }
        else if (is instanceof SimpleImportStatement sis)
        {
            String name = sis.dottedName.get(sis.dottedName.size() - 1).name;
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