package compiler.template;

import python.models.expr_statement.Condition;

import java.util.Map;

public record TemplateCall(
        String templateName,
        Map<String, Condition> contextArguments,
        int line
) {}