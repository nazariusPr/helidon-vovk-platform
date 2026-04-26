package org.nazarius.mapper;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.nazarius.dto.RoadDto;
import org.nazarius.model.Road;

public class RoadMapper {

    private static final WKTWriter wktWriter = new WKTWriter();
    private static final WKTReader wktReader = new WKTReader();

    // Convert Road entity to RoadDto
    public static RoadDto toDto(Road road) {
        if (road == null) return null;

        String geomWkt = null;
        if (road.getGeom() != null) {
            geomWkt = wktWriter.write(road.getGeom());
        }

        return new RoadDto(
                road.getId(),
                road.getName(),
                geomWkt,
                road.getLengthMeters()
        );
    }

    // Convert RoadDto to Road entity
    public static Road toEntity(RoadDto dto) {
        if (dto == null) return null;

        LineString geom = null;
        if (dto.getGeomWkt() != null && !dto.getGeomWkt().isBlank()) {
            try {
                Geometry geometry = wktReader.read(dto.getGeomWkt());
                if (geometry instanceof LineString) {
                    geom = (LineString) geometry;
                } else {
                    throw new IllegalArgumentException("Provided WKT is not a LineString: " + dto.getGeomWkt());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse WKT: " + dto.getGeomWkt(), e);
            }
        }

        return new Road(
                dto.getId(),
                dto.getName(),
                geom,
                dto.getLengthMeters()
        );
    }
}