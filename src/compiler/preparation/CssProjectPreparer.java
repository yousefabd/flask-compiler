package compiler.preparation;

import compiler.logging.AnalysisLog;
import css.CssFrontend;
import css.models.Stylesheet;
import errors.CompilerException;
import errors.CompilerStage;
import errors.ErrorReporter;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public final class CssProjectPreparer {

    private final Path staticDirectory;
    private final ErrorReporter reporter;
    private final AnalysisLog analysisLog;

    public CssProjectPreparer(
            Path staticDirectory,
            ErrorReporter reporter,
            AnalysisLog analysisLog
    ) {
        this.staticDirectory =
                Objects.requireNonNull(
                        staticDirectory
                );

        this.reporter =
                Objects.requireNonNull(reporter);

        this.analysisLog =
                Objects.requireNonNull(analysisLog);
    }

    public CssCompilationResult prepare() {
        try {
            CssFrontend frontend =
                    new CssFrontend(
                            staticDirectory,
                            reporter,
                            analysisLog
                    );

            Map<String, Stylesheet> stylesheets =
                    frontend.parseStylesheets();

            if (reporter.hasErrors()) {
                return null;
            }

            analysisLog.record(
                    CompilerStage.PARSING,
                    "Parsed "
                            + stylesheets.size()
                            + " CSS stylesheet(s)."
            );

            return new CssCompilationResult(
                    stylesheets
            );

        } catch (CompilerException exception) {
            reporter.report(exception);

        } catch (RuntimeException exception) {
            reporter.reportUnexpected(
                    CompilerStage.PARSING,
                    staticDirectory.toString(),
                    exception
            );
        }

        return null;
    }
}