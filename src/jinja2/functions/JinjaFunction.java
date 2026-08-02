package jinja2.functions;

import jinja2.runtime.RenderEnvironment;

@FunctionalInterface
public interface JinjaFunction {

    Object invoke(
            JinjaCallArguments arguments,
            RenderEnvironment environment
    );
}