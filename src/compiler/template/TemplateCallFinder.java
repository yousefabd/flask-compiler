package compiler.template;

import errors.SemanticError;
import errors.CompilerProblem;
import errors.CompilerStage;
import errors.ErrorReporter;
import python.models.ASTNode;
import python.models.atom_statement.StringAtom;
import python.models.expr_statement.Condition;
import python.models.expr_statement.IDTrailer;
import python.models.funcdef.FunctionDef;
import python.models.trailer.Argument;
import python.models.trailer.CallArguments;
import python.models.trailer.Trailer;
import utils.CompilerSettings;
import utils.CompilerUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TemplateCallFinder {
    private static final String MODULE_SCOPE = "<module>";

    public static List<TemplateCall> findTemplateCalls(
            ASTNode root
    ) {
        ErrorReporter reporter = new ErrorReporter();
        List<TemplateCall> calls = findTemplateCalls(
                root,
                CompilerSettings.appSource.toString(),
                reporter
        );
        if (reporter.hasErrors()) {
            CompilerProblem problem = reporter.getProblems().getFirst();
            throw new SemanticError(
                    problem.getFile(),
                    problem.getLine(),
                    problem.getKind(),
                    problem.getMessage()
            );
        }
        return calls;
    }

    public static List<TemplateCall> findTemplateCalls(
            ASTNode root,
            String sourceFile,
            ErrorReporter reporter
    ) {
        List<TemplateCall> calls = new ArrayList<>();

        collectTemplateCalls(
                root,
                MODULE_SCOPE,
                calls,
                sourceFile,
                reporter
        );

        return calls;
    }

    private static void collectTemplateCalls(
            ASTNode node,
            String currentFunction,
            List<TemplateCall> calls,
            String sourceFile,
            ErrorReporter reporter
    ) {
        String functionForChildren = currentFunction;

        if (node instanceof FunctionDef function &&
                function.id != null) {
            functionForChildren = function.id.name;
        }

        if (node instanceof IDTrailer expression) {
            TemplateCall call = readTemplateCall(
                    expression,
                    currentFunction,
                    sourceFile,
                    reporter
            );

            if (call != null) {
                calls.add(call);
            }
        }

        for (ASTNode child : node.getChildren()) {
            if (child != null) {
                collectTemplateCalls(
                        child,
                        functionForChildren,
                        calls,
                        sourceFile,
                        reporter
                );
            }
        }
    }
    private static TemplateCall readTemplateCall(
            IDTrailer expression,
            String ownerFunctionName,
            String sourceFile,
            ErrorReporter reporter
    ) {
        if (expression.id == null ||
                !"render_template".equals(expression.id.name)) {
            return null;
        }

        if (expression.trailers == null ||
                expression.trailers.isEmpty()) {
            return null;
        }

        Trailer callTrailer = expression.trailers.getFirst();

        if (!(callTrailer.arguments instanceof CallArguments callArguments)) {
            return null;
        }

        if (callArguments.args == null || callArguments.args.isEmpty()) {
            reportInvalid(reporter, sourceFile, expression.getLine(),
                    "render_template requires a template filename");
            return null;
        }

        Argument templateArgument = callArguments.args.getFirst();

        if (templateArgument.isAssigned() ||
                !(templateArgument.arg instanceof StringAtom templateString)) {
            reportInvalid(reporter, sourceFile, expression.getLine(),
                    "The template filename must be a string literal");
            return null;
        }

        String templateName = CompilerUtils.stripStringQuotes(templateString.value);
        Map<String, Condition> contextArguments = new LinkedHashMap<>();

        for (int i = 1; i < callArguments.args.size(); i++) {
            Argument argument = callArguments.args.get(i);

            if (!argument.isAssigned() ||
                    !(argument.arg instanceof IDTrailer keyword) ||
                    keyword.id == null ||
                    (keyword.trailers != null &&
                            !keyword.trailers.isEmpty())) {
                reportInvalid(reporter, sourceFile, argument.getLine(),
                        "Template context arguments must use name=value syntax");
                return null;
            }

            contextArguments.put(
                    keyword.id.name,
                    argument.assign
            );
        }

        return new TemplateCall(
                ownerFunctionName,
                templateName,
                contextArguments,
                expression.getLine()
        );
    }

    private static void reportInvalid(
            ErrorReporter reporter, String sourceFile, int line, String message) {
        reporter.report(new CompilerProblem(
                CompilerStage.SEMANTIC_ANALYSIS,
                "INVALID_TEMPLATE_CALL",
                sourceFile,
                line,
                message
        ));
    }
    public static Map<String, List<TemplateCall>> groupTemplateCalls(
            List<TemplateCall> calls
    ) {
        Map<String, List<TemplateCall>> callsByTemplate =
                new LinkedHashMap<>();

        for (TemplateCall call : calls) {
            callsByTemplate
                    .computeIfAbsent(
                            call.templateName(),
                            name -> new ArrayList<>()
                    )
                    .add(call);
        }

        return callsByTemplate;
    }
}
