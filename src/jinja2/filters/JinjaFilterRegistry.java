package jinja2.filters;

import java.lang.reflect.Array;
import java.util.*;

public final class JinjaFilterRegistry {

    private final Map<String, JinjaFilterDefinition> filters =
            new LinkedHashMap<>();

    public JinjaFilterRegistry() {
        registerBuiltInFilters();
    }

    public Optional<JinjaFilterDefinition> find(
            String name
    ) {
        return Optional.ofNullable(filters.get(name));
    }

    public Collection<JinjaFilterDefinition> getDefinitions() {
        return List.copyOf(filters.values());
    }

    private void registerBuiltInFilters() {
        register(
                "length",
                0,
                0,
                JinjaFilterRegistry::calculateLength
        );
        register(
                "format",
                1,
                Integer.MAX_VALUE,
                JinjaFilterRegistry::formatValue
        );
    }


    private void register(
            String name,
            int minimumArguments,
            int maximumArguments,
            JinjaFilter implementation
    ) {
        JinjaFilterDefinition definition =
                new JinjaFilterDefinition(
                        name,
                        minimumArguments,
                        maximumArguments,
                        implementation
                );

        if (filters.putIfAbsent(name, definition) != null) {
            throw new IllegalStateException(
                    "Duplicate Jinja filter registration: "
                            + name
            );
        }
    }

    private static Object calculateLength(
            Object value,
            List<Object> arguments
    ) {
        switch (value) {
            case null -> throw new IllegalArgumentException(
                    "Filter 'length' cannot be applied to none"
            );
            case CharSequence text -> {
                String string = text.toString();

                return string.codePointCount(
                        0,
                        string.length()
                );
            }
            case Collection<?> collection -> {
                return collection.size();
            }
            case Map<?, ?> map -> {
                return map.size();
            }
            default -> {
            }
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value);
        }

        if (value instanceof Iterable<?> iterable) {
            int length = 0;

            for (Object ignored : iterable) {
                length = Math.incrementExact(length);
            }

            return length;
        }

        throw new IllegalArgumentException(
                "Filter 'length' cannot be applied to "
                        + value.getClass().getSimpleName()
        );
    }
    private static Object formatValue(
            Object value,
            List<Object> arguments
    ) {
        if (!(value instanceof String formatPattern)) {
            throw new IllegalArgumentException(
                    "Filter 'format' requires a string format pattern"
            );
        }

        try {
            return String.format(
                    Locale.ROOT,
                    formatPattern,
                    arguments.toArray()
            );
        } catch (IllegalFormatException exception) {
            throw new IllegalArgumentException(
                    "Invalid format pattern '"
                            + formatPattern
                            + "': "
                            + exception.getMessage(),
                    exception
            );
        }
    }
}