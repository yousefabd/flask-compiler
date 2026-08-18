package css;

import antlr.css.CSSLexer;
import antlr.css.CSSParser;
import compiler.logging.AnalysisLog;
import css.models.Stylesheet;
import css.visitors.AntlrToStyleSheet;
import errors.CompilerIoError;
import errors.CompilerStage;
import errors.ErrorReporter;
import errors.SyntaxErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import utils.CompilerUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class CssFrontend {

    private final Path staticDirectory;
    private final ErrorReporter reporter;
    private final AnalysisLog analysisLog;

    public CssFrontend(
            Path staticDirectory,
            ErrorReporter reporter,
            AnalysisLog analysisLog
    ) {
        this.staticDirectory =
                Objects.requireNonNull(
                        staticDirectory
                ).normalize();

        this.reporter =
                Objects.requireNonNull(reporter);

        this.analysisLog =
                Objects.requireNonNull(analysisLog);
    }

    public Map<String, Stylesheet> parseStylesheets() {
        Map<String, Stylesheet> stylesheets =
                new LinkedHashMap<>();

        for (Path stylesheetPath
                : findStylesheetFiles()) {

            analysisLog.record(
                    CompilerStage.PARSING,
                    "Parsing CSS stylesheet: "
                            + stylesheetPath
            );

            Stylesheet stylesheet =
                    parseStylesheet(stylesheetPath);

            if (stylesheet != null) {
                stylesheets.put(
                        relativeName(stylesheetPath),
                        stylesheet
                );
            }
        }

        return Collections.unmodifiableMap(
                new LinkedHashMap<>(stylesheets)
        );
    }

    private Stylesheet parseStylesheet(
            Path stylesheetPath
    ) {
        CharStream input =
                CompilerUtils.readSource(
                        stylesheetPath
                );

        SyntaxErrorListener listener =
                new SyntaxErrorListener(
                        stylesheetPath.toString()
                );

        CSSLexer lexer =
                new CSSLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);

        CSSParser parser =
                new CSSParser(
                        new CommonTokenStream(lexer)
                );

        parser.removeErrorListeners();
        parser.addErrorListener(listener);

        CSSParser.StylesheetContext tree =
                parser.stylesheet();

        if (listener.hasErrors()) {
            for (var problem
                    : listener.getErrors()) {
                reporter.report(problem);
            }

            return null;
        }

        return new AntlrToStyleSheet()
                .visitStylesheet(tree);
    }

    private List<Path> findStylesheetFiles() {
        if (!Files.isDirectory(staticDirectory)) {
            return List.of();
        }

        try (Stream<Path> paths =
                     Files.walk(staticDirectory)) {

            return paths
                    .filter(Files::isRegularFile)
                    .filter(this::isCssFile)
                    .sorted(
                            Comparator.comparing(
                                    this::relativeName
                            )
                    )
                    .toList();

        } catch (IOException exception) {
            throw new CompilerIoError(
                    staticDirectory.toString(),
                    "Could not discover CSS stylesheets",
                    exception
            );
        }
    }

    private boolean isCssFile(Path file) {
        return file.getFileName()
                .toString()
                .toLowerCase(Locale.ROOT)
                .endsWith(".css");
    }

    private String relativeName(Path file) {
        return staticDirectory
                .relativize(file)
                .toString()
                .replace('\\', '/');
    }
}