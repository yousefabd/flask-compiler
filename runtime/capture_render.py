import importlib.util
import json
import sys
from pathlib import Path


CONTEXT_MARKER = "__RENDER_CONTEXT__"


def load_app(app_path):
    spec = importlib.util.spec_from_file_location(
        "compiler_input_app",
        app_path
    )

    module = importlib.util.module_from_spec(spec)
    sys.modules[spec.name] = module
    spec.loader.exec_module(module)

    return module


def main():
    if len(sys.argv) != 3:
        raise RuntimeError(
            "Usage: capture_render.py <app.py> <function_name>"
        )

    app_path = Path(sys.argv[1]).resolve()
    function_name = sys.argv[2]

    app_module = load_app(app_path)
    captured_render = {}

    def capture_render_template(template_name, **context):
        captured_render["templateName"] = template_name
        captured_render["context"] = context
        return ""

    # Functions defined in app.py look up this module variable when called.
    app_module.render_template = capture_render_template

    route_function = getattr(app_module, function_name)
    route_function()

    if not captured_render:
        raise RuntimeError(
            f"Function '{function_name}' did not call render_template"
        )

    print(
        CONTEXT_MARKER
        + json.dumps(captured_render, ensure_ascii=False)
    )


if __name__ == "__main__":
    main()