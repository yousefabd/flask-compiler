package python.execution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.ToNumberPolicy;
import compiler.generation.TemplateRenderRequest;
import errors.CodeGenError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public final class CPythonExecutor {

    private static final String CONTEXT_MARKER =
            "__RENDER_CONTEXT__";

    private final Path pythonExecutable;
    private final Path captureScript;
    private final Path appSource;
    private static final Gson GSON = new GsonBuilder()
            .setObjectToNumberStrategy(
                    ToNumberPolicy.LONG_OR_DOUBLE
            )
            .create();

    private record CapturedRenderPayload(
            String templateName,
            Map<String, Object> context
    ) {}

    public CPythonExecutor(
            Path pythonExecutable,
            Path captureScript,
            Path appSource
    ) {
        this.pythonExecutable = pythonExecutable;
        this.captureScript = captureScript;
        this.appSource = appSource;
    }

    public String executeCaptureScript(String functionName) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutable.toAbsolutePath().toString(),
                captureScript.toAbsolutePath().toString(),
                appSource.toAbsolutePath().toString(),
                functionName
        );

        processBuilder.redirectErrorStream(true);
        processBuilder.environment().put(
                "PYTHONIOENCODING",
                "utf-8"
        );

        try {
            Process process = processBuilder.start();

            StringBuilder completeOutput = new StringBuilder();
            String capturedJson = null;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            )) {
                String line;

                while ((line = reader.readLine()) != null) {
                    completeOutput
                            .append(line)
                            .append(System.lineSeparator());

                    if (line.startsWith(CONTEXT_MARKER)) {
                        capturedJson = line.substring(
                                CONTEXT_MARKER.length()
                        );
                    }
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new CodeGenError(
                        appSource.toString(),
                        -1,
                        "CPython execution failed for function '"
                                + functionName
                                + "':\n"
                                + completeOutput
                );
            }

            if (capturedJson == null) {
                throw new CodeGenError(
                        appSource.toString(),
                        -1,
                        "CPython did not produce a render context for "
                                + "function '"
                                + functionName
                                + "'. Output:\n"
                                + completeOutput
                );
            }

            return capturedJson;

        } catch (IOException exception) {
            throw new CodeGenError(
                    appSource.toString(),
                    "Could not start CPython",
                    exception
            );

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new CodeGenError(
                    appSource.toString(),
                    "CPython execution was interrupted",
                    exception
            );
        }
    }
    public TemplateRenderRequest captureRender(
            String functionName
    ) {
        String capturedJson =
                executeCaptureScript(functionName);

        try {
            CapturedRenderPayload payload = GSON.fromJson(
                    capturedJson,
                    CapturedRenderPayload.class
            );

            if (payload == null
                    || payload.templateName() == null
                    || payload.context() == null) {

                throw new JsonParseException(
                        "Missing templateName or context"
                );
            }

            return new TemplateRenderRequest(
                    functionName,
                    payload.templateName(),
                    payload.context()
            );

        } catch (JsonParseException exception) {
            throw new CodeGenError(
                    appSource.toString(),
                    "CPython produced an invalid render context",
                    exception
            );
        }
    }
}