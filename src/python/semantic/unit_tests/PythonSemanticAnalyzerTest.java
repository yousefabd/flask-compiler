package python.semantic.unit_tests;

import compiler.CompilationPipeline;
import compiler.runtime.CompiledApplication;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.PythonFrontend;
import python.models.root.Program;
import python.semantic.PythonSemanticResult;
import python.symbol_table.Scope;
import python.symbol_table.ScopeKind;
import python.symbol_table.Symbol;
import python.symbol_table.SymbolKind;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PythonSemanticAnalyzerTest {
    private static final Path FIXTURES =
            Path.of("tests", "python", "semantic");

    public static void main(String[] args) {
        validProgramBuildsPythonLexicalScopes();
        everySupportedReadPositionIsResolved();
        augmentedAssignmentRequiresAnExistingName();
        scopeViolationsAreDistinctFromUndefinedNames();
        typeChecksAreConservativeAndPrecise();
        semanticFailureStopsThePipelineBeforeTemplatesAndRuntime();
        templateDiscoveryCollectsSourceAccurateDiagnostics();
        useBeforeAssignmentIsUndefined();
        forLoopAssignmentsRequirePriorInitialization();
        ifBranchesMergeInitializedSymbols();
        System.out.println("Python semantic analysis passed.");
    }

    private static void templateDiscoveryCollectsSourceAccurateDiagnostics() {
        Path source = FIXTURES.resolve("invalid_template_calls.py");
        CompilationPipeline pipeline = new CompilationPipeline(
                source,
                FIXTURES.resolve("templates_must_not_be_read")
        );

        require(pipeline.compileApplication() == null,
                "Invalid render_template calls still constructed an application");
        List<CompilerProblem> problems = pipeline.getProblems();
        require(problems.size() == 2,
                "Template-call diagnostics were not collected: " + problems);
        require(problems.stream().allMatch(problem ->
                        "INVALID_TEMPLATE_CALL".equals(problem.getKind())
                                && problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS
                                && source.toString().equals(problem.getFile())),
                "Template-call diagnostics are not source-accurate: " + problems);
        require(problems.stream().map(CompilerProblem::getLine)
                        .collect(java.util.stream.Collectors.toSet())
                        .equals(Set.of(5, 8)),
                "Template-call diagnostic lines are inaccurate: " + problems);
        requireNoDuplicateProblems(problems);
    }

    private static void validProgramBuildsPythonLexicalScopes() {
        Analysis analysis = analyze("valid_semantics.py");
        require(!analysis.result().hasErrors(),
                "Valid fixture received diagnostics: " + analysis.result().diagnostics());

        var table = analysis.result().symbolTable();
        Scope module = table.getModuleScope();
        require(module.getKind() == ScopeKind.MODULE, "Root scope is not MODULE");
        require(table.getAllScopes().size() == 5,
                "Only module/function lexical scopes should exist: " + table.getAllScopes());
        require(module.getChildren().size() == 3,
                "Function scopes were not recorded as module children");

        requireNames(module,
                "os", "Flask", "module_value", "products", "module_name",
                "if_value", "else_value", "module_item", "for_value",
                "for_else_value", "while_value", "while_else_value",
                "caller", "later", "rebound", "result", "outer");
        require(module.resolveLocal("path") == null,
                "import os.path must bind os rather than path");
        require(module.resolveLocal("function_if") == null,
                "Function locals leaked into module scope");

        Scope caller = findScope(table.getAllScopes(), "function caller");
        Scope later = findScope(table.getAllScopes(), "function later");
        Scope outer = findScope(table.getAllScopes(), "function outer");
        Scope inner = findScope(table.getAllScopes(), "function inner");
        require(caller.getKind() == ScopeKind.FUNCTION, "caller is not a FUNCTION scope");
        requireNames(caller,
                "parameter", "function_if", "item", "function_for", "function_while");
        require(caller.resolveLocal("products") == null,
                "global products was incorrectly declared local");
        require(caller.getGlobalNames().equals(Set.of("products")),
                "Function-wide global declaration was not retained");
        requireNames(later, "value", "converted");
        requireNames(outer, "captured", "inner");
        require(inner.getParent() == outer && outer.getChildren().equals(List.of(inner)),
                "Nested function scope was not linked to its lexical parent");
        require(outer.resolveLocal("captured").getUsageLines().equals(Set.of(42)),
                "Nested function did not resolve its enclosing local");

        Symbol laterFunction = module.resolveLocal("later");
        require(laterFunction.getKind() == SymbolKind.FUNCTION,
                "Later function declaration is not callable metadata");
        require(laterFunction.getDeclarationLine() == 31,
                "Later function declaration line is inaccurate");
        require(laterFunction.getUsageLines().equals(Set.of(29)),
                "Forward function use was not resolved accurately: "
                        + laterFunction.getUsageLines());

        Symbol parameter = caller.resolveLocal("parameter");
        require(parameter.getKind() == SymbolKind.PARAMETER,
                "Parameter was not declared in its local scope");
        require(parameter.getUsageLines().equals(Set.of(22, 23, 26, 27, 29)),
                "Parameter usage lines are inaccurate: " + parameter.getUsageLines());

        require(analysis.result().identifierBindings().values().stream()
                        .anyMatch(symbol -> symbol.getKind() == SymbolKind.BUILTIN
                                && "__name__".equals(symbol.getName())),
                "__name__ was not resolved through the semantic builtin catalog");
        require(analysis.result().identifierBindings().values().stream()
                        .anyMatch(symbol -> symbol.getKind() == SymbolKind.BUILTIN
                                && "float".equals(symbol.getName())),
                "float was not resolved through the semantic builtin catalog");

        expectImmutable(() -> analysis.result().identifierBindings().clear(),
                "Identifier bindings are mutable");
        expectImmutable(() -> module.getSymbols().clear(),
                "Scope symbols are mutable");
        expectImmutable(() -> parameter.getUsageLines().clear(),
                "Symbol usage lines are mutable");
        expectImmutable(() -> analysis.reporter().getProblems().clear(),
                "ErrorReporter problems are mutable");

        boolean rootExitFailed = false;
        try {
            table.exitScope();
        } catch (IllegalStateException expected) {
            rootExitFailed = true;
        }
        require(rootExitFailed, "Exiting the module scope did not fail clearly");
    }

    private static void everySupportedReadPositionIsResolved() {
        Analysis analysis = analyze("undefined_reads.py");
        List<CompilerProblem> problems = problemsOfKind(
                analysis.result(), "UNDEFINED_VARIABLE");
        require(problems.size() == 13,
                "Not every read position was visited exactly once: " + problems);
        require(problemNames(problems).equals(Set.of(
                        "missing_condition", "missing_iterable", "missing_while",
                        "missing_return", "missing_call", "missing_argument",
                        "missing_index", "missing_subscript", "missing_list",
                        "missing_key", "missing_value", "missing_expression",
                        "missing_rhs")),
                "Unexpected undefined-name coverage: " + problems);
        requireNoDuplicateProblems(problems);
        requirePreciseDiagnostics(analysis.source(), problems, "UNDEFINED_VARIABLE");
    }

    private static void augmentedAssignmentRequiresAnExistingName() {
        Analysis analysis = analyze("augassign_undefined.py");
        List<CompilerProblem> problems = analysis.result().diagnostics();
        require(problems.size() == 1, "Augmented assignment produced wrong diagnostics: " + problems);
        CompilerProblem problem = problems.getFirst();
        require("UNDEFINED_VARIABLE".equals(problem.getKind()), "Wrong augmented category");
        require(problem.getLine() == 1, "Wrong augmented-assignment line");
        requirePreciseDiagnostics(analysis.source(), problems, "UNDEFINED_VARIABLE");
    }

    private static void scopeViolationsAreDistinctFromUndefinedNames() {
        Analysis analysis = analyze("scope_errors.py");
        List<CompilerProblem> problems = analysis.result().diagnostics();
        require(problems.size() == 2, "Expected sibling and module scope errors: " + problems);
        require(problems.stream().allMatch(problem -> "SCOPE".equals(problem.getKind())),
                "Declared-but-invisible names were not classified as SCOPE");
        require(problems.stream().map(CompilerProblem::getLine).collect(java.util.stream.Collectors.toSet())
                        .equals(Set.of(6, 8)),
                "Scope diagnostic lines are inaccurate: " + problems);
        requireNoDuplicateProblems(problems);
        requirePreciseDiagnostics(analysis.source(), problems, "SCOPE");
    }

    private static void typeChecksAreConservativeAndPrecise() {
        Analysis analysis = analyze("type_checks.py");
        List<CompilerProblem> mismatches = problemsOfKind(
                analysis.result(), "TYPE_MISMATCH");
        List<CompilerProblem> typeErrors = problemsOfKind(
                analysis.result(), "TYPE_ERROR");

        require(mismatches.size() == 1 && mismatches.getFirst().getLine() == 4,
                "Definite binary mismatch was not reported precisely: " + mismatches);
        require(typeErrors.size() == 4,
                "Call, index, iteration, and attribute misuse were not all reported: " + typeErrors);
        require(typeErrors.stream().map(CompilerProblem::getLine)
                        .collect(java.util.stream.Collectors.toSet())
                        .equals(Set.of(7, 8, 9, 12)),
                "TYPE_ERROR lines are inaccurate: " + typeErrors);
        require(analysis.result().diagnostics().size() == 5,
                "Legal type rebinding produced an extra diagnostic: "
                        + analysis.result().diagnostics());
        requireNoDuplicateProblems(analysis.result().diagnostics());
        requirePreciseDiagnostics(analysis.source(), mismatches, "TYPE_MISMATCH");
        requirePreciseDiagnostics(analysis.source(), typeErrors, "TYPE_ERROR");
    }

    private static void semanticFailureStopsThePipelineBeforeTemplatesAndRuntime() {
        Path source = FIXTURES.resolve("pipeline_stop.py");
        Path absentTemplates = FIXTURES.resolve("templates_must_not_be_read");
        require(!Files.exists(absentTemplates), "Pipeline-stop template path unexpectedly exists");

        CompilationPipeline pipeline = new CompilationPipeline(source, absentTemplates);
        CompiledApplication application = pipeline.compileApplication();
        require(application == null, "Semantic failure still constructed a runtime application");
        require(!Files.exists(absentTemplates), "Template preparation touched an unreachable path");

        List<CompilerProblem> problems = pipeline.getProblems();
        require(problems.size() == 1,
                "Template discovery or runtime ran after the semantic failure: " + problems);
        CompilerProblem problem = problems.getFirst();
        require("UNDEFINED_VARIABLE".equals(problem.getKind()), "Wrong pipeline-stop category");
        require(problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS,
                "Pipeline-stop failure was mislabeled as runtime/generation");
        require(source.toString().equals(problem.getFile()) && problem.getLine() == 2,
                "Pipeline-stop diagnostic location is inaccurate: " + problem);
        expectImmutable(() -> problems.clear(), "Pipeline diagnostic result is mutable");
    }

    private static Analysis analyze(String filename) {
        Path source = FIXTURES.resolve(filename);
        ErrorReporter reporter = new ErrorReporter();
        PythonFrontend frontend = new PythonFrontend(source, reporter);
        Program program = frontend.parsePython();
        require(program != null, "Fixture did not parse: " + source + "\n" + reporter.formatReport());
        PythonSemanticResult result = frontend.analyzePython(program);
        require(result.diagnostics().equals(reporter.getProblems()),
                "Frontend did not report the semantic result coherently");
        return new Analysis(source, reporter, result);
    }

    private static List<CompilerProblem> problemsOfKind(
            PythonSemanticResult result, String kind) {
        return result.diagnostics().stream()
                .filter(problem -> kind.equals(problem.getKind()))
                .toList();
    }

    private static void requirePreciseDiagnostics(
            Path source, List<CompilerProblem> problems, String kind) {
        for (CompilerProblem problem : problems) {
            require(kind.equals(problem.getKind()), "Wrong diagnostic category: " + problem);
            require(source.toString().equals(problem.getFile()), "Wrong diagnostic file: " + problem);
            require(problem.getLine() > 0, "Diagnostic has no useful line: " + problem);
            require(problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS,
                    "Wrong diagnostic stage: " + problem);
        }
    }

    private static Set<String> problemNames(List<CompilerProblem> problems) {
        Set<String> names = new HashSet<>();
        for (CompilerProblem problem : problems) {
            int first = problem.getMessage().indexOf('\'');
            int last = problem.getMessage().lastIndexOf('\'');
            require(first >= 0 && last > first, "Diagnostic has no quoted name: " + problem);
            names.add(problem.getMessage().substring(first + 1, last));
        }
        return names;
    }

    private static void requireNoDuplicateProblems(List<CompilerProblem> problems) {
        Set<String> unique = new HashSet<>();
        for (CompilerProblem problem : problems) {
            String key = problem.getKind() + "|" + problem.getLine() + "|" + problem.getMessage();
            require(unique.add(key), "Duplicate diagnostic: " + problem);
        }
    }

    private static Scope findScope(List<Scope> scopes, String name) {
        return scopes.stream()
                .filter(scope -> name.equals(scope.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing scope " + name));
    }

    private static void requireNames(Scope scope, String... names) {
        for (String name : names) {
            require(scope.resolveLocal(name) != null,
                    "Scope " + scope.getName() + " is missing declaration " + name);
        }
    }

    private static void expectImmutable(Runnable mutation, String message) {
        boolean failed = false;
        try {
            mutation.run();
        } catch (UnsupportedOperationException expected) {
            failed = true;
        }
        require(failed, message);
    }
    private static void useBeforeAssignmentIsUndefined() {
        Analysis analysis =
                analyze("use_before_assignment.py");

        List<CompilerProblem> problems =
                problemsOfKind(
                        analysis.result(),
                        "UNDEFINED_VARIABLE"
                );

        require(
                problems.size() == 3,
                "Expected 3 use-before-assignment errors: "
                        + problems
        );

        require(
                problemNames(problems).equals(
                        Set.of(
                                "module_value",
                                "local_value",
                                "counter"
                        )
                ),
                "Wrong use-before-assignment names: "
                        + problems
        );

        requireNoDuplicateProblems(problems);

        requirePreciseDiagnostics(
                analysis.source(),
                problems,
                "UNDEFINED_VARIABLE"
        );
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private record Analysis(
            Path source,
            ErrorReporter reporter,
            PythonSemanticResult result
    ) { }
    private static void ifBranchesMergeInitializedSymbols() {
        Analysis analysis =
                analyze("if_definite_assignment.py");

        List<CompilerProblem> problems =
                problemsOfKind(
                        analysis.result(),
                        "UNDEFINED_VARIABLE"
                );

        require(
                problems.size() == 3,
                "Expected 3 if-flow errors: "
                        + problems
        );

        require(
                problemNames(problems).equals(
                        Set.of(
                                "maybe",
                                "branch_later",
                                "earlier_branch"
                        )
                ),
                "Wrong if-flow names: "
                        + problems
        );

        requireNoDuplicateProblems(problems);

        requirePreciseDiagnostics(
                analysis.source(),
                problems,
                "UNDEFINED_VARIABLE"
        );
    }
    private static void forLoopAssignmentsRequirePriorInitialization() {
        Analysis analysis =
                analyze("for_definite_assignment.py");

        List<CompilerProblem> problems =
                problemsOfKind(
                        analysis.result(),
                        "UNDEFINED_VARIABLE"
                );

        require(
                problems.size() == 3,
                "Expected 3 for-loop definite-assignment errors: "
                        + problems
        );

        require(
                problemNames(problems).equals(
                        Set.of(
                                "item",
                                "inside_value",
                                "maybe"
                        )
                ),
                "Wrong for-loop undefined variables: "
                        + problems
        );

        require(
                problems.stream()
                        .map(CompilerProblem::getLine)
                        .collect(
                                java.util.stream.Collectors.toSet()
                        )
                        .equals(Set.of(6, 7, 18)),
                "Wrong for-loop diagnostic lines: "
                        + problems
        );

        requireNoDuplicateProblems(problems);

        requirePreciseDiagnostics(
                analysis.source(),
                problems,
                "UNDEFINED_VARIABLE"
        );
    }
}
