package compiler.artifacts;

import errors.CompilerIoError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class CompilerArtifactWriter {

    private static final String SEMANTIC_REPORT =
            "semantic_report.txt";

    private static final String ANALYSIS_LOG =
            "analysis_log.txt";

    private final Path outputDirectory;

    public CompilerArtifactWriter(
            Path outputDirectory
    ) {
        this.outputDirectory =
                Objects.requireNonNull(
                                outputDirectory
                        )
                        .toAbsolutePath()
                        .normalize();
    }

    public Path writeSemanticReport(
            String report
    ) {
        return writeText(
                SEMANTIC_REPORT,
                report
        );
    }

    public Path writeAnalysisLog(
            String log
    ) {
        return writeText(
                ANALYSIS_LOG,
                log
        );
    }

    private Path writeText(
            String fileName,
            String content
    ) {
        Objects.requireNonNull(content);

        Path outputFile =
                outputDirectory
                        .resolve(fileName)
                        .normalize();

        String fileContent =
                content.endsWith("\n")
                        ? content
                        : content
                        + System.lineSeparator();

        try {
            Files.createDirectories(
                    outputDirectory
            );

            Files.writeString(
                    outputFile,
                    fileContent,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );

            return outputFile;

        } catch (IOException exception) {
            throw new CompilerIoError(
                    outputFile.toString(),
                    "Could not write compiler artifact '"
                            + fileName
                            + "'",
                    exception
            );
        }
    }
}