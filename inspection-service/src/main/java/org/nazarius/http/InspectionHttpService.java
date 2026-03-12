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
import org.nazarius.dto.InspectionDto;
import org.nazarius.security.Roles;
import org.nazarius.service.InspectionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InspectionHttpService implements HttpService {

    private final InspectionService inspectionService;

    public InspectionHttpService(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @Override
    public void routing(HttpRules rules) {
        rules

                // READ endpoints
                .get("/inspections",
                        SecurityFeature.allowAnonymous(),
                        // SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getAll)

                .get("/inspections/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getById)

                .get("/inspections/by-code/{code}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getByCode)

                // WRITE endpoints
                .post("/inspections",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::create)

                .put("/inspections/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::update)

                .delete("/inspections/in-memory/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::deleteInMemoryById)

                .delete("/inspections/in-db/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::deleteInDbById)

                .post("/inspections/flush",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::flush);
    }

    /* =========================
       READ
       ========================= */

    private void getAll(ServerRequest req, ServerResponse res) {
        List<InspectionDto> dtos = inspectionService.findAll();
        res.send(toJsonArray(dtos));
    }

    private void getById(ServerRequest req, ServerResponse res) {
        Integer id = Integer.parseInt(req.path().pathParameters().get("id"));
        Optional<InspectionDto> dto = inspectionService.findById(id);

        if (dto.isPresent()) {
            res.send(toJsonObject(dto.get()));
        } else {
            res.status(404).send(error("Inspection not found"));
        }
    }

    private void getByCode(ServerRequest req, ServerResponse res) {
        String code = req.path().pathParameters().get("code");
        Optional<InspectionDto> dto = inspectionService.findByInspectionCode(code);

        if (dto.isPresent()) {
            res.send(toJsonObject(dto.get()));
        } else {
            res.status(404).send(error("Inspection not found"));
        }
    }

    /* =========================
       WRITE
       ========================= */

    private void create(ServerRequest req, ServerResponse res) {
        JsonObject json = req.content().as(JsonObject.class);
        InspectionDto dto = mapJsonToDto(json, null);

        int id = inspectionService.save(dto);

        res.status(201).send(Json.createObjectBuilder()
                .add("id", id)
                .add("message", "Buffered in memory")
                .build());
    }

    private void update(ServerRequest req, ServerResponse res) {
        Integer id = Integer.parseInt(req.path().pathParameters().get("id"));
        JsonObject json = req.content().as(JsonObject.class);
        InspectionDto dto = mapJsonToDto(json, id);

        int updated = inspectionService.update(dto);

        res.send(Json.createObjectBuilder()
                .add("updated", updated)
                .build());
    }

    private void deleteInMemoryById(ServerRequest req, ServerResponse res) {
        Integer id = Integer.parseInt(req.path().pathParameters().get("id"));
        int deleted = inspectionService.deleteInMemoryById(id);

        res.send(Json.createObjectBuilder()
                .add("deleted", deleted)
                .add("message", "Deleted from in-memory storage")
                .build());
    }

    private void deleteInDbById(ServerRequest req, ServerResponse res) {
        Integer id = Integer.parseInt(req.path().pathParameters().get("id"));
        int deleted = inspectionService.deleteInDbById(id);

        res.send(Json.createObjectBuilder()
                .add("deleted", deleted)
                .add("message", "Deleted from database")
                .build());
    }

    private void flush(ServerRequest req, ServerResponse res) {
        int flushed = inspectionService.flushToDatabase();
        res.send(Json.createObjectBuilder()
                .add("flushed", flushed)
                .add("message", "In-memory data written to database")
                .build());
    }

    /* =========================
       JSON HELPERS
       ========================= */

    private JsonObject toJsonObject(InspectionDto dto) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (dto.getId() != null) builder.add("id", dto.getId());
        if (dto.getInspectionCode() != null) builder.add("inspectionCode", dto.getInspectionCode());
        if (dto.getTitle() != null) builder.add("title", dto.getTitle());
        if (dto.getDescription() != null) builder.add("description", dto.getDescription());
        if (dto.getInspectionType() != null) builder.add("inspectionType", dto.getInspectionType());
        if (dto.getLocation() != null) builder.add("location", dto.getLocation());
        if (dto.getScheduledDate() != null)
            builder.add("scheduledDate", dto.getScheduledDate().toString());
        if (dto.getCompletedDate() != null)
            builder.add("completedDate", dto.getCompletedDate().toString());
        if (dto.getStatus() != null) builder.add("status", dto.getStatus());
        if (dto.getInspector() != null) builder.add("inspector", dto.getInspector());

        return builder.build();
    }

    private JsonArray toJsonArray(List<InspectionDto> list) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        list.forEach(dto -> builder.add(toJsonObject(dto)));
        return builder.build();
    }

    private JsonObject error(String message) {
        return Json.createObjectBuilder()
                .add("error", message)
                .build();
    }

    private InspectionDto mapJsonToDto(JsonObject json, Integer id) {
        InspectionDto dto = new InspectionDto();
        if (id != null) dto.setId(id);

        dto.setId(json.getInt("id"));
        dto.setInspectionCode(json.getString("inspectionCode", null));
        dto.setTitle(json.getString("title", null));
        dto.setDescription(json.getString("description", null));
        dto.setInspectionType(json.getString("inspectionType", null));
        dto.setLocation(json.getString("location", null));
        dto.setStatus(json.getString("status", null));
        dto.setInspector(json.getString("inspector", null));

        if (json.containsKey("scheduledDate") && !json.isNull("scheduledDate")) {
            dto.setScheduledDate(LocalDate.parse(json.getString("scheduledDate")));
        }
        if (json.containsKey("completedDate") && !json.isNull("completedDate")) {
            dto.setCompletedDate(LocalDate.parse(json.getString("completedDate")));
        }

        return dto;
    }
}