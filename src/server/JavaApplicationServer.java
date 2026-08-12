package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.http.BadRequestException;
import server.http.ServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class JavaApplicationServer {

    private final HttpServer httpServer;
    private final ExecutorService executor;
    private final ApplicationRequestDispatcher dispatcher;
    private final FlaskRequestReader requestReader = new FlaskRequestReader();

    private boolean running;

    public JavaApplicationServer(
            int port,
            ApplicationRequestDispatcher dispatcher
    ) throws IOException {
        this.dispatcher =
                Objects.requireNonNull(dispatcher);

        this.httpServer =
                HttpServer.create(
                        new InetSocketAddress(
                                "localhost",
                                port
                        ),
                        0
                );

        /*
         * The Python runtime contains mutable application state.
         * A single request thread prevents concurrent mutations
         * until explicit synchronization is implemented.
         */
        this.executor =
                Executors.newSingleThreadExecutor();

        httpServer.setExecutor(executor);

        httpServer.createContext(
                "/",
                this::handleRequest
        );
    }

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException(
                    "HTTP server is already running"
            );
        }

        running = true;
        httpServer.start();
    }

    public synchronized void stop(
            int delaySeconds
    ) {
        if (!running) {
            return;
        }

        running = false;

        httpServer.stop(delaySeconds);
        executor.shutdown();
    }

    public int port() {
        return httpServer
                .getAddress()
                .getPort();
    }

    private void handleRequest(
            HttpExchange exchange
    ) throws IOException {
        String method =
                exchange.getRequestMethod();

        String path =
                exchange.getRequestURI()
                        .getPath();

        ServerResponse response;

        try {
            response =
                    dispatcher.dispatch(
                            requestReader.read(exchange)
                    );

        }
        catch (
            BadRequestException exception) {
            response =
                ServerResponse.text(
                        400,
                        "Bad Request"
                );

        }
        catch (RuntimeException exception2) {
            exception2.printStackTrace(System.err);

            response =
                    ServerResponse.text(
                            500,
                            "Internal Server Error"
                    );
        }

        System.out.printf(
                "%s %s -> %d%n",
                method,
                path,
                response.statusCode()
        );

        writeResponse(
                exchange,
                response
        );
    }

    private void writeResponse(
            HttpExchange exchange,
            ServerResponse response
    ) throws IOException {
        for (Map.Entry<String, String> header
                : response.headers().entrySet()) {

            exchange.getResponseHeaders()
                    .set(
                            header.getKey(),
                            header.getValue()
                    );
        }

        byte[] body =
                response.body();

        exchange.sendResponseHeaders(
                response.statusCode(),
                body.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(body);
        }
    }
}