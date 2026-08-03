package python.symbol_table.semantic_rules;

import python.models.root.Program;
import python.symbol_table.CompilerError;
import python.symbol_table.SymbolTable;

import java.util.List;

/** Everything a Python semantic rule needs: the AST, the symbol table, the error sink. */
public record SemanticContext(Program root, SymbolTable symbolTable, List<CompilerError> errors) {

    public void error(CompilerError.Kind kind, String message, int line,
                      String context, String symbolName) {
        errors.add(new CompilerError(kind, message, line, context, symbolName));
    }
}
