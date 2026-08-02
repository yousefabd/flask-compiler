package jinja2.functions;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class JinjaFunctionRegistry {

    private final Map<String, JinjaFunctionDefinition> functions =
            new LinkedHashMap<>();

    public JinjaFunctionRegistry() {
        registerBuiltInFunctions();
    }

    public Optional<JinjaFunctionDefinition> find(
            String name
    ) {
        return Optional.ofNullable(
                functions.get(name)
        );
    }

    public Collection<JinjaFunctionDefinition>
    getDefinitions() {
        return List.copyOf(
                functions.values()
        );
    }

    private void registerBuiltInFunctions() {
        register(
                "get_flashed_messages",
                0,
                2,
                true,
                new GetFlashedMessagesFunction()
        );
        register(
                "url_for",
                1,
                Integer.MAX_VALUE,
                true,
                new UrlForFunction()
        );
    }

    private void register(
            String name,
            int minimumArguments,
            int maximumArguments,
            boolean acceptsKeywordArguments,
            JinjaFunction implementation
    ) {
        JinjaFunctionDefinition definition =
                new JinjaFunctionDefinition(
                        name,
                        minimumArguments,
                        maximumArguments,
                        acceptsKeywordArguments,
                        implementation
                );

        if (functions.putIfAbsent(
                name,
                definition
        ) != null) {

            throw new IllegalStateException(
                    "Duplicate Jinja function registration: "
                            + name
            );
        }
    }
}