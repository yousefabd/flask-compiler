package python.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record PythonCallArguments(
        List<Object> positional,
        Map<String, Object> keywords,
        int sourceLine
) {
    public PythonCallArguments {
        Objects.requireNonNull(positional);
        Objects.requireNonNull(keywords);

        // These copies permit Java null, which represents Python None.
        positional = Collections.unmodifiableList(
                new ArrayList<>(positional)
        );

        keywords = Collections.unmodifiableMap(
                new LinkedHashMap<>(keywords)
        );
    }
}