package org.nazarius.http;

import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import io.helidon.webserver.security.SecurityFeature;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.dto.AuthDto;
import org.nazarius.service.UserService;
import org.nazarius.dto.UserDto;
import org.nazarius.security.Roles;

import java.util.List;

import static org.nazarius.utils.SecurityUtils.getUsername;

public class UserHttpService implements HttpService {

    private final UserService userService;

    public UserHttpService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules
                .get("/me",
                        SecurityFeature.authenticate(),
                        this::getCurrentUser)

                .get("/page",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getUsersPage)

                .get("/",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::getAllUsers)

                .get("/by-username/{username}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getUserByUsername)

                .get("/by-role/{role}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::getUsersByRole)

                .get("/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getUserById)

                .post("/",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::createUser)

                .delete("/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::deleteUserById);
    }

    private void getCurrentUser(ServerRequest req, ServerResponse res) {
        String username = getUsername(req);

        UserDto userDto = userService.getUserByUsername(username);
        if (userDto != null) {
            res.send(toJsonObject(userDto));
        } else {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", "User not found")
                    .build());
        }
    }

    private void getAllUsers(ServerRequest req, ServerResponse res) {
        List<UserDto> users = userService.getAllUsers();
        res.send(toJsonArray(users));
    }

    private void getUsersPage(ServerRequest req, ServerResponse res) {
        int page = 0;
        int size = 10;

        String pageParam = req.query().get("page");
        String sizeParam = req.query().get("size");

        if (pageParam != null) page = Integer.parseInt(pageParam);
        if (sizeParam != null) size = Integer.parseInt(sizeParam);

        Pageable pageable = new Pageable(page, size);
        Page<UserDto> userPage = userService.readPage(pageable);

        JsonObject result = Json.createObjectBuilder()
                .add("page", userPage.getCurrentPage())
                .add("size", userPage.getElems().size())
                .add("totalElements", userPage.getTotalElems())
                .add("totalPages", userPage.getTotalPages())
                .add("content", toJsonArray(userPage.getElems()))
                .build();

        res.send(result);
    }

    private void getUserById(ServerRequest req, ServerResponse res) {
        int id = Integer.parseInt(req.path().pathParameters().get("id"));
        UserDto user = userService.getUserById(id);

        if (user != null) {
            res.send(toJsonObject(user));
        } else {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", "User not found")
                    .build());
        }
    }

    private void getUserByUsername(ServerRequest req, ServerResponse res) {
        String username = req.path().pathParameters().get("username");
        UserDto user = userService.getUserByUsername(username);

        if (user != null) {
            res.send(toJsonObject(user));
        } else {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", "User not found")
                    .build());
        }
    }

    private void getUsersByRole(ServerRequest req, ServerResponse res) {
        String role = req.path().pathParameters().get("role");
        List<UserDto> users = userService.getUsersByRole(role);
        res.send(toJsonArray(users));
    }

    private void createUser(ServerRequest req, ServerResponse res) {
        JsonObject json = req.content().as(JsonObject.class);
        AuthDto authDto = new AuthDto();
        authDto.setUsername(json.getString("username"));
        authDto.setPassword(json.getString("password"));
        authDto.setRole(Roles.VIEWER);

        UserDto created = userService.create(authDto);
        res.status(201).send(toJsonObject(created));
    }

    private void deleteUserById(ServerRequest req, ServerResponse res) {
        int id = Integer.parseInt(req.path().pathParameters().get("id"));
        userService.deleteById(id);
        res.status(204).send(); // No Content
    }

    /**
     * Helper: Convert UserDto to JsonObject
     */
    private JsonObject toJsonObject(UserDto user) {
        return Json.createObjectBuilder()
                .add("id", user.getId())
                .add("username", user.getUsername())
                .add("role", user.getRole())
                .build();
    }

    /**
     * Helper: Convert List<UserDto> to JsonArray
     */
    private JsonArray toJsonArray(List<UserDto> users) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (UserDto user : users) {
            arrayBuilder.add(toJsonObject(user));
        }
        return arrayBuilder.build();
    }
}