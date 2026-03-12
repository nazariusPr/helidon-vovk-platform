package org.nazarius.http;

import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import io.helidon.webserver.security.SecurityFeature;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.nazarius.service.AuthService;

public class AuthHttpService implements HttpService {

    private final AuthService authService;

    public AuthHttpService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void routing(HttpRules httpRules) {
        // POST /auth/login -> authenticate or create user
        httpRules.post("/", SecurityFeature.allowAnonymous(), this::authenticate);
    }

    private void authenticate(ServerRequest req, ServerResponse res) {
        // Read JSON body
        JsonObject json = req.content().as(JsonObject.class);
        String username = json.getString("username", null);
        String password = json.getString("password", null);

        if (username == null || password == null) {
            res.status(400).send(
                    Json.createObjectBuilder()
                            .add("error", "username and password are required")
                            .build()
            );
            return;
        }

        // Authenticate or register user
        String token = authService.authenticate(username, password);

        // Return token in JSON
        JsonObject responseJson = Json.createObjectBuilder()
                .add("accessToken", token)
                .build();

        res.send(responseJson);

    }
}