package compiler.logging;

import errors.CompilerStage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AnalysisLog {

    private final List<Entry> entries =
            new ArrayList<>();

    public void record(
            CompilerStage stage,
            String message
    ) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(message);

        if (stage != CompilerStage.PARSING
                && stage
                != CompilerStage.SEMANTIC_ANALYSIS) {

            throw new IllegalArgumentException(
                    "Analysis log does not accept stage "
                            + stage
            );
        }

        entries.add(
                new Entry(
                        stage,
                        message
                )
        );
    }

    public List<Entry> entries() {
        return List.copyOf(entries);
    }

    public String format() {
        if (entries.isEmpty()) {
            return "No analysis activity recorded.";
        }

        StringBuilder output =
                new StringBuilder();

        for (Entry entry : entries) {
            output.append('[')
                    .append(entry.stage())
                    .append("] ")
                    .append(entry.message())
                    .append('\n');
        }

        return output.toString();
    }

    public record Entry(
            CompilerStage stage,
            String message
    ) {
        public Entry {
            Objects.requireNonNull(stage);
            Objects.requireNonNull(message);
        }
    }
}