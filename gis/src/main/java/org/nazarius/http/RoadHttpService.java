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
import jakarta.json.JsonObjectBuilder;
import org.nazarius.dto.RoadDto;
import org.nazarius.security.Roles;
import org.nazarius.service.RoadService;

import java.util.List;
import java.util.Optional;

public class RoadHttpService implements HttpService {

    private final RoadService roadService;

    public RoadHttpService(RoadService roadService) {
        this.roadService = roadService;
    }

    @Override
    public void routing(HttpRules rules) {
        rules
                // READ all roads
                .get("/roads",
                        SecurityFeature.allowAnonymous(),
                        this::getAll)

                // READ by ID
                .get("/roads/{id}",
                        SecurityFeature.allowAnonymous(),
                        this::getById)

                // READ by name
                .get("/roads/by-name/{name}",
                        SecurityFeature.allowAnonymous(),
                        this::getByName);
    }

    /* =========================
       READ OPERATIONS
       ========================= */

    private void getAll(ServerRequest req, ServerResponse res) {
        List<RoadDto> dtos = roadService.getAllRoads();
        res.send(toJsonArray(dtos));
    }

    private void getById(ServerRequest req, ServerResponse res) {
        Integer id = Integer.parseInt(req.path().pathParameters().get("id"));
        RoadDto dto = roadService.getRoadById(id);

        res.send(toJsonObject(dto));
    }

    private void getByName(ServerRequest req, ServerResponse res) {
        String name = req.path().pathParameters().get("name");
        RoadDto dto = roadService.getRoadByName(name);

        res.send(toJsonObject(dto));
    }

    /* =========================
       JSON HELPERS
       ========================= */

    private JsonObject toJsonObject(RoadDto dto) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (dto.getId() != null) builder.add("id", dto.getId());
        if (dto.getName() != null) builder.add("name", dto.getName());
        if (dto.getGeomWkt() != null)
            builder.add("geom", dto.getGeomWkt()); // WKT representation
        if (dto.getLengthMeters() != null)
            builder.add("lengthMeters", dto.getLengthMeters());

        return builder.build();
    }

    private JsonArray toJsonArray(List<RoadDto> dtos) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        dtos.forEach(dto -> builder.add(toJsonObject(dto)));
        return builder.build();
    }

    private JsonObject error(String message) {
        return Json.createObjectBuilder()
                .add("error", message)
                .build();
    }
}