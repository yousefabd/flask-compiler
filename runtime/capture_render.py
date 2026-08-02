import importlib.util
import json
import sys
from pathlib import Path

from flask import get_flashed_messages, has_request_context


CONTEXT_MARKER = "__RENDER_CONTEXT__"


def load_app(app_path):
    spec = importlib.util.spec_from_file_location(
        "compiler_input_app",
        app_path
    )

    app_module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = app_module
    spec.loader.exec_module(app_module)

    return app_module


def capture_routes(flask_app):
    routes = []

    for rule in flask_app.url_map.iter_rules():
        routes.append({
            "endpoint": rule.endpoint,
            "rule": rule.rule,
            "arguments": sorted(rule.arguments)
        })

    routes.sort(
        key=lambda route: (
            route["endpoint"],
            route["rule"]
        )
    )

    return routes


def capture_flash_messages():
    if not has_request_context():
        return []

    messages = get_flashed_messages(
        with_categories=True
    )

    return [
        {
            "category": category,
            "message": message
        }
        for category, message in messages
    ]


def main():
    if len(sys.argv) != 3:
        raise RuntimeError(
            "Usage: capture_render.py "
            "<app.py> <function_name>"
        )

    app_path = Path(sys.argv[1]).resolve()
    function_name = sys.argv[2]

    app_module = load_app(app_path)
    flask_app = app_module.app

    captured_render = {}

    def capture_render_template(
            template_name,
            **context
    ):
        captured_render["templateName"] = (
            template_name
        )

        captured_render["context"] = context

        captured_render["environment"] = {
            "flashedMessages":
                capture_flash_messages(),

            "routes":
                capture_routes(flask_app)
        }

        return ""

    app_module.render_template = (
        capture_render_template
    )

    route_function = getattr(
        app_module,
        function_name
    )

    with flask_app.test_request_context(
            path="/",
            method="GET"
    ):
        route_function()

    if not captured_render:
        raise RuntimeError(
            f"Function '{function_name}' "
            "did not call render_template"
        )

    print(
        CONTEXT_MARKER
        + json.dumps(
            captured_render,
            ensure_ascii=False
        )
    )


if __name__ == "__main__":
    main()