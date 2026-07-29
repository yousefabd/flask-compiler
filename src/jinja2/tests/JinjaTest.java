package jinja2.tests;

import java.util.List;

@FunctionalInterface
public interface JinjaTest {

    boolean evaluate(
            TestValue subject,
            List<Object> arguments
    );
}