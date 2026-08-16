package python.semantic;

import errors.CompilerProblem;
import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolTable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record PythonSemanticResult(
        SymbolTable symbolTable,
        Map<ID, Symbol> identifierBindings,
        Map<ASTNode, PythonType> inferredTypes,
        List<CompilerProblem> diagnostics
) {
    public PythonSemanticResult {
        Objects.requireNonNull(symbolTable);
        identifierBindings = immutableIdentityMap(identifierBindings);
        inferredTypes = immutableIdentityMap(inferredTypes);
        diagnostics = List.copyOf(diagnostics);
    }

    private static <K, V> Map<K, V> immutableIdentityMap(Map<K, V> source) {
        Objects.requireNonNull(source);
        return Collections.unmodifiableMap(new IdentityHashMap<>(source));
    }

    public boolean hasErrors() {
        return !diagnostics.isEmpty();
    }

    public Symbol getBinding(ID identifier) {
        return identifierBindings.get(identifier);
    }

    public PythonType getInferredType(ASTNode node) {
        return inferredTypes.getOrDefault(node, PythonType.UNKNOWN);
    }
}
