package jinja2.symbol_table.semantic_rules;

import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.*;

import java.util.List;

public record SemanticContext(TemplateFile root, SymbolTable symbolTable, List<CompilerError> errors) {
}
