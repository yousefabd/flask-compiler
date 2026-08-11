package compiler.generation.unit_tests;

import compiler.generation.SupportingFilesCopier;
import utils.CompilerSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SupportingFilesCopierTest {

    public static void main(
            String[] args
    ) throws IOException {
        SupportingFilesCopier copier =
                new SupportingFilesCopier(
                        CompilerSettings.appSource,
                        CompilerSettings.staticDir,
                        CompilerSettings.outputDir
                );

        copier.copy();

        Path outputDirectory =
                CompilerSettings.outputDir
                        .toAbsolutePath()
                        .normalize();

        Path copiedApp =
                outputDirectory.resolve("app.py");

        Path copiedStylesheet =
                outputDirectory.resolve(
                        Path.of(
                                "static",
                                "styles.css"
                        )
                );

        require(
                Files.isRegularFile(copiedApp),
                "app.py was not copied"
        );

        require(
                Files.mismatch(
                        CompilerSettings.appSource,
                        copiedApp
                ) == -1,
                "Copied app.py was modified"
        );

        require(
                Files.isRegularFile(copiedStylesheet),
                "styles.css was not copied"
        );

        require(
                Files.mismatch(
                        CompilerSettings.staticDir
                                .resolve("styles.css"),
                        copiedStylesheet
                ) == -1,
                "Copied stylesheet was modified"
        );

        System.out.println(
                "Supporting files copied successfully."
        );

        System.out.println(copiedApp);
        System.out.println(copiedStylesheet);
    }

    private static void require(
            boolean condition,
            String message
    ) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}