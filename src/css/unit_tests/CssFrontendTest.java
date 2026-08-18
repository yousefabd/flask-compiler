package css.unit_tests;

import compiler.logging.AnalysisLog;
import css.CssFrontend;
import css.models.Stylesheet;
import errors.ErrorReporter;
import utils.CompilerSettings;

import java.util.Map;

public final class CssFrontendTest {

    public static void main(String[] args) {
        ErrorReporter reporter =
                new ErrorReporter();

        AnalysisLog analysisLog =
                new AnalysisLog();

        CssFrontend frontend =
                new CssFrontend(
                        CompilerSettings.staticDir,
                        reporter,
                        analysisLog
                );

        Map<String, Stylesheet> stylesheets =
                frontend.parseStylesheets();

        require(
                !reporter.hasErrors(),
                "CSS parsing failed:\n"
                        + reporter.formatReport()
        );

        require(
                stylesheets.containsKey(
                        "styles.css"
                ),
                "styles.css was not discovered"
        );

        Stylesheet stylesheet =
                stylesheets.get("styles.css");

        require(
                !stylesheet.getRulesets().isEmpty(),
                "CSS AST contains no rulesets"
        );

        require(
                analysisLog.entries()
                        .stream()
                        .anyMatch(entry ->
                                entry.message()
                                        .contains(
                                                "styles.css"
                                        )
                        ),
                "CSS parsing was not recorded"
        );

        System.out.println(
                "CSS frontend passed."
        );

        System.out.println(
                "Rulesets: "
                        + stylesheet
                        .getRulesets()
                        .size()
        );
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