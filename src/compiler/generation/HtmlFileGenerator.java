package compiler.generation;

import compiler.runtime.CompiledApplication;
import errors.CodeGenError;
import html.formatting.HtmlFormatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;

public final class HtmlFileGenerator {

    private final Path outputDirectory;
    private final HtmlFormatter formatter;

    public HtmlFileGenerator(
            Path outputDirectory,
            HtmlFormatter formatter
    ) {
        this.outputDirectory =
                Objects.requireNonNull(
                                outputDirectory
                        )
                        .toAbsolutePath()
                        .normalize();

        this.formatter =
                Objects.requireNonNull(formatter);
    }

    public Path generate(
            CompiledApplication application,
            TemplateRenderRequest request
    ) {
        Objects.requireNonNull(application);
        Objects.requireNonNull(request);

        String rawHtml =
                application.render(request);

        String formattedHtml =
                Objects.requireNonNull(
                        formatter.format(rawHtml),
                        "HTML formatter returned null"
                );

        Path outputFile =
                resolveOutputFile(
                        request.templateName()
                );

        try {
            Files.createDirectories(
                    outputFile.getParent()
            );

            Files.writeString(
                    outputFile,
                    formattedHtml,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );

            return outputFile;

        } catch (IOException exception) {
            throw new CodeGenError(
                    outputFile.toString(),
                    "Could not write generated HTML file",
                    exception
            );
        }
    }

    private Path resolveOutputFile(
            String templateName
    ) {
        String outputName =
                convertToHtmlName(templateName);

        Path relativePath;

        try {
            relativePath =
                    Path.of(outputName);

        } catch (InvalidPathException exception) {
            throw new CodeGenError(
                    outputDirectory.toString(),
                    "Invalid template output path: "
                            + templateName,
                    exception
            );
        }

        if (relativePath.isAbsolute()) {
            throw new CodeGenError(
                    outputDirectory.toString(),
                    -1,
                    "Template output path cannot be absolute: "
                            + templateName
            );
        }

        Path outputFile =
                outputDirectory
                        .resolve(relativePath)
                        .normalize();

        if (!outputFile.startsWith(outputDirectory)) {
            throw new CodeGenError(
                    outputDirectory.toString(),
                    -1,
                    "Template output path escapes the output directory: "
                            + templateName
            );
        }

        return outputFile;
    }

    private String convertToHtmlName(
            String templateName
    ) {
        String lowercaseName =
                templateName.toLowerCase(
                        Locale.ROOT
                );

        for (String extension :
                new String[]{
                        ".jinja",
                        ".jinja2",
                        ".j2",
                        ".htm"
                }) {

            if (lowercaseName.endsWith(extension)) {
                return templateName.substring(
                        0,
                        templateName.length()
                                - extension.length()
                ) + ".html";
            }
        }

        if (lowercaseName.endsWith(".html")) {
            return templateName;
        }

        return templateName + ".html";
    }
}