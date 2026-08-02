package jinja2.functions;

import jinja2.runtime.FlashMessage;
import jinja2.runtime.RenderEnvironment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GetFlashedMessagesFunction
        implements JinjaFunction {

    private static final String WITH_CATEGORIES =
            "with_categories";

    private static final String CATEGORY_FILTER =
            "category_filter";

    @Override
    public Object invoke(
            JinjaCallArguments arguments,
            RenderEnvironment environment
    ) {
        validateKeywordNames(arguments.keyword());

        boolean withCategories =
                readWithCategories(arguments);

        Set<String> categoryFilter =
                readCategoryFilter(arguments);

        List<Object> result = new ArrayList<>();

        for (FlashMessage flash :
                environment.flashedMessages()) {

            if (!categoryFilter.isEmpty()
                    && !categoryFilter.contains(
                    flash.category()
            )) {
                continue;
            }

            if (!withCategories) {
                result.add(flash.message());
                continue;
            }

            List<Object> categorizedMessage =
                    new ArrayList<>(2);

            categorizedMessage.add(
                    flash.category()
            );

            categorizedMessage.add(
                    flash.message()
            );

            result.add(categorizedMessage);
        }

        return result;
    }

    private void validateKeywordNames(
            Map<String, Object> keywordArguments
    ) {
        for (String name : keywordArguments.keySet()) {
            if (!name.equals(WITH_CATEGORIES)
                    && !name.equals(CATEGORY_FILTER)) {

                throw new IllegalArgumentException(
                        "get_flashed_messages() received "
                                + "unknown keyword argument '"
                                + name
                                + "'"
                );
            }
        }
    }

    private boolean readWithCategories(
            JinjaCallArguments arguments
    ) {
        boolean suppliedPositionally =
                !arguments.positional().isEmpty();

        boolean suppliedByKeyword =
                arguments.keyword()
                        .containsKey(WITH_CATEGORIES);

        if (suppliedPositionally
                && suppliedByKeyword) {

            throw new IllegalArgumentException(
                    "'with_categories' was provided more than once"
            );
        }

        Object value;

        if (suppliedByKeyword) {
            value = arguments.keyword()
                    .get(WITH_CATEGORIES);

        } else if (suppliedPositionally) {
            value = arguments.positional()
                    .getFirst();

        } else {
            return false;
        }

        if (!(value instanceof Boolean booleanValue)) {
            throw new IllegalArgumentException(
                    "'with_categories' must be boolean"
            );
        }

        return booleanValue;
    }

    private Set<String> readCategoryFilter(
            JinjaCallArguments arguments
    ) {
        boolean suppliedPositionally =
                arguments.positional().size() >= 2;

        boolean suppliedByKeyword =
                arguments.keyword()
                        .containsKey(CATEGORY_FILTER);

        if (suppliedPositionally
                && suppliedByKeyword) {

            throw new IllegalArgumentException(
                    "'category_filter' was provided more than once"
            );
        }

        Object value;

        if (suppliedByKeyword) {
            value = arguments.keyword()
                    .get(CATEGORY_FILTER);

        } else if (suppliedPositionally) {
            value = arguments.positional()
                    .get(1);

        } else {
            return Set.of();
        }

        return convertToCategories(value);
    }

    private Set<String> convertToCategories(
            Object value
    ) {
        if (value == null) {
            return Set.of();
        }

        Set<String> categories =
                new LinkedHashSet<>();

        if (value instanceof String category) {
            categories.add(category);
            return categories;
        }

        if (value instanceof Iterable<?> iterable) {
            for (Object item : iterable) {
                addCategory(
                        categories,
                        item
                );
            }

            return categories;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);

            for (int index = 0;
                 index < length;
                 index++) {

                addCategory(
                        categories,
                        Array.get(value, index)
                );
            }

            return categories;
        }

        throw new IllegalArgumentException(
                "'category_filter' must be iterable"
        );
    }

    private void addCategory(
            Set<String> categories,
            Object value
    ) {
        if (!(value instanceof String category)) {
            throw new IllegalArgumentException(
                    "Flash-message categories must be strings"
            );
        }

        categories.add(category);
    }
}