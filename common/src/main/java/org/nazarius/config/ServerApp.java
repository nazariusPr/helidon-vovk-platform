package org.nazarius.config;

import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import jakarta.json.Json;

import java.util.function.Consumer;

public abstract class ServerApp {

    private Runnable beforeStartHook;
    private Consumer<WebServer> afterStartHook;

    /**
     * Override this to provide routing for your service.
     */
    protected abstract HttpRouting.Builder createRouting();

    /**
     * Extension point for adding features (OpenAPI, metrics, etc.)
     */
    protected abstract void configureFeatures(WebServerConfig.Builder serverBuilder);

    public ServerApp beforeStart(Runnable hook) {
        this.beforeStartHook = hook;
        return this;
    }

    public ServerApp afterStart(Consumer<WebServer> hook) {
        this.afterStartHook = hook;
        return this;
    }

    public void start() {
        // Run before-start hook
        if (beforeStartHook != null) {
            beforeStartHook.run();
        }

        HttpRouting.Builder routingBuilder = createRouting();
        registerExceptionHandlers(routingBuilder);


        // Build server
        WebServerConfig.Builder serverBuilder = WebServer.builder()
                .routing(routingBuilder)
                .port(getPort());

        // Allow subclasses to add features
        configureFeatures(serverBuilder);

        // Build the server
        WebServer server = serverBuilder.build();

        // Start server
        try {
            server.start();
            // After start hook
            if (afterStartHook != null) {
                afterStartHook.accept(server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Default centralized exception registration.
     * Child classes can override to add or change behavior.
     */
    protected void registerExceptionHandlers(HttpRouting.Builder routingBuilder) {
        routingBuilder.error(NumberFormatException.class, (req, res, ex) -> {
            res.status(400).send(Json.createObjectBuilder()
                    .add("error", "Invalid number format: " + ex.getMessage())
                    .build());
        });

        routingBuilder.error(IllegalArgumentException.class, (req, res, ex) -> {
            res.status(400).send(Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build());
        });


        routingBuilder.error(IllegalStateException.class, (req, res, ex) -> {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build());
        });

        routingBuilder.error(Exception.class, (req, res, ex) -> {
            ex.printStackTrace();
            res.status(500).send(Json.createObjectBuilder()
                    .add("error", "Internal Server Error: " + ex.getMessage())
                    .build());
        });
    }

    /**
     * Default port, can be overridden
     */
    protected int getPort() {
        return 8080;
    }
}
