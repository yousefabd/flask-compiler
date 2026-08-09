package compiler.preparation;

import jinja2.models.file.TemplateFile;
import jinja2.symbol_table.SymbolTable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Everything produced while preparing the Jinja frontend.
 * Template ASTs and symbol tables are built once and reused
 * during every generation.
 */
public record TemplateCompilationResult(
        Map<String, TemplateFile> templates,
        Map<String, SymbolTable> symbolTables
) {
    public TemplateCompilationResult {
        Objects.requireNonNull(templates);
        Objects.requireNonNull(symbolTables);

        templates = Collections.unmodifiableMap(
                new LinkedHashMap<>(templates)
        );

        symbolTables = Collections.unmodifiableMap(
                new LinkedHashMap<>(symbolTables)
        );
    }
}