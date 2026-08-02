package python.execution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.ToNumberPolicy;
import compiler.generation.TemplateRenderRequest;
import compiler.generation.TemplateRenderRequestProvider;
import compiler.template.TemplateCall;
import errors.CodeGenError;
import jinja2.runtime.RenderEnvironment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class CPythonTemplateRenderRequestProvider
        implements TemplateRenderRequestProvider {

    private static final String CONTEXT_MARKER =
            "__RENDER_CONTEXT__";

    private static final Gson GSON =
            new GsonBuilder()
                    .setObjectToNumberStrategy(
                            ToNumberPolicy.LONG_OR_DOUBLE
                    )
                    .create();

    private final Path pythonExecutable;
    private final Path captureScript;
    private final Path appSource;

    /*
     * This is private because it only represents the JSON protocol
     * between Java and capture_render.py.
     *
     * It is not part of the compiler's public model.
     */
    private record CapturedRenderPayload(
            String templateName,
            Map<String, Object> context,
            RenderEnvironment environment
    ) {
    }

    public CPythonTemplateRenderRequestProvider(
            Path pythonExecutable,
            Path captureScript,
            Path appSource
    ) {
        this.pythonExecutable =
                Objects.requireNonNull(pythonExecutable);

        this.captureScript =
                Objects.requireNonNull(captureScript);

        this.appSource =
                Objects.requireNonNull(appSource);
    }

    @Override
    public TemplateRenderRequest provide(
            TemplateCall call
    ) {
        Objects.requireNonNull(call);

        String capturedJson =
                executeCaptureAsJsonScript(call);

        CapturedRenderPayload payload =
                parsePayload(
                        capturedJson,
                        call
                );

        validatePayload(
                call,
                payload
        );

        return new TemplateRenderRequest(
                payload.templateName(),
                payload.context(),
                payload.environment()
        );
    }

    private CapturedRenderPayload parsePayload(
            String capturedJson,
            TemplateCall call
    ) {
        try {
            CapturedRenderPayload payload =
                    GSON.fromJson(
                            capturedJson,
                            CapturedRenderPayload.class
                    );

            if (payload == null
                    || payload.templateName() == null
                    || payload.templateName().isBlank()
                    || payload.context() == null
                    || payload.environment() == null) {

                throw new JsonParseException(
                        "Missing templateName, context, or environment"
                );
            }

            return payload;

        } catch (JsonParseException exception) {
            throw new CodeGenError(
                    appSource.toString(),
                    call.line(),
                    "CPython produced invalid render-context JSON "
                            + "for function '"
                            + call.ownerFunctionName()
                            + "': "
                            + exception.getMessage()
            );
        }
    }

    private void validatePayload(
            TemplateCall call,
            CapturedRenderPayload payload
    ) {
        /*
         * The statically discovered call and the call CPython actually
         * executed must refer to the same template.
         */
        if (!call.templateName().equals(payload.templateName())) {
            throw new CodeGenError(
                    appSource.toString(),
                    call.line(),
                    "Function '"
                            + call.ownerFunctionName()
                            + "' was expected to render '"
                            + call.templateName()
                            + "', but CPython rendered '"
                            + payload.templateName()
                            + "'"
            );
        }

        /*
         * This also proves that the runtime result belongs to the
         * TemplateCall we discovered earlier.
         */
        if (!call.contextArguments().keySet()
                .equals(payload.context().keySet())) {

            throw new CodeGenError(
                    appSource.toString(),
                    call.line(),
                    "The captured context does not match the statically "
                            + "discovered render_template call. Expected "
                            + call.contextArguments().keySet()
                            + " but received "
                            + payload.context().keySet()
            );
        }
    }

    private String executeCaptureAsJsonScript(
            TemplateCall call
    ) {
        String functionName = call.ownerFunctionName();

        if ("<module>".equals(functionName)) {
            throw new CodeGenError(
                    appSource.toString(),
                    call.line(),
                    "A module-level render_template call cannot be "
                            + "executed as a Python function"
            );
        }

        ProcessBuilder processBuilder = getProcessBuilder(functionName);

        processBuilder.environment().put(
                "PYTHONIOENCODING",
                "utf-8"
        );

        try {
            Process process = processBuilder.start();

            StringBuilder completeOutput =
                    new StringBuilder();

            String capturedJson = null;

            try (BufferedReader reader =
                         new BufferedReader(
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
                        call.line(),
                        "CPython execution failed for function '"
                                + functionName
                                + "':\n"
                                + completeOutput.toString().stripTrailing()
                );
            }

            if (capturedJson == null) {
                throw new CodeGenError(
                        appSource.toString(),
                        call.line(),
                        "Function '"
                                + functionName
                                + "' did not produce a render context. "
                                + "CPython output:\n"
                                + completeOutput.toString().stripTrailing()
                );
            }

            return capturedJson;

        } catch (IOException exception) {
            throw new CodeGenError(
                    appSource.toString(),
                    "Could not start CPython for function '"
                            + functionName
                            + "'",
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

    private ProcessBuilder getProcessBuilder(String functionName) {
        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        pythonExecutable
                                .toAbsolutePath()
                                .toString(),

                        captureScript
                                .toAbsolutePath()
                                .toString(),

                        appSource
                                .toAbsolutePath()
                                .toString(),

                        functionName
                );

        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }
}