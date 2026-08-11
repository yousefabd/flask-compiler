package compiler.generation;

import errors.CodeGenError;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public final class SupportingFilesCopier {

    private final Path appSource;
    private final Path staticDirectory;
    private final Path outputDirectory;

    public SupportingFilesCopier(
            Path appSource,
            Path staticDirectory,
            Path outputDirectory
    ) {
        this.appSource =
                Objects.requireNonNull(appSource)
                        .toAbsolutePath()
                        .normalize();

        this.staticDirectory =
                Objects.requireNonNull(staticDirectory)
                        .toAbsolutePath()
                        .normalize();

        this.outputDirectory =
                Objects.requireNonNull(outputDirectory)
                        .toAbsolutePath()
                        .normalize();
    }

    public void copy() {
        try {
            Files.createDirectories(
                    outputDirectory
            );

            copyAppSource();

            if (Files.isDirectory(staticDirectory)) {
                copyStaticDirectory();
            }

        } catch (IOException exception) {
            throw new CodeGenError(
                    outputDirectory.toString(),
                    "Could not copy supporting application files",
                    exception
            );
        }
    }

    private void copyAppSource()
            throws IOException {
        if (!Files.isRegularFile(appSource)) {
            throw new IOException(
                    "Application source does not exist: "
                            + appSource
            );
        }

        Files.copy(
                appSource,
                outputDirectory.resolve(
                        appSource.getFileName()
                ),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private void copyStaticDirectory()
            throws IOException {
        Path outputStaticDirectory =
                outputDirectory.resolve("static");

        Files.walkFileTree(
                staticDirectory,
                new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult preVisitDirectory(
                            Path directory,
                            BasicFileAttributes attributes
                    ) throws IOException {
                        Path relative =
                                staticDirectory.relativize(
                                        directory
                                );

                        Files.createDirectories(
                                outputStaticDirectory.resolve(
                                        relative
                                )
                        );

                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(
                            Path file,
                            BasicFileAttributes attributes
                    ) throws IOException {
                        Path relative =
                                staticDirectory.relativize(
                                        file
                                );

                        Files.copy(
                                file,
                                outputStaticDirectory.resolve(
                                        relative
                                ),
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        return FileVisitResult.CONTINUE;
                    }
                }
        );
    }
}