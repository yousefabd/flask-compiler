package compiler.preparation;

import java.util.Objects;

/**
 * A successfully parsed and semantically analyzed application,
 * ready for execution and generation.
 */
public record PreparedApplication(
        PythonCompilationResult backend,
        TemplateCompilationResult frontend
) {
    public PreparedApplication {
        Objects.requireNonNull(backend);
        Objects.requireNonNull(frontend);
    }
}