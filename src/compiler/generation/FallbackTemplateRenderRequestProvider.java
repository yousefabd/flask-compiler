package compiler.generation;

import compiler.template.TemplateCall;
import errors.CodeGenError;
import errors.CompilerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Tries several render-context providers in order and returns the first result.
 *
 * <p>The pipeline uses this to prefer compile-time folding and only reach for
 * CPython when folding cannot prove a value — a route whose context comes out of a
 * database call has to be executed, one that passes literals does not.</p>
 *
 * <p>When every provider fails, the failures are reported together. Which one is
 * "the" cause depends on what the user was trying to do — a missing {@code .venv}
 * and an unfoldable expression are both plausible fixes — so the report shows both
 * instead of picking one.</p>
 */
public final class FallbackTemplateRenderRequestProvider
        implements TemplateRenderRequestProvider {

    private final List<TemplateRenderRequestProvider> providers;
    private final String sourceFile;

    public FallbackTemplateRenderRequestProvider(
            String sourceFile,
            List<TemplateRenderRequestProvider> providers) {

        this.sourceFile = Objects.requireNonNull(sourceFile);
        this.providers = List.copyOf(providers);

        if (this.providers.isEmpty())
            throw new IllegalArgumentException(
                    "At least one render request provider is required");
    }

    @Override
    public TemplateRenderRequest provide(TemplateCall call) {
        List<String> failures = new ArrayList<>();

        for (TemplateRenderRequestProvider provider : providers) {
            try {
                return provider.provide(call);

            } catch (CompilerException failure) {
                failures.add(describe(provider, failure.getMessage()));

            } catch (RuntimeException failure) {
                // A provider must not take the pipeline down: record it and try the next.
                failures.add(describe(provider,
                        failure.getClass().getSimpleName()
                                + ": " + failure.getMessage()));
            }
        }

        throw new CodeGenError(
                sourceFile,
                call.line(),
                "No render context could be produced for function '"
                        + call.ownerFunctionName() + "':\n    - "
                        + String.join("\n    - ", failures));
    }

    private static String describe(
            TemplateRenderRequestProvider provider, String message) {

        // ASCII only: the report is read in a terminal whose codepage may not
        // carry punctuation like an em dash.
        return provider.getClass().getSimpleName() + ": " + message;
    }
}
