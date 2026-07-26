package utils;

import java.nio.file.Path;

public class CompilerSettings {
    public static Path appSource = Path.of("tests/app.py");
    public static Path templatesDir = Path.of("tests/templates");
    public static Path staticDir = Path.of("tests/static");
    public static Path pythonExecutable =
            Path.of(".venv", "Scripts", "python.exe");

    public static Path renderCaptureScript =
            Path.of("runtime", "capture_render.py");

}
