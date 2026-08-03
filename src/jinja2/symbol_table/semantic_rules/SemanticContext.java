package jinja2.symbol_table.semantic_rules;

import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.*;

import java.util.List;

public record SemanticContext(TemplateFile root, SymbolTable symbolTable, List<CompilerError> errors) {

    // added: mirrors python.symbol_table.semantic_rules.SemanticContext.error(),
    // so a rule in either front end records the same structured fields.
    public void error(CompilerError.Kind kind, String message, int line,
                      String context, String symbolName) {
        errors.add(new CompilerError(kind, message, line, context, symbolName));
    }
}
