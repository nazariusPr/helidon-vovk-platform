package org.nazarius.http;

import io.helidon.http.media.multipart.MultiPart;
import io.helidon.http.media.multipart.ReadablePart;
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
import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.dto.EquipmentDto;
import org.nazarius.security.Roles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EquipmentHttpService implements HttpService {

    private final org.nazarius.service.EquipmentService equipmentService;

    public EquipmentHttpService(org.nazarius.service.EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules

                // CSV upload
                .post("/equipments/upload",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::uploadCsv)

                // pagination
                .get("/equipments/page",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getEquipmentsPage)

                // lookup by equipment number
                .get("/equipments/by-number/{equipmentNumber}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getEquipmentByNumber)

                // get by id
                .get("/equipments/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getEquipmentById)

                // update
                .put("/equipments/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::updateEquipment)

                // delete by id
                .delete("/equipments/{id}",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::deleteEquipmentById)

                // list all
                .get("/equipments",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN, Roles.INSPECTOR),
                        this::getAllEquipments)

                // create
                .post("/equipments",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::createEquipment)

                // delete all
                .delete("/equipments",
                        SecurityFeature.authenticate(),
                        SecurityFeature.rolesAllowed(Roles.ADMIN),
                        this::deleteAllEquipments);
    }

    private void getAllEquipments(ServerRequest req, ServerResponse res) {
        List<EquipmentDto> equipments = equipmentService.getAllEquipments();
        res.send(toJsonArray(equipments));
    }

    private void getEquipmentsPage(ServerRequest req, ServerResponse res) {
        int page = 0;
        int size = 10;

        String pageParam = req.query().get("page");
        String sizeParam = req.query().get("size");

        if (pageParam != null) page = Integer.parseInt(pageParam);
        if (sizeParam != null) size = Integer.parseInt(sizeParam);

        Pageable pageable = new Pageable(page, size);
        Page<EquipmentDto> equipmentPage = equipmentService.readPage(pageable);

        JsonObject result = Json.createObjectBuilder()
                .add("page", equipmentPage.getCurrentPage())
                .add("size", equipmentPage.getElems().size())
                .add("totalElements", equipmentPage.getTotalElems())
                .add("totalPages", equipmentPage.getTotalPages())
                .add("content", toJsonArray(equipmentPage.getElems()))
                .build();

        res.send(result);
    }

    private void getEquipmentById(ServerRequest req, ServerResponse res) {
        int id = Integer.parseInt(req.path().pathParameters().get("id"));
        EquipmentDto equipment = equipmentService.getById(id);

        if (equipment != null) {
            res.send(toJsonObject(equipment));
        } else {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", "Equipment not found")
                    .build());
        }
    }

    private void getEquipmentByNumber(ServerRequest req, ServerResponse res) {
        String number = req.path().pathParameters().get("equipmentNumber");
        EquipmentDto equipment = equipmentService.getByEquipmentNumber(number);

        if (equipment != null) {
            res.send(toJsonObject(equipment));
        } else {
            res.status(404).send(Json.createObjectBuilder()
                    .add("error", "Equipment not found")
                    .build());
        }
    }

    private void createEquipment(ServerRequest req, ServerResponse res) {
        JsonObject json = req.content().as(JsonObject.class);
        EquipmentDto dto = mapJsonToDto(json, null);

        EquipmentDto created = equipmentService.create(dto);
        res.status(201).send(toJsonObject(created));
    }

    private void updateEquipment(ServerRequest req, ServerResponse res) {
        int id = Integer.parseInt(req.path().pathParameters().get("id"));
        JsonObject json = req.content().as(JsonObject.class);
        EquipmentDto dto = mapJsonToDto(json, id);

        EquipmentDto updated = equipmentService.update(dto);
        res.send(toJsonObject(updated));
    }

    private void deleteEquipmentById(ServerRequest req, ServerResponse res) {
        int id = Integer.parseInt(req.path().pathParameters().get("id"));
        equipmentService.deleteById(id);
        res.status(204).send(); // No Content
    }

    private void deleteAllEquipments(ServerRequest req, ServerResponse res) {
        int deletedCount = equipmentService.deleteAll();
        res.send(Json.createObjectBuilder()
                .add("deleted", deletedCount)
                .build());
    }

    private void uploadCsv(ServerRequest req, ServerResponse res) {
        MultiPart multiPart = req.content().as(MultiPart.class);

        byte[] csvBytes = null;
        char delimiter = ',';       // default
        boolean hasHeader = true;   // default

        // Iterate parts once
        while (multiPart.hasNext()) {
            ReadablePart part = multiPart.next();
            String name = part.name();

            if ("file".equals(name)) {
                try (InputStream is = part.inputStream();
                     ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                    is.transferTo(buffer);
                    csvBytes = buffer.toByteArray();

                } catch (IOException e) {
                    res.status(400);
                    res.send(Json.createObjectBuilder()
                            .add("error", "Failed to read uploaded file")
                            .build());
                    return;
                }
            } else if ("delimiter".equals(name)) {
                String val = part.as(String.class).trim();
                if (!val.isEmpty()) {
                    delimiter = val.charAt(0);
                }
            } else if ("hasHeader".equals(name)) {
                String val = part.as(String.class).trim();
                if (!val.isEmpty()) {
                    hasHeader = Boolean.parseBoolean(val);
                }
            }
        }

        int uploaded = equipmentService.uploadCsv(csvBytes, hasHeader, delimiter);

        res.send(Json.createObjectBuilder()
                .add("uploaded", uploaded)
                .build());
    }

    /**
     * Helper: Convert EquipmentDto to JsonObject
     */
    private JsonObject toJsonObject(EquipmentDto dto) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (dto.getId() != null) builder.add("id", dto.getId());
        if (dto.getEquipmentNumber() != null) builder.add("equipmentNumber", dto.getEquipmentNumber());
        if (dto.getName() != null) builder.add("name", dto.getName());
        if (dto.getType() != null) builder.add("type", dto.getType());
        if (dto.getManufacturer() != null) builder.add("manufacturer", dto.getManufacturer());
        if (dto.getYear() != null) builder.add("year", dto.getYear());
        if (dto.getStatus() != null) builder.add("status", dto.getStatus());
        return builder.build();
    }

    /**
     * Helper: Convert List<EquipmentDto> to JsonArray
     */
    private JsonArray toJsonArray(List<EquipmentDto> dtos) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (EquipmentDto dto : dtos) {
            arrayBuilder.add(toJsonObject(dto));
        }
        return arrayBuilder.build();
    }

    /**
     * Helper: Map a JsonObject from the request payload to an EquipmentDto.
     */
    private EquipmentDto mapJsonToDto(JsonObject json, Integer id) {
        EquipmentDto dto = new EquipmentDto();
        if (id != null) dto.setId(id);

        dto.setEquipmentNumber(json.getString("equipmentNumber", null));
        dto.setName(json.getString("name", null));
        dto.setType(json.getString("type", null));
        dto.setManufacturer(json.getString("manufacturer", null));
        if (json.containsKey("year")) dto.setYear(json.getInt("year"));
        dto.setStatus(json.getString("status", null));

        return dto;
    }
}