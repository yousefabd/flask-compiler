package jinja2.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record JinjaCallArguments(
        List<Object> positional,
        Map<String, Object> keyword
) {
    public JinjaCallArguments {
        Objects.requireNonNull(positional);
        Objects.requireNonNull(keyword);

        /*
         * These copies allow null, unlike List.copyOf and Map.copyOf.
         */
        positional = Collections.unmodifiableList(
                new ArrayList<>(positional)
        );

        keyword = Collections.unmodifiableMap(
                new LinkedHashMap<>(keyword)
        );
    }

    public int count() {
        return positional.size()
                + keyword.size();
    }

    public boolean hasKeywordArguments() {
        return !keyword.isEmpty();
    }
}