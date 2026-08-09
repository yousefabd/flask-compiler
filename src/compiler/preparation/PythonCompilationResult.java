package compiler.preparation;

import compiler.template.TemplateCall;
import python.models.root.Program;
import python.symbol_table.SymbolTable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Everything produced while preparing the Python backend.
 * It contains static compiler information only—not changing
 * runtime values.
 */
public record PythonCompilationResult(
        Program program,
        SymbolTable symbolTable,
        List<TemplateCall> templateCalls,
        Map<String, List<TemplateCall>> callsByTemplate
) {
    public PythonCompilationResult {
        Objects.requireNonNull(program);
        Objects.requireNonNull(symbolTable);
        Objects.requireNonNull(templateCalls);
        Objects.requireNonNull(callsByTemplate);

        templateCalls =
                List.copyOf(templateCalls);

        Map<String, List<TemplateCall>> copiedCalls =
                new LinkedHashMap<>();

        for (Map.Entry<String, List<TemplateCall>> entry
                : callsByTemplate.entrySet()) {

            copiedCalls.put(
                    entry.getKey(),
                    List.copyOf(entry.getValue())
            );
        }

        callsByTemplate =
                Collections.unmodifiableMap(copiedCalls);
    }
}