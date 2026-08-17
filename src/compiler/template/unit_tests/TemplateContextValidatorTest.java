package compiler.template.unit_tests;

import compiler.CompilationPipeline;
import compiler.template.TemplateCall;
import compiler.template.TemplateContextValidator;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import jinja2.TemplateFrontend;
import jinja2.models.file.TemplateFile;
import jinja2.semantic.JinjaFreeVariableResult;
import jinja2.semantic.JinjaNameUse;
import jinja2.tests.JinjaTestRegistry;
import python.models.atom_statement.StringAtom;
import python.models.expr_statement.Condition;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TemplateContextValidatorTest {

    private static final Path TEMPLATES_DIRECTORY =
            Path.of("tests", "templates");

    public static void main(String[] args) {
        missingContextProducesOnePreciseDiagnostic();
        productTemplateUsesUnionOfRenderContexts();
        pipelineStopsOnMissingContext();
        definitionGuardMakesBranchValueOptional();
        staticIncludesInheritVisibleJinjaLocals();

        System.out.println(
                "Template context validation passed."
        );
    }

    private static void pipelineStopsOnMissingContext() {
        Path source = Path.of(
                "tests", "python", "semantic", "missing_context_app.py");
        CompilationPipeline pipeline =
                new CompilationPipeline(source, TEMPLATES_DIRECTORY);

        require(pipeline.compileApplication() == null,
                "Missing Flask context still constructed a runtime application");
        List<CompilerProblem> problems = pipeline.getProblems();
        require(problems.size() == 1,
                "Missing context was duplicated or mislabeled: " + problems);

        CompilerProblem problem = problems.getFirst();
        require("MISSING_FLASK_VARIABLE".equals(problem.getKind()),
                "Wrong pipeline context category: " + problem);
        require(problem.getStage() == CompilerStage.SEMANTIC_ANALYSIS,
                "Missing context has the wrong compiler stage");
        require(TEMPLATES_DIRECTORY.resolve("missing_context_test.html")
                        .normalize().toString().equals(problem.getFile()),
                "Missing context has the wrong template file");
        require(problem.getLine() == 17,
                "Missing context has the wrong first-use line");
    }

    private static void missingContextProducesOnePreciseDiagnostic() {
        String templateName =
                "missing_context_test.html";

        ParsedTemplate parsed = parse(templateName);
        JinjaFreeVariableResult freeVariables =
                parsed.frontend().collectFreeVariables(
                        parsed.template()
                );

        require(
                freeVariables.externalVariables()
                        .keySet()
                        .equals(
                                Set.of(
                                        "seed_value",
                                        "default_prefix",
                                        "supplied_items",
                                        "required_title"
                                )
                        ),
                "External template names were classified incorrectly: "
                        + freeVariables.externalVariables()
        );

        requireUse(
                freeVariables,
                "url_for",
                JinjaNameUse.Kind.BUILTIN
        );
        requireUse(
                freeVariables,
                "local_value",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "name",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "prefix",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "item",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "loop",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "show",
                JinjaNameUse.Kind.TEMPLATE_LOCAL
        );
        requireUse(
                freeVariables,
                "label",
                JinjaNameUse.Kind.PROPERTY_NAME
        );
        requireUse(
                freeVariables,
                "optional_value",
                JinjaNameUse.Kind.DEFINITION_GUARD
        );

        parsed.frontend().analyzeTemplate(
                templateName,
                parsed.template(),
                freeVariables.externalVariables().keySet()
        );

        require(
                !parsed.reporter().hasErrors(),
                "Jinja analysis misclassified Flask externals: "
                        + parsed.reporter().formatReport()
        );

        TemplateCall call = call(
                templateName,
                "seed_value",
                "default_prefix",
                "supplied_items"
        );

        List<CompilerProblem> problems =
                new TemplateContextValidator(
                        TEMPLATES_DIRECTORY
                ).validate(
                        Map.of(templateName, parsed.template()),
                        Map.of(templateName, freeVariables),
                        Map.of(templateName, List.of(call))
                );

        require(
                problems.size() == 1,
                "Repeated missing variable uses must produce one diagnostic: "
                        + problems
        );

        CompilerProblem problem = problems.getFirst();

        require(
                "MISSING_FLASK_VARIABLE".equals(problem.getKind()),
                "Wrong missing-context category"
        );
        require(
                problem.getStage()
                        == CompilerStage.SEMANTIC_ANALYSIS,
                "Wrong missing-context compiler stage"
        );
        require(
                TEMPLATES_DIRECTORY
                        .resolve(templateName)
                        .normalize()
                        .toString()
                        .equals(problem.getFile()),
                "Wrong missing-context source file: "
                        + problem.getFile()
        );
        require(
                problem.getLine() == 17,
                "Wrong first-use line: " + problem.getLine()
        );
    }

    private static void productTemplateUsesUnionOfRenderContexts() {
        String templateName = "index.html";
        ParsedTemplate parsed = parse(templateName);

        JinjaFreeVariableResult freeVariables =
                parsed.frontend().collectFreeVariables(
                        parsed.template()
                );

        require(
                freeVariables.externalVariables()
                        .keySet()
                        .equals(
                                Set.of(
                                        "page",
                                        "products",
                                        "product"
                                )
                        ),
                "Product template externals include a local, builtin, or property: "
                        + freeVariables.externalVariables()
        );

        List<TemplateCall> calls = List.of(
                call(templateName, "page"),
                call(templateName, "page", "products"),
                call(templateName, "page", "product")
        );

        List<CompilerProblem> problems =
                new TemplateContextValidator(
                        TEMPLATES_DIRECTORY
                ).validate(
                        Map.of(templateName, parsed.template()),
                        Map.of(templateName, freeVariables),
                        Map.of(templateName, calls)
                );

        require(
                problems.isEmpty(),
                "Valid branched product template received false missing-context errors: "
                        + problems
        );
    }
    private static void definitionGuardMakesBranchValueOptional() {
        String templateName =
                "defined_guard_context.html";

        ParsedTemplate parsed =
                parse(templateName);

        JinjaFreeVariableResult freeVariables =
                parsed.frontend().collectFreeVariables(
                        parsed.template()
                );

        require(
                freeVariables.externalVariables()
                        .keySet()
                        .equals(Set.of("required_title")),
                "Definition-guarded variables became required: "
                        + freeVariables.externalVariables()
        );

        requireUse(
                freeVariables,
                "optional_value",
                JinjaNameUse.Kind.DEFINITION_GUARD
        );

        requireUse(
                freeVariables,
                "second_value",
                JinjaNameUse.Kind.DEFINITION_GUARD
        );

        parsed.frontend().analyzeTemplate(
                templateName,
                parsed.template(),
                freeVariables.externalVariables().keySet()
        );

        require(
                !parsed.reporter().hasErrors(),
                "Definition guard caused Jinja semantic errors: "
                        + parsed.reporter().formatReport()
        );

        TemplateCall call =
                call(
                        templateName,
                        "required_title"
                );

        List<CompilerProblem> problems =
                new TemplateContextValidator(
                        TEMPLATES_DIRECTORY
                ).validate(
                        Map.of(
                                templateName,
                                parsed.template()
                        ),
                        Map.of(
                                templateName,
                                freeVariables
                        ),
                        Map.of(
                                templateName,
                                List.of(call)
                        )
                );

        require(
                problems.isEmpty(),
                "Definition-guarded values were reported as "
                        + "missing Flask variables: "
                        + problems
        );
    }

    private static ParsedTemplate parse(String templateName) {
        ErrorReporter reporter = new ErrorReporter();
        TemplateFrontend frontend =
                new TemplateFrontend(
                        TEMPLATES_DIRECTORY,
                        reporter,
                        new JinjaTestRegistry()
                );

        Map<String, TemplateFile> templates =
                frontend.parseTemplates(List.of(templateName));

        require(
                !reporter.hasErrors(),
                "Template parse failed: "
                        + reporter.formatReport()
        );

        TemplateFile template = templates.get(templateName);

        require(
                template != null,
                "Template was not returned by the frontend"
        );

        return new ParsedTemplate(
                frontend,
                reporter,
                template
        );
    }

    private static TemplateCall call(
            String templateName,
            String... contextNames
    ) {
        Map<String, Condition> context =
                new LinkedHashMap<>();

        for (String contextName : contextNames) {
            context.put(
                    contextName,
                    new StringAtom("\"value\"", 1)
            );
        }

        return new TemplateCall(
                "test_function",
                templateName,
                context,
                1
        );
    }

    private static void requireUse(
            JinjaFreeVariableResult result,
            String name,
            JinjaNameUse.Kind kind
    ) {
        boolean found = result.nameUses()
                .stream()
                .anyMatch(use ->
                        name.equals(use.name())
                                && use.kind() == kind
                );

        require(
                found,
                "Expected "
                        + kind
                        + " classification for '"
                        + name
                        + "'"
        );
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private record ParsedTemplate(
            TemplateFrontend frontend,
            ErrorReporter reporter,
            TemplateFile template
    ) {
    }
    private static void staticIncludesInheritVisibleJinjaLocals() {
        String rootTemplate =
                "include_context_parent.html";

        ErrorReporter reporter =
                new ErrorReporter();

        TemplateFrontend frontend =
                new TemplateFrontend(
                        TEMPLATES_DIRECTORY,
                        reporter,
                        new JinjaTestRegistry()
                );

        Map<String, TemplateFile> templates =
                frontend.parseTemplates(
                        List.of(rootTemplate)
                );

        require(
                !reporter.hasErrors(),
                "Include templates failed to parse: "
                        + reporter.formatReport()
        );

        Map<String, JinjaFreeVariableResult> freeVariables =
                new LinkedHashMap<>();

        for (Map.Entry<String, TemplateFile> entry
                : templates.entrySet()) {
            JinjaFreeVariableResult result =
                    frontend.collectFreeVariables(
                            entry.getValue()
                    );

            freeVariables.put(
                    entry.getKey(),
                    result
            );

            frontend.analyzeTemplate(
                    entry.getKey(),
                    entry.getValue(),
                    result.externalVariables().keySet()
            );
        }

        require(
                !reporter.hasErrors(),
                "Include templates failed semantic analysis: "
                        + reporter.formatReport()
        );

        TemplateCall rootCall =
                call(
                        rootTemplate,
                        "products"
                );

        List<CompilerProblem> problems =
                new TemplateContextValidator(
                        TEMPLATES_DIRECTORY
                ).validate(
                        templates,
                        freeVariables,
                        Map.of(
                                rootTemplate,
                                List.of(rootCall)
                        )
                );

        require(
                problems.isEmpty(),
                "Static includes did not inherit visible Jinja locals: "
                        + problems
        );
    }
}
