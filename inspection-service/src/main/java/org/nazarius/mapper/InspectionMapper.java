package org.nazarius.mapper;

import org.nazarius.model.Inspection;
import org.nazarius.dto.InspectionDto;

public class InspectionMapper {

    // Convert Inspection entity to InspectionDto
    public static InspectionDto toDto(Inspection inspection) {
        if (inspection == null) return null;

        return new InspectionDto(
                inspection.getId(),
                inspection.getInspectionCode(),
                inspection.getTitle(),
                inspection.getDescription(),
                inspection.getInspectionType(),
                inspection.getLocation(),
                inspection.getScheduledDate(),
                inspection.getCompletedDate(),
                inspection.getStatus(),
                inspection.getInspector()
        );
    }

    // Convert InspectionDto to Inspection entity
    public static Inspection toEntity(InspectionDto dto) {
        if (dto == null) return null;

        return new Inspection(
                dto.getId(),
                dto.getInspectionCode(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getInspectionType(),
                dto.getLocation(),
                dto.getScheduledDate(),
                dto.getCompletedDate(),
                dto.getStatus(),
                dto.getInspector()
        );
    }
}