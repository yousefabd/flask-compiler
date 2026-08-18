package compiler.artifacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import css.models.Stylesheet;
import errors.CodeGenError;
import jinja2.models.file.TemplateFile;
import python.models.root.Program;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class AstJsonSerializer {

    private final Gson gson;

    public AstJsonSerializer() {
        this.gson =
                new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .create();
    }

    public String serializePython(
            Program program
    ) {
        Objects.requireNonNull(program);

        return serialize(
                program,
                "ast_python.json"
        );
    }

    public String serializeJinja(
            Map<String, TemplateFile> templates
    ) {
        Objects.requireNonNull(templates);

        /*
         * Preserve template discovery order and use template
         * filenames as the top-level JSON keys.
         */
        Map<String, TemplateFile> orderedTemplates =
                new LinkedHashMap<>(templates);

        return serialize(
                orderedTemplates,
                "ast_jinja.json"
        );
    }
    public String serializeCss(
            Map<String, Stylesheet> stylesheets
    ) {
        Objects.requireNonNull(stylesheets);

        Map<String, Stylesheet> orderedStylesheets =
                new LinkedHashMap<>(
                        stylesheets
                );

        return serialize(
                orderedStylesheets,
                "ast_css.json"
        );
    }

    private String serialize(
            Object ast,
            String artifactName
    ) {
        try {
            return gson.toJson(ast);

        } catch (RuntimeException exception) {
            throw new CodeGenError(
                    artifactName,
                    "Could not serialize AST as JSON",
                    exception
            );
        }
    }
}