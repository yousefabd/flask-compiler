package jinja2.renderer;

import jinja2.functions.JinjaCallArguments;

@FunctionalInterface
public interface TemplateCallable {

    Object invoke(JinjaCallArguments arguments);
}