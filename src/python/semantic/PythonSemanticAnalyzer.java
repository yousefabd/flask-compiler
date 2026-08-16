package python.semantic;

import errors.CompilerProblem;
import python.models.ASTNode;
import python.models.atom_statement.ID;
import python.models.root.Program;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Coordinates declaration collection, lexical resolution, and type checking. */
public final class PythonSemanticAnalyzer {
    private final String sourceFile;

    public PythonSemanticAnalyzer(String sourceFile) {
        this.sourceFile = Objects.requireNonNull(sourceFile);
    }

    public PythonSemanticResult analyze(Program program) {
        Objects.requireNonNull(program);
        List<CompilerProblem> diagnostics = new ArrayList<>();

        PythonSymbolCollector collector =
                new PythonSymbolCollector(sourceFile, diagnostics);
        SymbolTable symbolTable = collector.collect(program);

        PythonGlobalDeclarationValidator.Result globalValidation =
                new PythonGlobalDeclarationValidator(
                        sourceFile,
                        diagnostics
                ).validate(program);

        Map<String, Symbol> builtins = PythonBuiltinCatalog.createSymbols();
        PythonNameResolver resolver = new PythonNameResolver(
                sourceFile,
                symbolTable,
                builtins,
                diagnostics,
                globalValidation.identifiersToSuppress()
        );
        Map<ID, Symbol> bindings = resolver.resolve(program);

        PythonTypeChecker typeChecker = new PythonTypeChecker(
                sourceFile, symbolTable, bindings, builtins, diagnostics);
        Map<ASTNode, PythonType> inferredTypes = typeChecker.check(program);

        return new PythonSemanticResult(
                symbolTable, bindings, inferredTypes, diagnostics);
    }
}
