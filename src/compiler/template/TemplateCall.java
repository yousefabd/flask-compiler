package compiler.template;

import python.models.expr_statement.Condition;

import java.util.Map;
//template request that maps to AST
public record TemplateCall(
        String ownerFunctionName,
        String templateName,
        Map<String, Condition> contextArguments,
        int line
) {}