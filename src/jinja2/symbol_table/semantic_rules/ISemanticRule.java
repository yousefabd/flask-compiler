package jinja2.symbol_table.semantic_rules;

import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.SymbolTable;

import java.util.List;

public interface ISemanticRule {
    void validate(TemplateFile root, SymbolTable table, List<String> errors);
}