package jinja2.filters;

import java.util.List;

@FunctionalInterface
public interface JinjaFilter {

    Object apply(
            Object value,
            List<Object> arguments
    );
}