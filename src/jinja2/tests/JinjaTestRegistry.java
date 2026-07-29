package jinja2.tests;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JinjaTestRegistry {

    private static final BigDecimal TWO =
            BigDecimal.valueOf(2);

    private final Map<String, JinjaTestDefinition> tests =
            new LinkedHashMap<>();

    public JinjaTestRegistry() {
        registerBuiltInTests();
    }

    public Optional<JinjaTestDefinition> find(String name) {
        return Optional.ofNullable(tests.get(name));
    }

    public Collection<JinjaTestDefinition> getDefinitions() {
        return List.copyOf(tests.values());
    }

    private void registerBuiltInTests() {
        register(
                "defined", 0, 0, true,
                (subject, arguments) -> subject.defined()
        );

        register(
                "undefined", 0, 0, true,
                (subject, arguments) -> !subject.defined()
        );

        register(
                "none", 0, 0, false,
                (subject, arguments) ->
                        subject.defined()
                                && subject.value() == null
        );

        register(
                "true", 0, 0, false,
                (subject, arguments) ->
                        Boolean.TRUE.equals(subject.value())
        );

        register(
                "false", 0, 0, false,
                (subject, arguments) ->
                        Boolean.FALSE.equals(subject.value())
        );

        register(
                "boolean", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof Boolean
        );

        register(
                "string", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof String
        );

        register(
                "number", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof Number
        );

        register(
                "integer", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof Byte
                                || subject.value() instanceof Short
                                || subject.value() instanceof Integer
                                || subject.value() instanceof Long
        );

        register(
                "float", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof Float
                                || subject.value() instanceof Double
        );

        register(
                "mapping", 0, 0, false,
                (subject, arguments) ->
                        subject.value() instanceof Map<?, ?>
        );

        register(
                "iterable", 0, 0, false,
                (subject, arguments) ->
                        isIterable(subject.value())
        );

        register(
                "even", 0, 0, false,
                (subject, arguments) ->
                        requireNumber(subject, "even")
                                .remainder(TWO)
                                .compareTo(BigDecimal.ZERO) == 0
        );

        register(
                "odd", 0, 0, false,
                (subject, arguments) ->
                        isOdd(
                                requireNumber(subject, "odd")
                        )
        );

        register(
                "divisibleby", 1, 1, false,
                (subject, arguments) -> {
                    BigDecimal value =
                            requireNumber(subject, "divisibleby");

                    BigDecimal divisor =
                            requireNumber(
                                    arguments.getFirst()
                            );

                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException(
                                "divisibleby cannot use zero"
                        );
                    }

                    return value.remainder(divisor)
                            .compareTo(BigDecimal.ZERO) == 0;
                }
        );
    }

    private void register(
            String name,
            int minimumArguments,
            int maximumArguments,
            boolean acceptsUndefined,
            JinjaTest implementation
    ) {
        JinjaTestDefinition definition =
                new JinjaTestDefinition(
                        name,
                        minimumArguments,
                        maximumArguments,
                        acceptsUndefined,
                        implementation
                );

        if (tests.putIfAbsent(name, definition) != null) {
            throw new IllegalStateException(
                    "Duplicate Jinja test registration: " + name
            );
        }
    }

    private static boolean isIterable(Object value) {
        return value instanceof Iterable<?>
                || value instanceof Map<?, ?>
                || value instanceof String
                || value != null
                && value.getClass().isArray();
    }

    private static BigDecimal requireNumber(
            TestValue subject,
            String testName
    ) {
        if (!subject.defined()
                || !(subject.value() instanceof Number number)) {
            throw new IllegalArgumentException(
                    "Test '" + testName
                            + "' requires a number"
            );
        }

        return new BigDecimal(number.toString());
    }

    private static BigDecimal requireNumber(
            Object value
    ) {
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException(
                    "divisibleby argument" + " must be a number"
            );
        }

        return new BigDecimal(number.toString());
    }
    private static boolean isOdd(BigDecimal value) {
        BigDecimal remainder =
                value.remainder(TWO);

        /*
         * BigDecimal remainder follows Java's sign rules, while
         * Python uses a non-negative remainder with a positive divisor.
         */
        if (remainder.signum() < 0) {
            remainder = remainder.add(TWO);
        }

        return remainder.compareTo(BigDecimal.ONE) == 0;
    }
}