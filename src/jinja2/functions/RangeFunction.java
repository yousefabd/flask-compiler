package jinja2.functions;

import jinja2.runtime.RenderEnvironment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class RangeFunction
        implements JinjaFunction {

    @Override
    public Object invoke(
            JinjaCallArguments arguments,
            RenderEnvironment environment
    ) {
        List<Object> values =
                arguments.positional();

        long start = 0;
        long stop;
        long step = 1;

        switch (values.size()) {
            case 1 ->
                    stop = requireInteger(
                            values.get(0),
                            1
                    );

            case 2 -> {
                start = requireInteger(
                        values.get(0),
                        1
                );

                stop = requireInteger(
                        values.get(1),
                        2
                );
            }

            case 3 -> {
                start = requireInteger(
                        values.get(0),
                        1
                );

                stop = requireInteger(
                        values.get(1),
                        2
                );

                step = requireInteger(
                        values.get(2),
                        3
                );
            }

            default -> throw new IllegalArgumentException(
                    "range() expects between 1 and 3 arguments"
            );
        }

        if (step == 0) {
            throw new IllegalArgumentException(
                    "range() step cannot be zero"
            );
        }

        BigInteger currentValue =
                BigInteger.valueOf(start);

        BigInteger stopValue =
                BigInteger.valueOf(stop);

        BigInteger stepValue =
                BigInteger.valueOf(step);

        List<Long> result =
                new ArrayList<>();

        while (step > 0
                ? currentValue.compareTo(stopValue) < 0
                : currentValue.compareTo(stopValue) > 0) {

            result.add(
                    currentValue.longValueExact()
            );

            currentValue =
                    currentValue.add(stepValue);
        }

        return result;
    }

    private long requireInteger(
            Object value,
            int position
    ) {
        if (!(value instanceof Number number)
                || !isIntegerNumber(number)) {

            throw new IllegalArgumentException(
                    "Argument "
                            + position
                            + " of range() must be an integer"
            );
        }

        return number.longValue();
    }

    private boolean isIntegerNumber(
            Number number
    ) {
        return number instanceof Byte
                || number instanceof Short
                || number instanceof Integer
                || number instanceof Long;
    }
}